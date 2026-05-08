package de.htwg.se.colorwoodSort.view

import de.htwg.se.colorwoodSort.controller.*
import de.htwg.se.colorwoodSort.model.* //not perrfect but we need GameState and printGameState
import de.htwg.se.colorwoodSort.util.Observer

object View extends Observer[ControllerEvent] {

  def startGame(pipes: Int, height: Int, colorStrings: List[String]): Unit =
    Controller.add(this)
    Controller.startGame(pipes, height, colorStrings, readInput)

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
