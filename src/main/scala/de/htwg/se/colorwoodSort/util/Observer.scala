package de.htwg.se.colorwoodSort.util

trait Observer[T] {
  def update(value: T): Unit
}
