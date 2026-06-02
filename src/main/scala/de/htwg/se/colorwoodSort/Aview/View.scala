package de.htwg.se.colorwoodSort.Aview

import de.htwg.se.colorwoodSort.controller.*
import de.htwg.se.colorwoodSort.model.* //not perrfect but we need GameState and printGameState
import de.htwg.se.colorwoodSort.util.Observer

class View(controller: Controller) extends Observer[ControllerEvent] {

  controller.add(this)

  // def startGame(pipes: Int, height: Int, colorStrings: List[String]): Unit =
  def startGame(pipes: Int, height: Int, colorStrings: List[String]): Unit =
    controller.startGame(pipes, height, colorStrings, readInput)

  // is def run() and def update not redundant?
  // def run(): Unit =
  //  startGame(3, 4, List("R", "G", "Y"))

  override def update(output: ControllerEvent): Unit = output match {
    case ControllerEvent.StateChanged(state) => println(printGameState(state))
    case ControllerEvent.Message(text)       => println(text)
  }

  def readInput(): String =
    scala.io.StdIn.readLine("Enter move (from to), or q to quit: ")

  def printGameState(state: GameState): String = {
    val height = state.pipeHeight

    val lines =
      for (level <- (height - 1) to 0 by -1) yield {
        state.pipes
          .map { pipe =>
            val content =
              if (level < pipe.content.size)
                pipe.content(level).toString
              else " "

            s"|$content|"
          }
          .mkString("  ")
      }

    val bottom =
      state.pipes.map(_ => "+-+").mkString("  ")

    val indices =
      state.pipes.indices
        .map(i => s" ${i + 1} ")
        .mkString("  ")

    "\n\n" + lines.mkString("\n") + "\n" + bottom + "\n" + indices + "\n"
  }
}
