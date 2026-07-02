package de.htwg.se.colorwoodSort.util

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ObservableSpec extends AnyWordSpec with Matchers {

  class TestObserver extends Observer[String] {
    var received: Vector[String] = Vector.empty
    override def update(value: String): Unit = received = received :+ value
  }

  class TestObservable extends Observable[String]

  "An Observable" should {

    "notify added observers" in {
      val observable = new TestObservable
      val observer = new TestObserver

      observable.add(observer)
      observable.notifyObservers("hello")

      observer.received shouldBe Vector("hello")
    }

    "notify all registered observers" in {
      val observable = new TestObservable
      val o1 = new TestObserver
      val o2 = new TestObserver

      observable.add(o1)
      observable.add(o2)
      observable.notifyObservers("event")

      o1.received shouldBe Vector("event")
      o2.received shouldBe Vector("event")
    }

    "not notify removed observers anymore" in {
      val observable = new TestObservable
      val observer = new TestObserver

      observable.add(observer)
      observable.remove(observer)
      observable.notifyObservers("ignored")

      observer.received shouldBe Vector.empty
    }
  }
}
