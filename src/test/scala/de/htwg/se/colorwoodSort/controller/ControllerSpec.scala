package de.htwg.se.colorwoodSort.controller

import de.htwg.se.colorwoodSort.model.*
import de.htwg.se.colorwoodSort.util.Observer
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ControllerSpec extends AnyWordSpec with Matchers {

  class TestObserver extends Observer[ControllerEvent] {
    var events: Vector[ControllerEvent] = Vector.empty

    override def update(value: ControllerEvent): Unit =
      events = events :+ value
  }

  def testState: GameState =
    GameState(
      Vector(
        Pipe(2, List(Color.R)),
        Pipe(2, Nil)
      )
    )

  "A Controller" should {

    "start a new game and notify observers" in {
      val controller = Controller()
      val observer = TestObserver()
      controller.add(observer)

      controller.startGame(4, 2, List("R", "G"))

      controller.workflowState shouldBe PlayingState
      controller.undoHistory shouldBe Nil
      controller.gameState.pipes.size shouldBe 4
      observer.events.last shouldBe ControllerEvent.StateChanged(controller.gameState)
    }

    "process quit input and finish the game" in {
      val controller = Controller()
      val observer = TestObserver()
      controller.add(observer)
      controller.gameState = testState

      controller.processInput("q")

      controller.workflowState shouldBe FinishedState
      observer.events should contain(ControllerEvent.Message("Game quit."))
    }

    "process undo input" in {
      val controller = Controller()
      val oldState = testState

      controller.gameState = oldState
      controller.processInput("1 2")
      controller.gameState should not be oldState

      controller.processInput("u")

      controller.gameState shouldBe oldState
      controller.undoHistory shouldBe Nil
    }

    "process invalid input without changing the gameState" in {
      val controller = Controller()
      val observer = TestObserver()
      controller.add(observer)
      val oldState = testState
      controller.gameState = oldState

      controller.processInput("abc")

      controller.gameState shouldBe oldState
      controller.undoHistory shouldBe Nil
      observer.events should contain(ControllerEvent.Message("Invalid move or input"))
    }

    "process a valid move and store it for undo" in {
      val controller = Controller()
      val oldState = testState
      controller.gameState = oldState

      controller.processInput("1 2")

      controller.gameState should not be oldState
      controller.undoHistory.size shouldBe 1
    }

    "detect when the game is solved after a move" in {
      val controller = Controller()
      val observer = TestObserver()
      controller.add(observer)

      controller.gameState = GameState(
        Vector(
          Pipe(2, List(Color.R)),
          Pipe(2, List(Color.R))
        )
      )

      controller.processInput("1 2")

      controller.workflowState shouldBe FinishedState
      observer.events should contain(ControllerEvent.Message("You solved it!"))
    }

    "send a message when input is processed after the game is finished" in {
      val controller = Controller()
      val observer = TestObserver()
      controller.add(observer)

      controller.workflowState = FinishedState
      controller.processInput("1 2")

      observer.events should contain(ControllerEvent.Message("Game is finished. Please restart."))
    }

    "undo nothing when undoHistory is empty" in {
      val controller = Controller()
      val observer = TestObserver()
      controller.add(observer)
      val oldState = testState
      controller.gameState = oldState

      controller.undo()

      controller.gameState shouldBe oldState
      observer.events should contain(ControllerEvent.Message("Nothing to undo."))
    }
  }
}
