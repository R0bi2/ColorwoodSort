package de.htwg.se.colorwoodSort.view
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.colorwoodSort.model.*

class printyPrintSpec extends AnyWordSpec with Matchers {

  "printPipes" should {

    "return a message if one of the parameters is smaller or equal to 0" in {
      val invalidDimensionsMessage = "\n\nInvalid dimensions for pipes.\n"

      printPipes(-1, 3, 3) should be(invalidDimensionsMessage)
      printPipes(3, -1, 3) should be(invalidDimensionsMessage)
      printPipes(3, 3, -1) should be(invalidDimensionsMessage)
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
