package de.htwg.se.colorwoodSort.controller

import de.htwg.se.colorwoodSort.model.*
import de.htwg.se.colorwoodSort.util.Observable
import scala.annotation.tailrec

object Controller extends Observable[ControllerEvent] {

  def startGame(pipes: Int, height: Int, colorStrings: List[String], readInput: () => String): Unit =
    val colors = colorStrings.map(parseColor)
    val state = generator(pipes, height, colors)
    gameLoop(state, readInput)

  @tailrec
  private def gameLoop(state: GameState, readInput: () => String): Unit =
    notifyObservers(ControllerEvent.StateChanged(state)) // print current state

    if isSolved(state) then notifyObservers(ControllerEvent.Message("You solved it!"))
    else
      val input = readInput()

      if input == null || input.trim.equalsIgnoreCase("q") then notifyObservers(ControllerEvent.Message("Game quit."))
      else
        val newState = handleInput(input, state)
        gameLoop(newState, readInput)

  private def handleInput(input: String, state: GameState): GameState =
    parseMove(input, state.pipes.size) match
      case Some((from, to)) =>
        val newState = move(state, from, to)

        if newState == state then
          notifyObservers(ControllerEvent.Message("Invalid move"))
          state
        else newState

      case None =>
        notifyObservers(ControllerEvent.Message("Invalid input"))
        state

  private def parseColor(s: String): Color = s match
    case "R"   => Color.R
    case "G"   => Color.G
    case "Y"   => Color.Y
    case "B"   => Color.B
    case "P"   => Color.P
    case other => throw new IllegalArgumentException(s"Unknown color: $other")
}
