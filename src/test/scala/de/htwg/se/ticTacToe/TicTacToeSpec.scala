import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class TicTacToeSpec extends AnyWordSpec:
  "TicTacToe" should {
    "have a bar as a String in Form of '+---+---+---+' " in {
      bar() should be("+---+---+---+" + "\n")
    }

    "have a minimum Cell with of '+---+'" in {
      bar(1, 1) should be("+-+" + "\n")
    }
  }
