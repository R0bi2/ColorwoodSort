package de.htwg.se.colorwoodSort.util

/** ------------------------------------------------------- Observer Pattern -------------------------------------------------------
  *
  * Interface fuer beobachtbare Objekte (Subjects). Ein Observable verwaltet eine Liste von
  * [[Observer]]-Instanzen und benachrichtigt diese bei Zustandsaenderungen.
  *
  * Im Spiel implementiert [[de.htwg.se.colorwoodSort.controller.ControllerInterface]] dieses
  * Interface und informiert TUI sowie GUI ueber [[de.htwg.se.colorwoodSort.controller.ControllerEvent]]s.
  *
  * @tparam T Typ der Benachrichtigung, die an die Observer gesendet wird.
  */
trait Observable[T] {

  private var subscribers: Vector[Observer[T]] = Vector()

  /** Registriert einen Observer fuer kuenftige Benachrichtigungen. */
  def add(s: Observer[T]): Unit =
    subscribers = subscribers :+ s

  /** Entfernt einen zuvor registrierten Observer. */
  def remove(s: Observer[T]): Unit = subscribers = subscribers.filterNot(_ == s)

  /** Benachrichtigt alle registrierten Observer mit dem uebergebenen Wert. */
  def notifyObservers(value: T): Unit =
    subscribers.foreach(_.update(value))
}
