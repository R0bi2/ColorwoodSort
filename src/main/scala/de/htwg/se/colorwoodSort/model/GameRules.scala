package de.htwg.se.colorwoodSort.model

/** ------------------------------------------------------- Game Rules -------------------------------------------------------
  */

//handle over the game state or blocks does not care about empty or full pipes so better check the pipes
//valid: colors match || Pipe empty, unvalid: colors dont match || pipe is full

def isValid(fromPipe: Pipe, toPipe: Pipe): Boolean = {

  if (fromPipe.content.isEmpty) false
  else if (toPipe.content.isEmpty) true
  else if (isFull(toPipe)) false
  else if (topColor(fromPipe) != topColor(toPipe)) false
  else if (topColor(fromPipe) == topColor(toPipe)) true
  else false

}

/** change Gamestate by moving Blocks from pipe 'fromPipe' to pipe 'toPipe' if there are more Blocks of the same color beneath the selected
  * then they are selected as well
  *
  * How do I adress pipes? I dont have any Gamestate structure yet
  */
def move(state: GameState, from: Int, to: Int): GameState = {
  val fromPipe = state.pipes(from)
  val toPipe = state.pipes(to)

  if (isValid(fromPipe, toPipe)) {

    val color = topColor(fromPipe).get // safe because of isValid check

    // select blocks to move and determine how many can be moved
    val selected = fromPipe.content.reverse.takeWhile(_ == color)
    val same = selected.size
    val space = toPipe.capacity - toPipe.content.size
    val amount = math.min(same, space)

    // update pipes
    val tmp = selected.take(amount)
    val fromPipe2 = Pipe(fromPipe.capacity, fromPipe.content.dropRight(amount))
    val toPipe2 = Pipe(toPipe.capacity, toPipe.content ++ tmp.reverse) // why tmp.reverse

    // new GameState
    val newPipes = state.pipes.updated(from, fromPipe2).updated(to, toPipe2)
    GameState(newPipes)

  } else state
}

// Checks every move if all pipes contains blocks of same color
def isSolved(state: GameState): Boolean =
  state.pipes.exists(_.content.nonEmpty) &&
    state.pipes.forall(pipe => pipe.content.isEmpty || (pipe.content.distinct.size == 1 && pipe.content.size == pipe.capacity))
