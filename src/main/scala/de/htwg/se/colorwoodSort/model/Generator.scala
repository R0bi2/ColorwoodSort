package de.htwg.se.colorwoodSort.model

/** ------------------------------------------------------- Generator (intern) -------------------------------------------------------
  *
  * Erzeugt durchmischte Startaufstellungen fuer das Puzzle.
  * Zugriff von aussen nur ueber [[GeneratorStrategy]] (Strategy Pattern).
  */

/** Erzeugt einen gemischten [[GameState]] durch wiederholtes Shuffeln.
  *
  * @param pipeCount  Anzahl Pipes (Farbpipes + 2 leere)
  * @param pipeheight Kapazitaet jeder Pipe
  * @param colors     verwendete Farben
  * @param count      Anzahl Shuffle-Schritte (mehr = schwerer)
  */
private[model] def generator(pipeCount: Int, pipeheight: Int, colors: List[Color], count: Int = 30): GameState = {
  var currentState =
    GameState(
      colors.map(c => Pipe(pipeheight, List.fill(pipeheight)(c))).toVector ++
        Vector.fill(2)(Pipe(pipeheight, Nil))
    )

  var stepsLeft = count
  var lastMove: Option[(Int, Int)] = None

  while (stepsLeft > 0) {
    val moves = allShuffleMoves(currentState)

    val usableMoves = nonReversingMoves(moves, lastMove)

    if (usableMoves.nonEmpty) {
      val candidates = usableMoves.map { case (from, to) =>
        val next = shuffleMove(currentState, from, to)
        ((from, to), next)
      }

      val pool = preferMixingCandidates(candidates, countMixedPipes(currentState))

      val chosen = pool(scala.util.Random.nextInt(pool.size))
      currentState = chosen._2
      lastMove = Some(chosen._1)
    }

    stepsLeft -= 1
  }

  forceEmptyTwoPipes(currentState)
}

/** Filtert Zuege heraus, die den letzten Shuffle-Zug direkt rueckgaengig machen wuerden.
  * Faellt auf alle Zuege zurueck, wenn sonst nichts uebrig bleibt.
  */
private[model] def nonReversingMoves(
    moves: List[(Int, Int)],
    lastMove: Option[(Int, Int)]
): List[(Int, Int)] = {
  val noRedoMoves = moves.filter { case (from, to) =>
    lastMove.forall { case (a, b) => !(from == b && to == a) }
  }
  if (noRedoMoves.nonEmpty) noRedoMoves
  else moves
}

/** Bevorzugt Kandidaten-Zuege, die die Durchmischung halten oder verbessern.
  * Faellt auf alle Kandidaten zurueck, wenn keiner das Kriterium erfuellt.
  */
private[model] def preferMixingCandidates(
    candidates: List[((Int, Int), GameState)],
    currentScore: Int
): List[((Int, Int), GameState)] = {
  val betterOrEqual = candidates.filter { (_, s) =>
    countMixedPipes(s) >= currentScore
  }
  if (betterOrEqual.nonEmpty) betterOrEqual
  else candidates
}

/** Stellt sicher, dass genau zwei Pipes leer sind (Spielregel) und sortiert leere Pipes ans Ende. */
private[model] def forceEmptyTwoPipes(state: GameState): GameState = {
  val pipes = state.pipes

  // Indizes der zwei kleinsten Pipes finden
  val sorted = pipes.zipWithIndex.sortBy(_._1.content.size)
  val emptyIdx1 = sorted(0)._2
  val emptyIdx2 = sorted(1)._2

  val toEmpty = Set(emptyIdx1, emptyIdx2)

  // Alle Blöcke aus diesen Pipes einsammeln
  val blocksToMove = toEmpty.toList.flatMap(i => pipes(i).content)

  // Neue Pipes vorbereiten: die zwei werden leer
  var newPipes = pipes.zipWithIndex.map { case (p, i) =>
    if (toEmpty.contains(i)) Pipe(p.capacity, Nil)
    else p
  }.toVector

  // Blöcke auf andere Pipes verteilen
  for (block <- blocksToMove) {
    // finde erste Pipe mit Platz (die nicht geleert wird)
    val targetIndex = newPipes.indices.find { i =>
      !toEmpty.contains(i) &&
      newPipes(i).content.size < newPipes(i).capacity
    }

    targetIndex match {
      case Some(i) =>
        val p = newPipes(i)
        newPipes = newPipes.updated(i, Pipe(p.capacity, p.content :+ block))
      case None =>
        // falls kein Platz mehr: einfach nichts tun (sollte selten passieren)
        ()
    }
  }

  // move empty pipes to the end
  val (empty, filled) = newPipes.partition(_.content.isEmpty)
  GameState((filled ++ empty).toVector)
}

// This is a thing I tried
// Score function for generator: more mixed pipes is better, but having more than 2 empty pipes is very bad (because we need exactly 2 empty pipes to solve the game)
//def score(state: GameState): Int =
//  countMixedPipes(state) * 10 - math.abs(state.countEmptyPipes - 2) * 100

/** Fuehrt einen einzelnen Shuffle-Zug aus (verschiebt den obersten Block). */
private[model] def shuffleMove(state: GameState, from: Int, to: Int): GameState = {
  val fromPipe = state.pipes(from)
  val toPipe = state.pipes(to)

  val block = fromPipe.content.last
  val newFromPipe = Pipe(fromPipe.capacity, fromPipe.content.dropRight(1))
  val newToPipe = Pipe(toPipe.capacity, toPipe.content :+ block)

  GameState(state.pipes.updated(from, newFromPipe).updated(to, newToPipe))
}
