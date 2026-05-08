package de.htwg.se.colorwoodSort.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.colorwoodSort.model.*

class ParserSpec extends AnyWordSpec with Matchers {

  "parseMove" should {
    "parse valid 1-based indices" in {
      parseMove("1 2", 3) should be(Some((0, 1)))
    }
    "reject invalid input" in {
      parseMove("4 5", 4) should be(None)
      parseMove("1  5", 4) should be(None)
      parseMove("a b", 4) should be(None)
      parseMove("1", 4) should be(None)
    }
  }

}
