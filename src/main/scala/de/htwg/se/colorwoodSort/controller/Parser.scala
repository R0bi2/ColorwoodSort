package de.htwg.se.colorwoodSort.controller

/*
  takes players console input of color moves
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
