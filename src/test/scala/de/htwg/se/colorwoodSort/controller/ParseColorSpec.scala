package de.htwg.se.colorwoodSort.controller

import de.htwg.se.colorwoodSort.model.Color
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ParseColorSpec extends AnyWordSpec with Matchers {

  "parseColor" should {

    "parse all known color strings" in {
      parseColor("R") shouldBe Color.R
      parseColor("G") shouldBe Color.G
      parseColor("Y") shouldBe Color.Y
      parseColor("B") shouldBe Color.B
      parseColor("P") shouldBe Color.P
    }

    "throw an IllegalArgumentException for an unknown color" in {
      an[IllegalArgumentException] should be thrownBy parseColor("X")
    }
  }
}
