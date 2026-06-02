package de.htwg.se.colorwoodSort.util

trait Observable[T] {

  private var subscribers: Vector[Observer[T]] = Vector()

  def add(s: Observer[T]): Unit =
    subscribers = subscribers :+ s

  def remove(s: Observer[T]): Unit = subscribers = subscribers.filterNot(_ == s)

  def notifyObservers(value: T): Unit =
    subscribers.foreach(_.update(value))
}
