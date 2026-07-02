package de.htwg.se.colorwoodSort.util

/** ------------------------------------------------------- Observer Pattern -------------------------------------------------------
  *
  * Interface fuer Beobachter eines [[Observable]]. Implementierungen reagieren auf
  * Benachrichtigungen, die das Subject bei Zustandsaenderungen versendet.
  *
  * Im Spiel implementieren [[de.htwg.se.colorwoodSort.Aview.View]] (TUI) und
  * [[de.htwg.se.colorwoodSort.Aview.Gui]] dieses Interface und aktualisieren ihre
  * Darstellung bei jedem [[de.htwg.se.colorwoodSort.controller.ControllerEvent]].
  *
  * @tparam T Typ der Benachrichtigung, die vom Observable empfangen wird.
  */
trait Observer[T] {

  /** Wird aufgerufen, wenn sich der beobachtete Zustand geaendert hat. */
  def update(value: T): Unit
}
