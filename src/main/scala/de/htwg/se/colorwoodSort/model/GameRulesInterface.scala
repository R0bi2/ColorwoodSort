package de.htwg.se.colorwoodSort.model

/** ------------------------------------------------------- Task 10: Component Interface -------------------------------------------------------
  *
  * Interface der Regel-Komponente des Models.
  *
  * Die Controller-Komponente greift auf die Spielregeln ausschliesslich ueber dieses Interface
  * (bzw. das Singleton `GameRules`) zu. Die eigentlichen Implementierungs-Funktionen in
  * GameRules.scala sind mit private[model] gekapselt und von aussen nicht mehr erreichbar.
  *
  * Zusammen mit GeneratorStrategy (Interface der Generator-Komponente) ist das Model damit
  * vollstaendig ueber Interfaces gekapselt; GameState und Pipe sind unveraenderliche
  * Datenstrukturen und bilden das oeffentliche Vokabular der Komponente.
  */
trait GameRulesInterface {

  /** Prueft, ob ein Zug von fromPipe nach toPipe regelkonform ist. */
  def isValid(fromPipe: Pipe, toPipe: Pipe): Boolean

  /** Fuehrt einen Zug aus und liefert den neuen Spielzustand (oder den alten bei ungueltigem Zug). */
  def move(state: GameState, from: Int, to: Int): GameState

  /** Prueft, ob das Spiel geloest ist. */
  def isSolved(state: GameState): Boolean
}

/** Standard-Implementierung der Regel-Komponente: delegiert an die gekapselten Funktionen. */
object GameRules extends GameRulesInterface {

  override def isValid(fromPipe: Pipe, toPipe: Pipe): Boolean =
    de.htwg.se.colorwoodSort.model.isValid(fromPipe, toPipe)

  override def move(state: GameState, from: Int, to: Int): GameState =
    de.htwg.se.colorwoodSort.model.move(state, from, to)

  override def isSolved(state: GameState): Boolean =
    de.htwg.se.colorwoodSort.model.isSolved(state)
}
