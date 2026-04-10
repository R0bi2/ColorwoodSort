package de.htwg.se.colorwoodSort

object x {

  def main(args: Array[String]): Unit = {
    println(printSkipes(3, 9, 10))
  }

}

def printSkipes(pipeCount: Int, height: Int, width: Int): String =
  if (pipeCount <= 0 || height <= 0 || width <= 0) "\n\nInvalid dimensions for pipes.\n"
  else {
    val wallLine = ("|" + " " * width + "|" + "  ") * pipeCount + "\n"
    val body = wallLine * height
    val bottomLine = ("+" + "-" * width + "+" + "  ") * pipeCount + "\n"
    "\n\n" + body + bottomLine
  }
