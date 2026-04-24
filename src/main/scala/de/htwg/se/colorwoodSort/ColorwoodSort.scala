package de.htwg.se.colorwoodSort

/*
 * Gut — das ist der richtige Move. Hier sind deine 10 Kern-Bausteine für Scala (reicht für 80%):
 *
 *val / var → Variablen
 *List(...) → Daten speichern
 *.head / .tail → erstes Element / Rest
 *.last → oberstes Element
 *.size → Länge
 *.take(n) / .drop(n) → vorne nehmen / entfernen
 *.takeRight(n) / .dropRight(n) → hinten nehmen / entfernen
 *.map(f) → jedes Element verändern
 *.filter(f) → Elemente auswählen
 *if (...) ... else ... → Logik
 *
 *Das ist dein „Alphabet“.
 *
 * Merksatz: Alles Komplexe ist nur Kombination dieser 10.
 */

/** ------------------------------------------------------- Datastructure -------------------------------------------------------
  */

enum Color:
  case R, G, Y, B, P

case class ColorBlock(color: Color) // nicht zwingend nötig

//old
//case class Pipe(capacity: Int = 1, content: List[Color] = Nil)

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

/** ------------------------------------------------------ main method to run the game ----------------------------------------------
  */
object colorwoodSort {
  val eol = "\n"
  def main(args: Array[String]): Unit = {
    // print(printPipes(3, 3, 3))
    val state = generator(2, 3, List(Color.R, Color.G, Color.Y))
    gameLoop(state)
  }
}

/** ------------------------------------------------------- Game Loop -------------------------------------------------------
  */

def parseMove(input: String, pipeCount: Int): Option[(Int, Int)] = {
  val parts = input.trim.split("\\s+")
  if (parts.length != 2) None
  else {
    val fromOpt = parts(0).toIntOption
    val toOpt = parts(1).toIntOption
    (fromOpt, toOpt) match {
      case (Some(from1), Some(to1)) =>
        val from = from1 - 1
        val to = to1 - 1
        if (from >= 0 && from < pipeCount && to >= 0 && to < pipeCount && from != to) Some((from, to))
        else None
      case _ => None
    }
  }
}

// r for redo action, q for quit
def gameLoop(state: GameState): Unit = {
  println(printGameState(state)) // print current state

  if (isSolved(state)) {
    println(" You solved it!\n")
    return
  }

  println("Enter move (from to), or q to quit:")

  val input = scala.io.StdIn.readLine()

  if (input == null || input.trim.equalsIgnoreCase("q")) { // quit game
    return
  }

  parseMove(input, state.pipes.size) match {
    case Some((from, to)) =>
      val newState = move(state, from, to)

      if (newState == state) {
        println("Invalid move")
        gameLoop(state)
      } else {
        gameLoop(newState)
      }

    case None =>
      println("Invalid input")
      gameLoop(state)
  }
}

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

/** ------------------------------------------------------- Generator -------------------------------------------------------
  */

// printPipe and printPipes is old stuff, not used anymore
def printPipe(height: Int = 2, width: Int = 1): String =

  val wall = "|" + " " * width + "|\n"
  val body = wall * height
  val bottom = "+" + "-" * width + "+\n"
  body + bottom
def printPipes(pipeCount: Int, height: Int, width: Int, symbol: Char = ' '): String =
  if (pipeCount <= 0 || height <= 0 || width <= 0) "\n\nInvalid dimensions for pipes.\n"
  else {
    val filling =
      if (symbol == ' ') " " * width
      else {
        val left = (width - 1) / 2
        val right = width - 1 - left
        " " * left + symbol + " " * right
      }

    val singleWall = "|" + filling + "|"
    val singleBottom = "+" + "-" * width + "+"

    // vars are not nice
    var wallLine = ""
    var bottomLine = ""
    var i = 0
    while (i < pipeCount) {
      wallLine += singleWall
      bottomLine += singleBottom
      if (i < pipeCount - 1) { wallLine += "  "; bottomLine += "  " }
      i += 1
    }

    "\n\n" + (wallLine + "\n") * height + bottomLine + "\n"
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

def allShuffleMoves(state: GameState): List[(Int, Int)] =
  (for {
    from <- state.pipes.indices
    to <- state.pipes.indices
    if from != to
    fromPipe = state.pipes(from)
    toPipe = state.pipes(to)
    if fromPipe.content.nonEmpty
    if toPipe.content.size < toPipe.capacity
  } yield (from, to)).toList

def shuffleMove(state: GameState, from: Int, to: Int): GameState = {
  val fromPipe = state.pipes(from)
  val toPipe = state.pipes(to)

  val block = fromPipe.content.last
  val newFromPipe = Pipe(fromPipe.capacity, fromPipe.content.dropRight(1))
  val newToPipe = Pipe(toPipe.capacity, toPipe.content :+ block)

  GameState(state.pipes.updated(from, newFromPipe).updated(to, newToPipe))
}

def countMixedPipes(state: GameState): Int =
  state.pipes.count(p => p.content.nonEmpty && p.content.distinct.size > 1)

// This is a thing I tried
// Score function for generator: more mixed pipes is better, but having more than 2 empty pipes is very bad (because we need exactly 2 empty pipes to solve the game)
//def score(state: GameState): Int =
//  countMixedPipes(state) * 10 - math.abs(state.countEmptyPipes - 2) * 100

def generator(pipeCount: Int, pipeheight: Int, colors: List[Color], count: Int = 30): GameState = {
  var currentState =
    GameState(
      colors.map(c => Pipe(pipeheight, List.fill(pipeheight)(c))).toVector ++
        Vector.fill(2)(Pipe(pipeheight, Nil))
    )

  var stepsLeft = count
  var lastMove: Option[(Int, Int)] = None

  while (stepsLeft > 0) {
    val moves = allShuffleMoves(currentState)

    val noRedoMoves = moves.filter { case (from, to) =>
      lastMove.forall { case (a, b) => !(from == b && to == a) }
    }

    val usableMoves =
      if (noRedoMoves.nonEmpty) noRedoMoves
      else moves

    if (usableMoves.nonEmpty) {
      val candidates = usableMoves.map { case (from, to) =>
        val next = shuffleMove(currentState, from, to)
        ((from, to), next)
      }

      val currentScore = countMixedPipes(currentState)

      val betterOrEqual = candidates.filter { (_, s) =>
        countMixedPipes(s) >= currentScore
      }

      val pool =
        if (betterOrEqual.nonEmpty) betterOrEqual
        else candidates

      val chosen = pool(scala.util.Random.nextInt(pool.size))
      currentState = chosen._2
      lastMove = Some(chosen._1)
    }

    stepsLeft -= 1
  }

  forceEmptyTwoPipes(currentState)
}

def forceEmptyTwoPipes(state: GameState): GameState = {
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
