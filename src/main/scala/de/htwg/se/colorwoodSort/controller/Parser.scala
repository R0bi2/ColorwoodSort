package de.htwg.se.colorwoodSort.controller

import de.htwg.se.colorwoodSort.model.*
import scala.util.{Try, Success, Failure}

/*
  takes players console input of color moves
 */

private[controller] def parseMove(input: String, pipeCount: Int): Option[(Int, Int)] = {
  val parts = input.trim.split("\\s+")
  if (parts.length != 2) None
  else {
    // Hier nutzen wir die Try-Monade statt toIntOption
    val fromTry = Try(parts(0).toInt)
    val toTry = Try(parts(1).toInt)

    (fromTry, toTry) match {
      // Wir packen die Monade aus: Nur wenn beide Konvertierungen ein Success waren, machen wir weiter
      case (Success(from1), Success(to1)) =>
        val from = from1 - 1
        val to = to1 - 1
        if (from >= 0 && from < pipeCount && to >= 0 && to < pipeCount && from != to) Some((from, to))
        else None
      // Wenn auch nur ein Try ein Failure war (z.B. wegen Buchstaben), geben wir None zurück
      case _ => None
    }
  }
}

private[controller] def parseColor(s: String): Color = {
  s match {
    case "R"   => Color.R
    case "G"   => Color.G
    case "Y"   => Color.Y
    case "B"   => Color.B
    case "P"   => Color.P
    case other => throw new IllegalArgumentException(s"Unknown color: $other")
  }
}
