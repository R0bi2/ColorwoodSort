package de.htwg.se.colorwoodSort.model

/** ------------------------------------------------------- Datastructure -------------------------------------------------------
  */

enum Color:
  case R, G, Y, B, P

case class Pipe(capacity: Int = 1, content: List[Color] = Nil) {
  require(capacity > 0, "capacity must be > 0")
  require(content.size <= capacity, "too many elements")
}
//pipe utility methods
extension (pipe: Pipe)
  def isFull: Boolean =
    pipe.content.size == pipe.capacity

  def topColor: Option[Color] = {
    if (pipe.content.isEmpty)
      None
    else
      Some(pipe.content.last)
  }

case class GameState(pipes: Vector[Pipe])
//gameState utility methods for TDD
extension (state: GameState)
  def allColors: List[Color] =
    state.pipes.flatMap(_.content).toList.distinct

  def pipeHeight: Int =
    require(state.pipes.nonEmpty)
    state.pipes.head.capacity

  def countEmptyPipes: Int =
    state.pipes.count(_.content.isEmpty)

  def countColorBlocks(color: Color): Int =
    state.pipes.flatMap(_.content).count(_ == color)

  def countMixedPipes: Int =
    state.pipes.count(p => p.content.nonEmpty && p.content.distinct.size > 1)

  def allShuffleMoves: List[(Int, Int)] =
    (for {
      from <- state.pipes.indices
      to <- state.pipes.indices
      if from != to
      fromPipe = state.pipes(from)
      toPipe = state.pipes(to)
      if fromPipe.content.nonEmpty
      if toPipe.content.size < toPipe.capacity
    } yield (from, to)).toList
