package de.htwg.se.colorwoodSort.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class ParseMoveSpec extends AnyWordSpec with Matchers {

  "parseMove" should {

    "parse a valid move" in {
      parseMove("1 2", 4) shouldBe Some((0, 1))
    }

    "trim whitespace correctly" in {
      parseMove("   2   3   ", 4) shouldBe Some((1, 2))
    }

    "return None for same pipe" in {
      parseMove("1 1", 4) shouldBe None
    }

    "return None for numbers outside range" in {
      parseMove("0 2", 4) shouldBe None
      parseMove("1 5", 4) shouldBe None
    }

    "return None for non numeric input" in {
      parseMove("a b", 4) shouldBe None
    }

    "return None for incomplete input" in {
      parseMove("1", 4) shouldBe None
    }

    "return None for too many arguments" in {
      parseMove("1 2 3", 4) shouldBe None
    }

    "return None for empty input" in {
      parseMove("", 4) shouldBe None
    }
  }
}