package de.htwg.se.colorwoodSort.util

/** ------------------------------------------------------- Command Pattern -------------------------------------------------------
  *
  * Interface fuer ausfuehrbare, rueckgaengig machbare Aktionen auf einem Zustand vom Typ `T`.
  *
  * Der UndoManager arbeitet ausschliesslich mit diesem Interface und kennt keine konkreten
  * Command-Implementierungen (z. B. [[de.htwg.se.colorwoodSort.controller.MoveCommand]]).
  * Jeder Command kapselt eine Aktion als Objekt, sodass Ausfuehren, Undo und Redo einheitlich
  * verwaltet werden koennen.
  *
  * @tparam T Typ des Zustands, auf dem der Command operiert (im Spiel: [[de.htwg.se.colorwoodSort.model.GameState]]).
  */
trait Command[T] {

  /** Fuehrt die Aktion aus und liefert den neuen Zustand. */
  def doStep(state: T): T

  /** Macht die zuletzt ausgefuehrte Aktion rueckgaengig. */
  def undoStep(state: T): T

  /** Fuehrt eine zuvor rueckgaengig gemachte Aktion erneut aus. */
  def redoStep(state: T): T
}
