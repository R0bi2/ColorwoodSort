package de.htwg.se.colorwoodSort.controller

import de.htwg.se.colorwoodSort.model.*
import de.htwg.se.colorwoodSort.util.Command

// Command Pattern: Ein Move wird als Objekt gekapselt.
// Dadurch kann der Controller den Zug ausführen und später rückgängig machen.
case class MoveCommand(from: Int, to: Int, before: GameState) extends Command[GameState] {

  override def doStep(state: GameState): GameState =
    move(state, from, to)

  override def undoStep(state: GameState): GameState =
    before
}
