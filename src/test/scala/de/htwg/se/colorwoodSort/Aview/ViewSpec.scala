package de.htwg.se.colorwoodSort.Aview

import de.htwg.se.colorwoodSort.controller.*
import de.htwg.se.colorwoodSort.model.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ViewSpec extends AnyWordSpec with Matchers {

  def testState: GameState =
    GameState(
      Vector(
        Pipe(2, List(Color.R, Color.G)),
        Pipe(2, Nil)
      )
    )

  "A View (TUI)" should {

    "render a GameState as ASCII art" in {
      val controller = new Controller()
      val view = new View(controller)

      val output = view.printGameState(testState)

      output should include("|G|")
      output should include("|R|")
      output should include("+-+")
      output should include(" 1 ")
      output should include(" 2 ")
    }

    "print the game state on a StateChanged event" in {
      val controller = new Controller()
      val view = new View(controller)

      val out = new java.io.ByteArrayOutputStream()
      Console.withOut(out) {
        view.update(ControllerEvent.StateChanged(testState))
      }

      out.toString should include("|R|")
    }

    "print the text on a Message event" in {
      val controller = new Controller()
      val view = new View(controller)

      val out = new java.io.ByteArrayOutputStream()
      Console.withOut(out) {
        view.update(ControllerEvent.Message("Hello TUI"))
      }

      out.toString should include("Hello TUI")
    }

    "start a game and stop the input loop on q" in {
      val controller = new Controller()
      val view = new View(controller)

      val in = new java.io.ByteArrayInputStream("q\n".getBytes)
      val out = new java.io.ByteArrayOutputStream()

      Console.withIn(in) {
        Console.withOut(out) {
          view.startGame(3, 4, List("R", "G", "Y"))
        }
      }

      controller.isFinished shouldBe true
      out.toString should include("Game quit.")
    }

    "keep looping on input until the game is finished" in {
      val controller = new Controller()
      val view = new View(controller)

      // Ein ungueltiger Zug, dann quit -> Schleife laeuft zweimal
      val in = new java.io.ByteArrayInputStream("abc\nq\n".getBytes)
      val out = new java.io.ByteArrayOutputStream()

      Console.withIn(in) {
        Console.withOut(out) {
          view.startGame(3, 4, List("R", "G", "Y"))
        }
      }

      out.toString should include("Invalid move or input")
      out.toString should include("Game quit.")
    }
  }
}
