package de.htwg.se.colorwoodSort.Aview

import de.htwg.se.colorwoodSort.controller.*
import de.htwg.se.colorwoodSort.model.*
import de.htwg.se.colorwoodSort.util.Observer
import scala.annotation.tailrec

class View(controller: Controller) extends Observer[ControllerEvent] {

  controller.add(this)

  def startGame(pipes: Int, height: Int, colorStrings: List[String]): Unit = {
    // 1. Spiel im Controller starten (ohne readInput Parameter!)
    controller.startGame(pipes, height, colorStrings)

    // 2. Die Eingabeschleife der TUI anwerfen
    inputLoop()
  }

  // Die TUI übernimmt jetzt die Kontrolle über die Eingabeschleife
  @tailrec
  private def inputLoop(): Unit = {
    // Wenn der Controller meldet, dass das Spiel vorbei ist, brechen wir die Schleife ab
    if (controller.workflowState == FinishedState) {
      return
    }

    // Eingabe lesen und blind an den Controller schicken
    val input = scala.io.StdIn.readLine("Enter move (from to), u for undo, or q to quit: ")
    controller.processInput(input)

    // Nächster Schleifendurchlauf
    inputLoop()
  }

  override def update(output: ControllerEvent): Unit = output match {
    case ControllerEvent.StateChanged(state) => println(printGameState(state))
    case ControllerEvent.Message(text)       => println(text)
  }

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
