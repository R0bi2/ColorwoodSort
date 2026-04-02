package de.htwg.se.colorwoodSort

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class ColorwoodSortSpec extends AnyWordSpec with Matchers:
  "ColorwoodSort should have a Pipe as |  | + \\n" in {
    printPipe() should be("+   +\n" + ("|   |" + "\n") * 3 + "+---+\n")
  }

  "isFull should return true if topmost element reaches capacity" in {
    val p1 = Pipe(2, List(Color.G, Color.Y))
    isFull(p1) should be(true)
  }

  "isValid should return false if colors do not match" in {
    val p1 = Pipe(2, List(Color.G, Color.Y))
    val p2 = Pipe(2, List(Color.G, Color.G))

    isValid(p1, p2) should be(false)
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
