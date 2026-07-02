package de.htwg.se.colorwoodSort.model

/** ------------------------------------------------------- Spielregeln (intern) -------------------------------------------------------
  *
  * Die eigentliche Regel-Logik: Zugpruefung, Ausfuehrung und Gewinnerkennung.
  * Alle Funktionen sind `private[model]` gekapselt — Zugriff von aussen nur ueber
  * [[GameRulesInterface]] / [[GameRules]].
  */

/** Prueft, ob ein Zug von `fromPipe` nach `toPipe` regelkonform ist.
  *
  * Gueltig wenn: Quelle nicht leer, Ziel hat Platz, Ziel leer oder gleiche oberste Farbe.
  */
private[model] def isValid(fromPipe: Pipe, toPipe: Pipe): Boolean =
  (topColor(fromPipe), topColor(toPipe)) match {
    case (None, _)              => false // Quelle leer -> nichts zu ziehen
    case (_, None)              => true  // Ziel leer   -> immer erlaubt
    case (Some(from), Some(to)) => !isFull(toPipe) && from == to // Platz + gleiche Farbe
  }

/** Fuehrt einen Zug aus und liefert den neuen Spielstand.
  *
  * Wenn mehrere Bloecke derselben Farbe oben liegen, werden sie als Gruppe verschoben
  * (begrenzt durch freien Platz in der Ziel-Pipe). Ungueltige Zuege liefern den alten State.
  */
private[model] def move(state: GameState, from: Int, to: Int): GameState = {
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

/** Prueft, ob das Puzzle geloest ist: jede Pipe leer oder einfarbig und voll. */
private[model] def isSolved(state: GameState): Boolean =
  state.pipes.exists(_.content.nonEmpty) &&
    state.pipes.forall(pipe => pipe.content.isEmpty || (pipe.content.distinct.size == 1 && pipe.content.size == pipe.capacity))
