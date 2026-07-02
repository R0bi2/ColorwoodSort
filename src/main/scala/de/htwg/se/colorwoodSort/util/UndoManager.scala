package de.htwg.se.colorwoodSort.util

/** ------------------------------------------------------- Undo/Redo (Command Pattern) -------------------------------------------------------
  *
  * Der UndoManager verwaltet die Historie ausgeführter Commands über zwei Stapel:
  *   - undoStack: bereits ausgeführte Commands, die rückgängig gemacht werden können
  *   - redoStack: rückgängig gemachte Commands, die wiederhergestellt werden können
  *
  * Er ist generisch (T = Typ des Zustands) und kennt die konkreten Commands nicht.
  */
class UndoManager[T] {

  private var undoStack: List[Command[T]] = Nil
  private var redoStack: List[Command[T]] = Nil

  /** Führt einen Command aus. Nur wenn er den Zustand tatsächlich verändert (gültiger Zug),
    * wird er auf den Undo-Stapel gelegt und die Redo-Historie verworfen.
    */
  def doStep(state: T, command: Command[T]): T = {
    val newState = command.doStep(state)
    if (newState == state) newState
    else {
      undoStack = command :: undoStack
      redoStack = Nil
      newState
    }
  }

  /** Macht den letzten Command rückgängig und legt ihn auf den Redo-Stapel.
    * Gibt None zurück, wenn es nichts rückgängig zu machen gibt.
    */
  def undoStep(state: T): Option[T] = undoStack match {
    case Nil => None
    case head :: rest =>
      undoStack = rest
      redoStack = head :: redoStack
      Some(head.undoStep(state))
  }

  /** Stellt den zuletzt rückgängig gemachten Command wieder her und legt ihn zurück auf den Undo-Stapel.
    * Gibt None zurück, wenn es nichts wiederherzustellen gibt.
    */
  def redoStep(state: T): Option[T] = redoStack match {
    case Nil => None
    case head :: rest =>
      redoStack = rest
      undoStack = head :: undoStack
      Some(head.redoStep(state))
  }

  def canUndo: Boolean = undoStack.nonEmpty
  def canRedo: Boolean = redoStack.nonEmpty

  def clear(): Unit = {
    undoStack = Nil
    redoStack = Nil
  }
}
