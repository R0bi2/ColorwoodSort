package de.htwg.se.colorwoodSort

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

//coverage report: D:\SE\colorwoodsort-se_SS_2026\target\scala-3.8.2\scoverage-report
//sbt clean coverage test coverageReport

class ColorwoodSortSpec extends AnyWordSpec with Matchers:

  "ColorwoodSort should have a Pipe as current default printPipe output" in {
    printPipe() should be(
      "| |\n" +
        "| |\n" +
        "+-+\n"
    )
  }

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

  "allValidMoves" should {
    "return a tuple list of all valid moves for a given gamestate" in {
      val p1 = Pipe(2, List(Color.G, Color.G))
      val p2 = Pipe(2, List(Color.R))
      val p3 = Pipe(2, Nil)

      val gamestate = GameState(Vector(p1, p2, p3))

      allValidMoves(gamestate) should be(
        List((0, 2), (1, 2))
      )
    }
  }

  "generator" should {
    "generate given colors" in {

      val state: GameState = generator(1, 1, List(Color.R))

      state.allColors should be(
        List(Color.R)
      )
    }

    "generate given height" in {

      val state = generator(2, 3, List(Color.R, Color.G))
      state.pipeHeight should be(
        3
      )
    }

    "preserve the number of blocks for each color" in {
      val colors = List(Color.R, Color.G)
      val height = 3

      val initial =
        GameState(
          colors.map(c => Pipe(height, List.fill(height)(c))).toVector ++
            Vector.fill(2)(Pipe(height, Nil))
        )

      val generated = generator(2, height, colors, 30)

      colors.foreach { c =>
        countColorBlocks(generated, c) should be(countColorBlocks(initial, c))
      }
    }

    "generate at least two empty pipes" in {
      val state = generator(2, 3, List(Color.R, Color.G))
      countEmptyPipes(state) should be(
        2
      )
    }

    "generate at least one mixed pipe" in {
      val state = generator(2, 3, List(Color.R, Color.G), 30)
      state.pipes.count(p => p.content.nonEmpty && p.content.distinct.size > 1) should be > 0
    }
  }

  "allShuffleMoves" should {

    "return all moves where from != to, fromPipe is not empty, and toPipe is not full" in {
      val p1 = Pipe(2, List(Color.G, Color.G))
      val p2 = Pipe(2, List(Color.R))
      val p3 = Pipe(2, Nil)

      val gamestate = GameState(Vector(p1, p2, p3))

      allShuffleMoves(gamestate) should be(
        List((0, 1), (0, 2), (1, 2), (2, 1))
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
      val p2 = Pipe(2, List(Color.R, Color.R))

      val gamestate = GameState(Vector(p1, p2))

      allShuffleMoves(gamestate) should be(
        List()
      )
    }

    "not include moves where from == to" in {
      val p1 = Pipe(2, List(Color.G))
      val p2 = Pipe(2, Nil)

      val gamestate = GameState(Vector(p1, p2))

      allShuffleMoves(gamestate) should be(
        List((0, 1), (1, 0))
      )
    }
  }

  "shuffleMove" should {
    "move one block from one pipe to another no matter what color" in {
      val p1 = Pipe(2, List(Color.G, Color.G))
      val p2 = Pipe(2, List(Color.R))

      val gamestate = GameState(Vector(p1, p2))

      val newState = shuffleMove(gamestate, 0, 1)

      newState should be(
        GameState(Vector(Pipe(2, List(Color.G)), Pipe(2, List(Color.R, Color.G))))
      )
    }

    "return no change to state if fromPipe is empty" in {
      val p1 = Pipe(2, Nil)
      val p2 = Pipe(2, List(Color.R))

      val gamestate = GameState(Vector(p1, p2))

      shuffleMove(gamestate, 0, 1) should be(
        GameState(Vector(Pipe(2, Nil), Pipe(2, List(Color.R))))
      )
    }

    "return no change to state if toPipe is full" in {
      val p1 = Pipe(2, List(Color.G, Color.G))
      val p2 = Pipe(2, List(Color.R, Color.R))

      val gamestate = GameState(Vector(p1, p2))

      shuffleMove(gamestate, 0, 1) should be(
        GameState(Vector(Pipe(2, List(Color.G, Color.G)), Pipe(2, List(Color.R, Color.R))))
      )
    }

    "return no change if from == to" in {
      val p1 = Pipe(2, List(Color.G))
      val p2 = Pipe(2, List(Color.R))

      val gamestate = GameState(Vector(p1, p2))

      shuffleMove(gamestate, 0, 0) should be(gamestate)
    }

  }

  "forceEmptyTwoPipes" should {

    "return a state with exactly two empty pipes" in {
      val state = GameState(
        Vector(
          Pipe(3, List(Color.R, Color.G)),
          Pipe(3, List(Color.G)),
          Pipe(3, List(Color.R)),
          Pipe(3, List(Color.G))
        )
      )

      countEmptyPipes(forceEmptyTwoPipes(state)) should be(2)
    }

    "preserve the number of blocks for each color" in {
      val state = GameState(
        Vector(
          Pipe(3, List(Color.R, Color.G)),
          Pipe(3, List(Color.G)),
          Pipe(3, List(Color.R)),
          Pipe(3, List(Color.G))
        )
      )

      val newState = forceEmptyTwoPipes(state)

      countColorBlocks(newState, Color.R) should be(countColorBlocks(state, Color.R))
      countColorBlocks(newState, Color.G) should be(countColorBlocks(state, Color.G))
    }

    "choose the two least filled pipes to empty" in {
      val state = GameState(
        Vector(
          Pipe(3, List(Color.R, Color.R, Color.R)),
          Pipe(3, List(Color.G)),
          Pipe(3, List(Color.R)),
          Pipe(3, List(Color.G, Color.G))
        )
      )

      val newState = forceEmptyTwoPipes(state)

      newState.pipes(1).content should be(Nil)
      newState.pipes(2).content should be(Nil)
    }

    "not change the number of pipes" in {
      val state = GameState(
        Vector(
          Pipe(3, List(Color.R, Color.G)),
          Pipe(3, List(Color.G)),
          Pipe(3, List(Color.R)),
          Pipe(3, List(Color.G))
        )
      )

      forceEmptyTwoPipes(state).pipes.size should be(state.pipes.size)
    }
  }
