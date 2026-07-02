package de.htwg.se.colorwoodSort.controller

import de.htwg.se.colorwoodSort.model.*
import de.htwg.se.colorwoodSort.util.Command

// Command Pattern: Ein Move wird als Objekt gekapselt.
// Dadurch kann der Controller den Zug ausführen und später rückgängig machen.
// Dependency Injection: Die Regel-Komponente wird als Interface hereingereicht.
case class MoveCommand(from: Int, to: Int, before: GameState, rules: GameRulesInterface = GameRules) extends Command[GameState] {

  override def doStep(state: GameState): GameState =
    rules.move(state, from, to)

  override def undoStep(state: GameState): GameState =
    before

  // Redo: Der Zug wird auf dem aktuellen (durch Undo zurückgesetzten) Zustand erneut ausgeführt.
  override def redoStep(state: GameState): GameState =
    rules.move(state, from, to)
}
