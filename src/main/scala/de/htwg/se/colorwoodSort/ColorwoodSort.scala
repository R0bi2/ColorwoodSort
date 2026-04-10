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

enum Color:
  case R, G, Y, B

case class ColorBlock(color: Color) // nicht zwingend nötig
case class Pipe(capacity: Int = 1, content: List[Color] = Nil)
case class GameState(pipes: Vector[Pipe])

object colorwoodSort {

  val eol = "\n"

  def main(args: Array[String]): Unit = {

    print(printPipes(3, 3, 3))
  }
}

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

/** change Gamestate by moving Blocks from pipe from to pipe to there are more Blocks of the same color beneath the selected then they are
  * selected as well
  *
  * How do I adress pipes? I dont have any Gamestate structure yet
  */
def move(state: GameState, from: Int, to: Int): GameState = {
  val fromPipe = state.pipes(from)
  val toPipe = state.pipes(to)

  if (isValid(fromPipe, toPipe)) {

    topColor(fromPipe) match
      case Some(color) =>

        // select blocks to move and determine how many can be moved
        val selected = fromPipe.content.reverse.takeWhile(_ == color)
        val same = selected.size
        val space = toPipe.capacity - toPipe.content.size
        val amount = math.min(same, space)

        // update pipes
        val tmp = selected.take(amount)
        val fromPipe2 = Pipe(fromPipe.capacity, fromPipe.content.dropRight(amount))
        val toPipe2 = Pipe(toPipe.capacity, toPipe.content ++ tmp.reverse)

        // new GameState
        val newPipes = state.pipes.updated(from, fromPipe2).updated(to, toPipe2)
        GameState(newPipes)

      case None => state

  } else state

}

// Checks every move if all pipes contains blocks of same color
def isSolved(state: GameState): Boolean = {
  true
} // checks gamestate after each move operation

//support methods like topColor, canPour, isEmpty, isFull

def isFull(pipe: Pipe): Boolean = {
  pipe.content.size == pipe.capacity
}

def topColor(pipe: Pipe): Option[Color] = {
  if (pipe.content.isEmpty)
    None
  else
    Some(pipe.content.last)
}

/** pourable if pipe is not empty || pipe is not solved def canPour(pipe: Pipe): Boolean = {}
  */

def printPipe(height: Int = 2, width: Int = 1): String =

  val wall = "|" + " " * width + "|\n"
  val body = wall * height
  val bottom = "+" + "-" * width + "+\n"
  body + bottom

def printPipes(pipeCount: Int, height: Int, width: Int): String =
  val wallLine = ("|" + " " * width + "|" + "  ") * pipeCount + "\n"
  val body = wallLine * height
  val bottomLine = ("+" + "-" * width + "+" + "  ") * pipeCount + "\n"
  "\n\n" + body + bottomLine


/**
  * Causing a Merge Conflict
*/