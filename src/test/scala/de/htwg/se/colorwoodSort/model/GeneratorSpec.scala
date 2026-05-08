package de.htwg.se.colorwoodSort.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.colorwoodSort.model.*

class GeneratorSpec extends AnyWordSpec with Matchers {
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
        generated.countColorBlocks(c) should be(initial.countColorBlocks(c))
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

    "fallback to all candidates if no move keeps or improves the mixed-pipe count" in {
      val state = GameState(
        Vector(
          Pipe(3, List(Color.R, Color.G)), // mixed, not full -> allowed source
          Pipe(3, Nil)
        )
      )

      val moves = allShuffleMoves(state)
      val candidates = moves.map { case (from, to) =>
        val next = shuffleMove(state, from, to)
        ((from, to), next)
      }

      val currentScore = countMixedPipes(state)
      val betterOrEqual = candidates.filter { (_, s) =>
        countMixedPipes(s) >= currentScore
      }

      moves should not be empty
      betterOrEqual shouldBe empty
      candidates should not be empty
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

      newState.countColorBlocks(Color.R) should be(state.countColorBlocks(Color.R))
      newState.countColorBlocks(Color.G) should be(state.countColorBlocks(Color.G))
    }

    // Designfrage speziell für forceEmptyTwoPipes:
    // Soll die Funktion vollständig deterministisch sein (gleiche Verteilung der Blöcke),
    // oder reicht es, wenn nur die Kernbedingungen erfüllt sind
    // (genau zwei Pipes leer, keine Blöcke gehen verloren)?
    //
    // Aktuell ist sie teilweise deterministisch:
    // → welche Pipes geleert werden: klar und reproduzierbar
    // → wohin die Blöcke wandern: abhängig von Reihenfolge → nicht strikt festgelegt
    //
    // Für sauberen Programmierstil gilt hier:
    // Wenn die genaue Verteilung später wichtig ist (z. B. für Tests, Debugging),
    // sollte sie deterministisch gemacht werden.
    // Wenn nur die Eigenschaften zählen, ist die jetzige Lösung ausreichend,
    "choose the two least filled pipes to empty and place them to the right" in {
      val state = GameState(
        Vector(
          Pipe(3, List(Color.R, Color.R, Color.R)),
          Pipe(3, List(Color.G)),
          Pipe(3, List(Color.R)),
          Pipe(3, List(Color.G, Color.G))
        )
      )

      val newState = forceEmptyTwoPipes(state)

      newState.pipes(2).content should be(Nil)
      newState.pipes(3).content should be(Nil)
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

  "shuffleMove" should {

    "move one block from one pipe to another no matter what color" in {
      val p1 = Pipe(2, List(Color.G, Color.R))
      val p2 = Pipe(2, Nil)
      val gamestate = GameState(Vector(p1, p2))

      shuffleMove(gamestate, 0, 1) should be(
        GameState(Vector(Pipe(2, List(Color.G)), Pipe(2, List(Color.R))))
      )
    }

    // Ich weiss nicht ob das mit den Exceptions so optimal ist, aber es ist auf jeden Fall eine klare und einfache Lösung, um ungültige Züge zu verhindern.
    //
    //
    "throw NoSuchElementException if fromPipe is empty" in {
      val p1 = Pipe(2, Nil)
      val p2 = Pipe(2, List(Color.R))
      val gamestate = GameState(Vector(p1, p2))

      assertThrows[NoSuchElementException] {
        shuffleMove(gamestate, 0, 1)
      }
    }

    "throw IllegalArgumentException if toPipe is full" in {
      val p1 = Pipe(2, List(Color.G))
      val p2 = Pipe(2, List(Color.R, Color.R))
      val gamestate = GameState(Vector(p1, p2))

      assertThrows[IllegalArgumentException] {
        shuffleMove(gamestate, 0, 1)
      }
    }

    "duplicate the top block if from == to" in {
      val p1 = Pipe(2, List(Color.G))
      val p2 = Pipe(2, List(Color.R))
      val gamestate = GameState(Vector(p1, p2))

      shuffleMove(gamestate, 0, 0) should be(
        GameState(Vector(Pipe(2, List(Color.G, Color.G)), Pipe(2, List(Color.R))))
      )
    }
  }

}
