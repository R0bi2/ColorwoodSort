package de.htwg.se.colorwoodSort
import de.htwg.se.colorwoodSort.model.*
import de.htwg.se.colorwoodSort.controller.*
import de.htwg.se.colorwoodSort.Aview.*

/** ------------------------------------------------------ main method to run the game ----------------------------------------------
  */

// main
object colorwoodSort {
  def main(args: Array[String]): Unit =
    val controller = new Controller()
    val tui = new View(controller)
    tui.startGame(3, 4, List("R", "G", "Y"))
}

/** --------------------------------------------------------------------------------------------------------------
  */
