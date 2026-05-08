package de.htwg.se.colorwoodSort.view
import de.htwg.se.colorwoodSort.model.*

object printyPrint {}

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
