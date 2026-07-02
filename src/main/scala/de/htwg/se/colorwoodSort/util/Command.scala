package de.htwg.se.colorwoodSort.util

trait Command[T] {
  def doStep(state: T): T
  def undoStep(state: T): T
  def redoStep(state: T): T
}
