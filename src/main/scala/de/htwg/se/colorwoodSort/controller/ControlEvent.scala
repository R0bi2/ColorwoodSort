package de.htwg.se.colorwoodSort.controller

import de.htwg.se.colorwoodSort.model.GameState

/** Nachrichten, die der Controller an registrierte Observer (TUI, GUI) sendet.
  *
  * Teil des Observer Patterns: Views implementieren `update(ControllerEvent)` und
  * reagieren auf Zustandsaenderungen oder Textmeldungen, ohne den Controller zu kennen.
  */
enum ControllerEvent {

  /** Der Spielstand hat sich geaendert — Views sollen neu zeichnen. */
  case StateChanged(state: GameState)

  /** Eine Textmeldung (Fehler, Gewinn, Undo-Hinweis, ...) an den Spieler. */
  case Message(text: String)
}
