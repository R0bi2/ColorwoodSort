package de.htwg.se.colorwoodSort.util

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class UndoManagerSpec extends AnyWordSpec with Matchers {

  // Einfacher Test-Command auf Int-Zustaenden: addiert einen Wert
  case class AddCommand(amount: Int, before: Int) extends Command[Int] {
    override def doStep(state: Int): Int = state + amount
    override def undoStep(state: Int): Int = before
    override def redoStep(state: Int): Int = state + amount
  }

  // Command, der den Zustand nicht veraendert (ungueltiger Zug)
  case class NoopCommand() extends Command[Int] {
    override def doStep(state: Int): Int = state
    override def undoStep(state: Int): Int = state
    override def redoStep(state: Int): Int = state
  }

  "An UndoManager" should {

    "execute a command and put it on the undo stack" in {
      val manager = new UndoManager[Int]
      val result = manager.doStep(0, AddCommand(5, 0))

      result shouldBe 5
      manager.canUndo shouldBe true
      manager.canRedo shouldBe false
    }

    "not store a command that does not change the state" in {
      val manager = new UndoManager[Int]
      val result = manager.doStep(7, NoopCommand())

      result shouldBe 7
      manager.canUndo shouldBe false
    }

    "undo a step and move it to the redo stack" in {
      val manager = new UndoManager[Int]
      val state = manager.doStep(0, AddCommand(5, 0))

      manager.undoStep(state) shouldBe Some(0)
      manager.canUndo shouldBe false
      manager.canRedo shouldBe true
    }

    "return None when undoing with an empty undo stack" in {
      val manager = new UndoManager[Int]
      manager.undoStep(3) shouldBe None
    }

    "redo an undone step" in {
      val manager = new UndoManager[Int]
      val state = manager.doStep(0, AddCommand(5, 0))
      val undone = manager.undoStep(state).get

      manager.redoStep(undone) shouldBe Some(5)
      manager.canUndo shouldBe true
      manager.canRedo shouldBe false
    }

    "return None when redoing with an empty redo stack" in {
      val manager = new UndoManager[Int]
      manager.redoStep(3) shouldBe None
    }

    "clear both stacks" in {
      val manager = new UndoManager[Int]
      val state = manager.doStep(0, AddCommand(5, 0))
      manager.undoStep(state)

      manager.clear()

      manager.canUndo shouldBe false
      manager.canRedo shouldBe false
    }
  }
}
