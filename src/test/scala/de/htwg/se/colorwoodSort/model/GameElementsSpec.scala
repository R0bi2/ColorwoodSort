package de.htwg.se.colorwoodSort.test

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.colorwoodSort.model.*

class GameElementsSpec extends AnyWordSpec with Matchers:

  "case class pipe" should {
    "be created with valid parameters" in {
      val p1 = Pipe(3, List(Color.R, Color.G, Color.Y))
      p1.capacity should be(3)
      p1.content should be(List(Color.R, Color.G, Color.Y))
    }

    "be correctly created with default parameters" in {
      val p2 = Pipe()
      p2.capacity should be(1)
      p2.content should be(Nil)
    }

    "throw exception if capacity is 0" in {
      assertThrows[IllegalArgumentException] {
        Pipe(0, Nil)
      }
    }

    "throw exception if content exceeds capacity" in {
      assertThrows[IllegalArgumentException] {
        Pipe(3, List(Color.R, Color.G, Color.Y, Color.B))
      }
    }
  }

  "isFull" should {

    val p1 = Pipe(2, List(Color.G, Color.Y))
    val p2 = Pipe(3, List(Color.G, Color.Y))
    val p3 = Pipe(3, Nil)

    "return true if topmost element reaches capacity" in {
      isFull(p1) should be(true)
    }

    "return false if topmost element does not reach capacity" in {
      val p2 = Pipe(3, List(Color.G, Color.Y))
      isFull(p2) should be(false)
    }

    "return false if pipe is empty" in {
      val p3 = Pipe(3, Nil)
      isFull(p3) should be(false)
    }
  }

  "topColor should" should {
    "return the topmost(last) color of the List as Option[Color]" in {
      val p1 = Pipe(1, List(Color.Y))
      topColor(p1) should be(Some(Color.Y))
    }

    "return None if List is empty" in {
      val p2 = Pipe(1, Nil)
      topColor(p2) should be(None)
    }
  }

  "case Class gameState" should {
    "be created with valid parameters" in {
      val p1 = Pipe(2, List(Color.R, Color.G))
      val p2 = Pipe(2, List(Color.Y, Color.B))

      val gamestate = GameState(Vector(p1, p2))

      gamestate.pipes should be(Vector(p1, p2))
    }
  }

  "allShuffleMoves" should {

    "return all valid shuffle moves" in {
      val p1 = Pipe(2, List(Color.G, Color.R)) // mixed, full -> not allowed as source
      val p2 = Pipe(2, List(Color.R)) // monocolor, not full -> allowed
      val p3 = Pipe(2, Nil) // empty target

      val gamestate = GameState(Vector(p1, p2, p3))

      allShuffleMoves(gamestate) should be(
        List((0, 1), (0, 2), (1, 2))
      )
    }

    "not include moves where from == to" in {
      val p1 = Pipe(2, List(Color.G))
      val p2 = Pipe(2, Nil)

      val gamestate = GameState(Vector(p1, p2))

      allShuffleMoves(gamestate) should be(
        List((0, 1))
      )
    }

    "not include moves from an empty pipe" in {
      val p1 = Pipe(2, Nil)
      val p2 = Pipe(2, List(Color.R))

      val gamestate = GameState(Vector(p1, p2))

      allShuffleMoves(gamestate) should be(
        List((1, 0))
      )
    }

    "not include moves to a full pipe" in {
      val p1 = Pipe(2, List(Color.G))
      val p2 = Pipe(2, List(Color.R, Color.R)) // full target

      val gamestate = GameState(Vector(p1, p2))

      allShuffleMoves(gamestate) should be(
        List((1, 0))
      )
    }

    "allow moves from a monocolor pipe if it is not full" in {
      val p1 = Pipe(3, List(Color.G, Color.G)) // monocolor, not full
      val p2 = Pipe(3, Nil)

      val gamestate = GameState(Vector(p1, p2))

      allShuffleMoves(gamestate) should be(
        List((0, 1))
      )
    }

    "allow moves from a mixed pipe if it is not full" in {
      val p1 = Pipe(3, List(Color.G, Color.R)) // mixed, not full -> allowed
      val p2 = Pipe(3, Nil)

      val gamestate = GameState(Vector(p1, p2))

      allShuffleMoves(gamestate) should be(
        List((0, 1))
      )
    }

    "allow moves from a mixed pipe even if it is full" in {
      val p1 = Pipe(2, List(Color.G, Color.R)) // mixed, full -> now allowed
      val p2 = Pipe(2, Nil)

      val gamestate = GameState(Vector(p1, p2))

      allShuffleMoves(gamestate) should be(
        List((0, 1))
      )
    }

    "allow moves from a full monocolor pipe" in {
      val p1 = Pipe(2, List(Color.G, Color.G)) // full, monocolor -> allowed
      val p2 = Pipe(2, Nil)

      val gamestate = GameState(Vector(p1, p2))

      allShuffleMoves(gamestate) should be(
        List((0, 1))
      )
    }
  }
