package de.htwg.se.colorwoodSort.controller

import de.htwg.se.colorwoodSort.model.*
import de.htwg.se.colorwoodSort.util.Observable
import scala.compiletime.uninitialized

// --- DAS STATE PATTERN ---

// 1. Das gemeinsame Interface für alle Zustände
sealed trait ControllerState {
  def handleInput(input: String, controller: Controller): Unit
}

// 2. Zustand: Das Spiel läuft
case object PlayingState extends ControllerState {
  override def handleInput(input: String, controller: Controller): Unit = {
    if input == null || input.trim.equalsIgnoreCase("q") then {
      controller.notifyObservers(ControllerEvent.Message("Game quit."))
      controller.workflowState = FinishedState
    } else {
      // Zug versuchen auszuführen
      val newState = controller.executeMove(input, controller.gameState)

      if newState == controller.gameState then {
        controller.notifyObservers(ControllerEvent.Message("Invalid move or input"))
      } else {
        // Zug war erfolgreich, Status updaten und View benachrichtigen
        controller.gameState = newState
        controller.notifyObservers(ControllerEvent.StateChanged(controller.gameState))

        // Prüfen, ob durch den Zug gewonnen wurde
        if isSolved(controller.gameState) then {
          controller.notifyObservers(ControllerEvent.Message("You solved it!"))
          controller.workflowState = FinishedState
        }
      }
    }
  }
}

// 3. Zustand: Das Spiel ist vorbei (Gewonnen oder Abgebrochen)
case object FinishedState extends ControllerState {
  override def handleInput(input: String, controller: Controller): Unit = {
    controller.notifyObservers(ControllerEvent.Message("Game is finished. Please restart."))
  }
}

// --- DER CONTROLLER ---

class Controller extends Observable[ControllerEvent] {

  // Der Controller hält jetzt passiv zwei Dinge: Den Spielablauf-Status und die Spieldaten
  var workflowState: ControllerState = PlayingState
  var gameState: GameState = uninitialized

  // Die Start-Methode braucht keine readInput-Funktion mehr und ruft keine Schleife mehr auf
  def startGame(pipes: Int, height: Int, colorStrings: List[String]): Unit = {
    val colors = colorStrings.map(parseColor)
    gameState = generator(pipes, height, colors)
    workflowState = PlayingState
    notifyObservers(ControllerEvent.StateChanged(gameState)) // Start-Zustand printen
  }

  // NEU: Diese Methode wird von deiner TUI aufgerufen, wenn der Nutzer etwas eintippt
  def processInput(input: String): Unit = {
    // Der Controller leitet den Input blind an den aktuellen Zustand weiter
    workflowState.handleInput(input, this)
  }

  // Hilfsmethode, die deine alte Logik für das Bewegen der Röhren kapselt
  def executeMove(input: String, state: GameState): GameState = {
    parseMove(input, state.pipes.size) match {
      case Some((from, to)) => move(state, from, to)
      case None             => state
    }
  }
}
