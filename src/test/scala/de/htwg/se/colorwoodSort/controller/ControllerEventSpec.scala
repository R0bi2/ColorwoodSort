package de.htwg.se.colorwoodSort.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.colorwoodSort.model.*

class ControllerEventSpec extends AnyWordSpec with Matchers {

  "A ControllerEvent" should {

    "create a StateChanged event with a GameState" in {
      val state = GameState(Vector.empty)

      val event = ControllerEvent.StateChanged(state)

      event shouldBe ControllerEvent.StateChanged(state)
    }

    "create a Message event with text" in {
      val event = ControllerEvent.Message("Hello")

      event shouldBe ControllerEvent.Message("Hello")
    }

    "store the correct GameState in StateChanged" in {
      val state = GameState(Vector.empty)

      val event = ControllerEvent.StateChanged(state)

      event match {
        case ControllerEvent.StateChanged(s) =>
          s shouldBe state
        case _ => fail("Expected StateChanged event")
      }
    }

    "store the correct text in Message" in {
      val event = ControllerEvent.Message("Game Over")

      event match {
        case ControllerEvent.Message(text) =>
          text shouldBe "Game Over"
        case _ => fail("Expected Message event")
      }
    }
  }
}
