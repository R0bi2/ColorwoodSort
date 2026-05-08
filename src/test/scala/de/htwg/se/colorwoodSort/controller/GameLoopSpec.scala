package test.scala.de.htwg.se.colorwoodSort.controller
/*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.colorwoodSort.model.*

class GameLoopSpec extends AnyWordSpec with Matchers {

  "gameLoop" should {

    "print solved message when state is already solved" in {
      val state = GameState(
        Vector(
          Pipe(2, List(Color.R, Color.R)),
          Pipe(2, List(Color.G, Color.G))
        )
      )

      val output = new java.io.ByteArrayOutputStream()
      Console.withOut(output) { gameLoop(state) }

      output.toString should include(" You solved it!\n")
    }

    "stop when user enters q" in {
      val state = GameState(
        Vector(
          Pipe(2, List(Color.R, Color.G)),
          Pipe(2, Nil)
        )
      )

      val input = new java.io.ByteArrayInputStream("q\n".getBytes)
      noException should be thrownBy {
        Console.withIn(input) { gameLoop(state) }
      }
    }

    "print invalid input for non-numeric command" in {
      val state = GameState(
        Vector(
          Pipe(2, List(Color.R, Color.G)),
          Pipe(2, Nil)
        )
      )

      val input = new java.io.ByteArrayInputStream("abc\nq\n".getBytes)
      val output = new java.io.ByteArrayOutputStream()

      Console.withIn(input) {
        Console.withOut(output) { gameLoop(state) }
      }

      output.toString should include("Invalid input")
    }

    "print invalid input for out-of-range indices" in {
      val state = GameState(
        Vector(
          Pipe(2, List(Color.R, Color.G)),
          Pipe(2, Nil)
        )
      )

      val input = new java.io.ByteArrayInputStream("4 5\nq\n".getBytes)
      val output = new java.io.ByteArrayOutputStream()

      Console.withIn(input) {
        Console.withOut(output) { gameLoop(state) }
      }

      output.toString should include("Invalid input")
    }

    "print invalid move for a syntactically valid but rule-invalid move" in {
      val state = GameState(
        Vector(
          Pipe(2, List(Color.R)),
          Pipe(2, List(Color.G))
        )
      )

      val input = new java.io.ByteArrayInputStream("1 2\nq\n".getBytes)
      val output = new java.io.ByteArrayOutputStream()

      Console.withIn(input) {
        Console.withOut(output) { gameLoop(state) }
      }

      output.toString should include("Invalid move")
    }

    "continue with new state after a valid move" in {
      val state = GameState(
        Vector(
          Pipe(2, List(Color.R)),
          Pipe(2, Nil)
        )
      )

      val input = new java.io.ByteArrayInputStream("1 2\nq\n".getBytes)
      val output = new java.io.ByteArrayOutputStream()

      Console.withIn(input) {
        Console.withOut(output) { gameLoop(state) }
      }

      val text = output.toString
      text should include("| |  |R|")
    }
  }

}
 */
