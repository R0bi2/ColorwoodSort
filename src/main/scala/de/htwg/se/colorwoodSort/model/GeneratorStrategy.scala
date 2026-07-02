package de.htwg.se.colorwoodSort.model

/** ------------------------------------------------------- Strategy Pattern -------------------------------------------------------
  *
  * Das Strategy Pattern kapselt den Erzeugungs-Algorithmus des Spielfelds hinter einem
  * gemeinsamen Interface. Dadurch kann der Controller den Schwierigkeitsgrad zur Laufzeit
  * austauschen, ohne die eigentliche Generierungslogik zu kennen.
  *
  * Alle Strategien verwenden denselben Kern-Algorithmus (`generator`), unterscheiden sich aber
  * in der Anzahl der Shuffle-Schritte (`count`): mehr Schritte => stärker durchmischt => schwerer.
  */

trait GeneratorStrategy {
  def generate(pipeCount: Int, pipeHeight: Int, colors: List[Color]): GameState
}

case object EasyGenerator extends GeneratorStrategy {
  override def generate(pipeCount: Int, pipeHeight: Int, colors: List[Color]): GameState =
    generator(pipeCount, pipeHeight, colors, count = 10)
}

case object MediumGenerator extends GeneratorStrategy {
  override def generate(pipeCount: Int, pipeHeight: Int, colors: List[Color]): GameState =
    generator(pipeCount, pipeHeight, colors, count = 30)
}

case object HardGenerator extends GeneratorStrategy {
  override def generate(pipeCount: Int, pipeHeight: Int, colors: List[Color]): GameState =
    generator(pipeCount, pipeHeight, colors, count = 80)
}
