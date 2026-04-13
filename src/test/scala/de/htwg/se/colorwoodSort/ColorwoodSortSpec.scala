package de.htwg.se.colorwoodSort

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class ColorwoodSortSpec extends AnyWordSpec with Matchers:
  "ColorwoodSort should have a Pipe as |  | + \\n" in {
    printPipe() should be("+   +\n" + ("|   |" + "\n") * 3 + "+---+\n")
  }

  "case class pipe" should {
    "be created with valid parameters" in {
      val p1 = Pipe(3, List(Color.R, Color.G, Color.Y))
      p1.capacity should be(3)
      p1.content should be(List(Color.R, Color.G, Color.Y))
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
    "return true if topmost element reaches capacity" in {
      val p1 = Pipe(2, List(Color.G, Color.Y))
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

  "move" should {
    "move blocks from one pipe to another if valid" in {
      val p1 = Pipe(3, List(Color.G, Color.G))
      val p2 = Pipe(3, Nil)

      val gamestate = GameState(Vector(p1, p2))

      move(gamestate, 0, 1) should be(
        GameState(Vector(Pipe(3, Nil), Pipe(3, List(Color.G, Color.G))))
      )

      "move" should {
        "not move blocks if invalid" in {
          val p1 = Pipe(3, List(Color.G, Color.G))
          val p2 = Pipe(3, List(Color.R))

          val gamestate = GameState(Vector(p1, p2))

          move(gamestate, 0, 1) should be(
            GameState(Vector(Pipe(3, List(Color.G, Color.G)), Pipe(3, List(Color.R))))
          )
        }
      }

    }

    "printPipes" should {

      "return a message if one of the parameters is smaller or equal to 0" in {
        printPipes(-1, 3, 3) should be("\n\nInvalid dimensions for pipes.\n")
        printPipes(3, -1, 3) should be("\n\nInvalid dimensions for pipes.\n")
        printPipes(3, 3, -1) should be("\n\nInvalid dimensions for pipes.\n")
      }
      "return a string with the given dimensions" in {
        printPipes(2, 3, 4) should be(
          "\n\n" +
            "|    |  |    |\n" +
            "|    |  |    |\n" +
            "|    |  |    |\n" +
            "+----+  +----+\n"
        )
      }
      "print a given symbol in the middle (the placed ColorBlock)" in {
        printPipes(1, 1, 1, 'X') should be(
          "\n\n" +
            "|X|\n" +
            "+-+\n"
        )
        printPipes(1, 3, 3, 'X') should be(
          "\n\n" +
            "| X |\n" +
            "| X |\n" +
            "| X |\n" +
            "+---+\n"
        )
      }
    }
  }
