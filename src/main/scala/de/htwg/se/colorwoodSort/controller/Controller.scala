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
    } else if input.trim.equalsIgnoreCase("u") || input.trim.equalsIgnoreCase("undo") then { // Task 8: implement undo
      controller.undo()
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

  // Task 7: Der Controller hält jetzt passiv zwei Dinge: Den Spielablauf-Status und die Spieldaten
  var workflowState: ControllerState = PlayingState
  var gameState: GameState = uninitialized

  var undoHistory: List[MoveCommand] = Nil // Task 8: Speichert ausgeführte Commands für Undo.

  // Strategy Pattern: Der Controller kennt nur das GeneratorStrategy-Interface.
  // Der konkrete Schwierigkeitsgrad kann von außen ausgetauscht werden.
  var generatorStrategy: GeneratorStrategy = MediumGenerator

  // Task 7: Die Start-Methode braucht keine readInput-Funktion mehr und ruft keine Schleife mehr auf
  def startGame(
      pipes: Int,
      height: Int,
      colorStrings: List[String],
      strategy: GeneratorStrategy = generatorStrategy
  ): Unit = {
    generatorStrategy = strategy
    val colors = colorStrings.map(parseColor)
    gameState = generatorStrategy.generate(pipes, height, colors)
    workflowState = PlayingState
    undoHistory = Nil
    notifyObservers(ControllerEvent.StateChanged(gameState)) // Start-Zustand printen
  }

  // Task 7: Diese Methode wird von deiner TUI aufgerufen, wenn der Nutzer etwas eintippt
  def processInput(input: String): Unit = {
    // Der Controller leitet den Input blind an den aktuellen Zustand weiter
    workflowState.handleInput(input, this)
  }

  // Task 8: Erstellt einen MoveCommand und speichert ihn nur, wenn der Zug gültig war.
  def executeMove(input: String, state: GameState): GameState = {
    parseMove(input, state.pipes.size) match { // Task 8: Try Monade inside parseMove
      case Some((from, to)) =>
        val command = MoveCommand(from, to, state)
        val newState = command.doStep(state)
        if newState != state then
          undoHistory =
            command :: undoHistory // Task 8: Wenn sich der Spielstand geändert hat, war der Zug gültig und kommt auf den Undo-Stapel.
        newState
      case None => state
    }
  }

  // Task 8: Macht den letzten gespeicherten MoveCommand rückgängig.
  def undo(): Unit = {
    undoHistory match {
      case lastCommand :: rest =>
        gameState = lastCommand.undoStep(gameState)
        undoHistory = rest
        notifyObservers(ControllerEvent.StateChanged(gameState))
      case Nil =>
        notifyObservers(ControllerEvent.Message("Nothing to undo."))
    }
  }
}
