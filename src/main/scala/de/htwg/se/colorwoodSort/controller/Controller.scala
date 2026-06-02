package de.htwg.se.colorwoodSort.controller

import de.htwg.se.colorwoodSort.model.*
import de.htwg.se.colorwoodSort.util.Observable
import scala.annotation.tailrec

class Controller extends Observable[ControllerEvent] {

  def startGame(pipes: Int, height: Int, colorStrings: List[String], readInput: () => String): Unit =
    val colors = colorStrings.map(parseColor)
    val state = generator(pipes, height, colors)
    gameLoop(state, readInput)

  // tailrec is needed to avoid stack overflow for long games, but it also means we can't use a var to store the state, so we pass it as a parameter instead
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

}
