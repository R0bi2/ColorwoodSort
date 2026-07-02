package de.htwg.se.colorwoodSort.controller

import com.google.inject.Inject
import de.htwg.se.colorwoodSort.model.*
import de.htwg.se.colorwoodSort.util.{Observable, UndoManager}
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
    } else if input.trim.equalsIgnoreCase("r") || input.trim.equalsIgnoreCase("redo") then { // implement redo
      controller.redo()
    } else {
      // Zug versuchen auszuführen
      val newState = controller.executeMove(input, controller.gameState)

      if newState == controller.gameState then {
        controller.notifyObservers(ControllerEvent.Message("Invalid move or input"))
      } else {
        // Zug war erfolgreich, Status updaten und View benachrichtigen
        controller.gameState = newState
        controller.notifyObservers(ControllerEvent.StateChanged(controller.gameState))

        // Prüfen, ob durch den Zug gewonnen wurde (ueber die injizierte Regel-Komponente)
        if controller.rules.isSolved(controller.gameState) then {
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

// Task 10: Der Controller implementiert das ControllerInterface. Nach aussen (TUI, GUI, Main)
// ist nur das Interface sichtbar, die inneren Abläufe bleiben gekapselt.
// Dependency Injection: Guice injiziert die Model-Komponenten (Strategie + Regeln) ueber den
// Konstruktor. Die Default-Werte erlauben weiterhin ein einfaches `Controller()` in den Tests.
class Controller @Inject() (
    var generatorStrategy: GeneratorStrategy = MediumGenerator,
    val rules: GameRulesInterface = GameRules
) extends ControllerInterface {

  // Task 7: Der Controller hält jetzt passiv zwei Dinge: Den Spielablauf-Status und die Spieldaten
  var workflowState: ControllerState = PlayingState
  var gameState: GameState = uninitialized

  // Task 10: Views fragen nur noch dieses Interface-Merkmal ab statt des inneren workflowState
  override def isFinished: Boolean = workflowState == FinishedState

  // Command Pattern + Undo/Redo: Der UndoManager verwaltet die Undo- und Redo-Stapel.
  val undoManager: UndoManager[GameState] = new UndoManager[GameState]()

  override def canUndo: Boolean = undoManager.canUndo
  override def canRedo: Boolean = undoManager.canRedo

  // Task 7: Die Start-Methode braucht keine readInput-Funktion mehr und ruft keine Schleife mehr auf
  override def startGame(
      pipes: Int,
      height: Int,
      colorStrings: List[String],
      strategy: GeneratorStrategy
  ): Unit = {
    generatorStrategy = strategy
    val colors = colorStrings.map(parseColor)
    gameState = generatorStrategy.generate(pipes, height, colors)
    workflowState = PlayingState
    undoManager.clear()
    notifyObservers(ControllerEvent.StateChanged(gameState)) // Start-Zustand printen
  }

  // Task 7: Diese Methode wird von deiner TUI aufgerufen, wenn der Nutzer etwas eintippt
  override def processInput(input: String): Unit = {
    // Der Controller leitet den Input blind an den aktuellen Zustand weiter
    workflowState.handleInput(input, this)
  }

  // Erstellt einen MoveCommand und lässt ihn vom UndoManager ausführen.
  // Der UndoManager speichert ihn nur dann auf dem Undo-Stapel, wenn der Zug gültig war.
  def executeMove(input: String, state: GameState): GameState = {
    parseMove(input, state.pipes.size) match { // Task 8: Try Monade inside parseMove
      case Some((from, to)) =>
        undoManager.doStep(state, MoveCommand(from, to, state, rules))
      case None => state
    }
  }

  // Macht den letzten Zug rückgängig (Undo-Stapel).
  override def undo(): Unit = {
    undoManager.undoStep(gameState) match {
      case Some(newState) =>
        gameState = newState
        notifyObservers(ControllerEvent.StateChanged(gameState))
      case None =>
        notifyObservers(ControllerEvent.Message("Nothing to undo."))
    }
  }

  // Stellt den zuletzt rückgängig gemachten Zug wieder her (Redo-Stapel).
  override def redo(): Unit = {
    undoManager.redoStep(gameState) match {
      case Some(newState) =>
        gameState = newState
        notifyObservers(ControllerEvent.StateChanged(gameState))
      case None =>
        notifyObservers(ControllerEvent.Message("Nothing to redo."))
    }
  }
}
