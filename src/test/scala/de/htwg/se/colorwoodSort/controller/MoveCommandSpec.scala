package de.htwg.se.colorwoodSort.controller

import de.htwg.se.colorwoodSort.model.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class MoveCommandSpec extends AnyWordSpec with Matchers {

  def testState: GameState =
    GameState(
      Vector(
        Pipe(2, List(Color.R)),
        Pipe(2, Nil)
      )
    )

  "A MoveCommand" should {

    "execute a move with doStep" in {
      val before = testState
      val command = MoveCommand(0, 1, before)

      val after = command.doStep(before)

      after.pipes(0).content shouldBe Nil
      after.pipes(1).content shouldBe List(Color.R)
    }

    "restore the stored before-state with undoStep" in {
      val before = testState
      val command = MoveCommand(0, 1, before)
      val after = command.doStep(before)

      command.undoStep(after) shouldBe before
    }

    "re-execute the move with redoStep" in {
      val before = testState
      val command = MoveCommand(0, 1, before)
      val after = command.doStep(before)
      val undone = command.undoStep(after)

      command.redoStep(undone) shouldBe after
    }

    "use the GameRules singleton as default rules component" in {
      MoveCommand(0, 1, testState).rules should be theSameInstanceAs GameRules
    }
  }
}
