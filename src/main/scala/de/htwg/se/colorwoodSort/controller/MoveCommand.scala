package de.htwg.se.colorwoodSort.controller

import de.htwg.se.colorwoodSort.model.*
import de.htwg.se.colorwoodSort.util.Command

/** Command Pattern: kapselt einen einzelnen Spielzug als ausfuehrbares Objekt.
  *
  * Der [[de.htwg.se.colorwoodSort.util.UndoManager]] speichert MoveCommands auf dem Undo-Stapel.
  * Undo stellt den gespeicherten `before`-Zustand wieder her; Redo fuehrt den Zug erneut aus.
  *
  * @param from   Quell-Pipe (0-basierter Index)
  * @param to     Ziel-Pipe (0-basierter Index)
  * @param before Spielstand vor dem Zug (fuer Undo)
  * @param rules  Regel-Komponente (Interface, injizierbar fuer Tests)
  */
case class MoveCommand(from: Int, to: Int, before: GameState, rules: GameRulesInterface = GameRules) extends Command[GameState] {

  override def doStep(state: GameState): GameState =
    rules.move(state, from, to)

  override def undoStep(state: GameState): GameState =
    before

  // Redo: Der Zug wird auf dem aktuellen (durch Undo zurückgesetzten) Zustand erneut ausgeführt.
  override def redoStep(state: GameState): GameState =
    rules.move(state, from, to)
}
