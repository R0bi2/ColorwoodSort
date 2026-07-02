package de.htwg.se.colorwoodSort.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GeneratorStrategySpec extends AnyWordSpec with Matchers {

  val colors = List(Color.R, Color.G, Color.Y)

  def checkGenerated(state: GameState): Unit = {
    state.pipes.size shouldBe colors.size + 2
    state.pipeHeight shouldBe 4
    state.allColors should contain theSameElementsAs colors
    colors.foreach(c => state.countColorBlocks(c) shouldBe 4)
  }

  "EasyGenerator" should {
    "generate a valid game state" in {
      checkGenerated(EasyGenerator.generate(3, 4, colors))
    }
  }

  "MediumGenerator" should {
    "generate a valid game state" in {
      checkGenerated(MediumGenerator.generate(3, 4, colors))
    }
  }

  "HardGenerator" should {
    "generate a valid game state" in {
      checkGenerated(HardGenerator.generate(3, 4, colors))
    }
  }

  "generator" should {
    "skip shuffling when no shuffle moves are possible (no colors)" in {
      val state = generator(0, 2, Nil, count = 3)

      state.pipes.size shouldBe 2
      state.countEmptyPipes shouldBe 2
    }
  }

  "nonReversingMoves" should {

    "return all moves when there was no last move" in {
      val moves = List((0, 1), (1, 0))
      nonReversingMoves(moves, None) shouldBe moves
    }

    "filter out the move that reverses the last move" in {
      val moves = List((0, 1), (1, 0), (1, 2))
      nonReversingMoves(moves, Some((0, 1))) shouldBe List((0, 1), (1, 2))
    }

    "fall back to all moves if only the reversing move is possible" in {
      val moves = List((1, 0))
      nonReversingMoves(moves, Some((0, 1))) shouldBe List((1, 0))
    }
  }

  "preferMixingCandidates" should {

    val mixed = GameState(Vector(Pipe(2, List(Color.R, Color.G)), Pipe(2, Nil)))
    val sorted = GameState(Vector(Pipe(2, List(Color.R, Color.R)), Pipe(2, Nil)))

    "keep only candidates that hold or improve the mixed-pipe count" in {
      val candidates = List(((0, 1), mixed), ((1, 0), sorted))
      preferMixingCandidates(candidates, 1) shouldBe List(((0, 1), mixed))
    }

    "fall back to all candidates if none holds the mixed-pipe count" in {
      val candidates = List(((1, 0), sorted))
      preferMixingCandidates(candidates, 1) shouldBe candidates
    }
  }
}
