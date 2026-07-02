package de.htwg.se.colorwoodSort.controller

import de.htwg.se.colorwoodSort.model.{GameState, GeneratorStrategy, MediumGenerator}
import de.htwg.se.colorwoodSort.util.Observable

/** ------------------------------------------------------- Task 10: Component Interface -------------------------------------------------------
  *
  * Interface der Controller-Komponente.
  *
  * Alle anderen Komponenten (TUI, GUI, Main) greifen ausschliesslich auf dieses Interface zu,
  * niemals auf die konkrete Controller-Klasse. Dadurch sind die inneren Abläufe des Controllers
  * (State Pattern, UndoManager, Parser, ...) nach aussen vollstaendig gekapselt und die
  * Implementierung kann jederzeit ausgetauscht werden.
  *
  * Das Interface erbt von Observable[ControllerEvent]: Views registrieren sich als Observer
  * und werden ueber jede Zustandsaenderung benachrichtigt (Observer Pattern).
  */
trait ControllerInterface extends Observable[ControllerEvent] {

  /** Aktueller Spielzustand (nur lesend fuer die Views). */
  def gameState: GameState

  /** Startet ein neues Spiel mit der gegebenen Groesse, den Farben und dem Schwierigkeitsgrad. */
  def startGame(
      pipes: Int,
      height: Int,
      colorStrings: List[String],
      strategy: GeneratorStrategy = MediumGenerator
  ): Unit

  /** Nimmt eine Nutzereingabe entgegen (Zug "from to", "u", "r", "q"). */
  def processInput(input: String): Unit

  /** Macht den letzten Zug rueckgaengig. */
  def undo(): Unit

  /** Stellt den zuletzt rueckgaengig gemachten Zug wieder her. */
  def redo(): Unit

  def canUndo: Boolean
  def canRedo: Boolean

  /** True, wenn das Spiel beendet ist (gewonnen oder abgebrochen). */
  def isFinished: Boolean
}
