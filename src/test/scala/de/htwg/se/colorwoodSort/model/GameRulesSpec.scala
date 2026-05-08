package de.htwg.se.colorwoodSort.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.colorwoodSort.model.*

class GameRulesSpec extends AnyWordSpec with Matchers {

  "isValid" should {
    "return false if colors do not match" in {
      val p1 = Pipe(2, List(Color.G, Color.Y))
      val p2 = Pipe(2, List(Color.G, Color.G))

      isValid(p1, p2) should be(false)
    }

    "return false if fromPipe is empty" in {
      val p1 = Pipe(2, Nil)
      val p2 = Pipe(2, List(Color.G, Color.G))

      isValid(p1, p2) should be(false)
    }

    "return false if toPipe is full" in {
      val p1 = Pipe(2, List(Color.G, Color.G))
      val p2 = Pipe(2, List(Color.G, Color.G))

      isValid(p1, p2) should be(false)
    }

    "return true if colors match and toPipe is not full" in {
      val p1 = Pipe(2, List(Color.G, Color.G))
      val p2 = Pipe(2, List(Color.G))

      isValid(p1, p2) should be(true)
    }

    "return true if toPipe is empty" in {
      val p1 = Pipe(2, List(Color.G, Color.G))
      val p2 = Pipe(2, Nil)

      isValid(p1, p2) should be(true)
    }

  }

  "move" should {
    "move blocks from one pipe to another if valid" in {
      val p1 = Pipe(3, List(Color.G, Color.G))
      val p2 = Pipe(3, Nil)

      val gamestate = GameState(Vector(p1, p2))

      move(gamestate, 0, 1) should be(
        GameState(Vector(Pipe(3, Nil), Pipe(3, List(Color.G, Color.G))))
      )
    }

    "not move blocks if invalid" in {
      val p1 = Pipe(3, List(Color.G, Color.G))
      val p2 = Pipe(3, List(Color.R))

      val gamestate = GameState(Vector(p1, p2))

      move(gamestate, 0, 1) should be(
        GameState(Vector(Pipe(3, List(Color.G, Color.G)), Pipe(3, List(Color.R))))
      )
    }
  }

  "isSolved" should {

    "return true if no pipe has more than one color" in {
      val p1 = Pipe(2, List(Color.G, Color.G))
      val p2 = Pipe(2, List(Color.R, Color.R))
      val p3 = Pipe(2, Nil)

      val gamestate = GameState(Vector(p1, p2, p3))

      isSolved(gamestate) should be(
        true
      )
    }

    "return false if a at least one pipe has more than one color" in {
      val p1 = Pipe(2, List(Color.G, Color.R))
      val p2 = Pipe(2, List(Color.R))
      val p3 = Pipe(2, List(Color.G))

      val gamestate = GameState(Vector(p1, p2, p3))

      isSolved(gamestate) should be(
        false
      )
    }

    // Do we really want an unsolved gamestate with empty pipes?
    "return false if all pipes are empty" in {
      val p1 = Pipe(1, Nil)
      val p2 = Pipe(1, Nil)

      val gamestate = GameState(Vector(p1, p2))

      isSolved(gamestate) should be(false)
    }
  }
}
