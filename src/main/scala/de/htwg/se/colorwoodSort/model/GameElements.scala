package de.htwg.se.colorwoodSort.model

/** ------------------------------------------------------- Datenstrukturen -------------------------------------------------------
  *
  * Das Model-Vokabular des Spiels: Farben, Pipes und der Gesamt-Spielzustand.
  * Alle Strukturen sind unveraenderlich (immutable) — jeder Zug erzeugt einen neuen
  * [[GameState]], was Undo/Redo und testbare Regeln vereinfacht.
  */

/** Die fuenf im Spiel verwendbaren Block-Farben. */
enum Color:
  case R, G, Y, B, P

/** Eine Pipe (Rohr) mit fester Kapazitaet und einer Liste von Bloecken.
  *
  * Die Liste `content` ist von unten nach oben sortiert:
  * `content.head` = unterster Block, `content.last` = oberster Block.
  *
  * @param capacity maximale Anzahl Bloecke in dieser Pipe
  * @param content  aktuelle Bloecke (leer = Pipe ist leer)
  */
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

/** Der komplette Spielzustand: alle Pipes auf einmal.
  *
  * @param pipes Vektor aller Pipes; die Indizes entsprechen den Pipe-Nummern im Controller (0-basiert).
  */
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
