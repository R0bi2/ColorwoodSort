package de.htwg.se.colorwoodSort.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

/** Testet die Interface-Fassade der Regel-Komponente (Task 10). */
class GameRulesInterfaceSpec extends AnyWordSpec with Matchers {

  "The GameRules component (via GameRulesInterface)" should {

    val rules: GameRulesInterface = GameRules

    "validate moves" in {
      rules.isValid(Pipe(2, List(Color.R)), Pipe(2, Nil)) shouldBe true
      rules.isValid(Pipe(2, Nil), Pipe(2, List(Color.R))) shouldBe false
    }

    "execute moves" in {
      val state = GameState(Vector(Pipe(2, List(Color.R)), Pipe(2, Nil)))
      val result = rules.move(state, 0, 1)

      result.pipes(0).content shouldBe Nil
      result.pipes(1).content shouldBe List(Color.R)
    }

    "detect a solved game" in {
      val solved = GameState(Vector(Pipe(1, List(Color.R)), Pipe(1, Nil)))
      val unsolved = GameState(Vector(Pipe(2, List(Color.R, Color.G)), Pipe(2, Nil)))

      rules.isSolved(solved) shouldBe true
      rules.isSolved(unsolved) shouldBe false
    }
  }
}
