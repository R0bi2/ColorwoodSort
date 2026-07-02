package de.htwg.se.colorwoodSort

import com.google.inject.Guice
import de.htwg.se.colorwoodSort.controller.{Controller, ControllerInterface}
import de.htwg.se.colorwoodSort.model.{GameRules, GameRulesInterface, GeneratorStrategy, MediumGenerator}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ColorwoodSortModuleSpec extends AnyWordSpec with Matchers {

  "The ColorwoodSortModule" should {

    val injector = Guice.createInjector(new ColorwoodSortModule)

    "bind ControllerInterface to a Controller instance" in {
      val controller = injector.getInstance(classOf[ControllerInterface])
      controller shouldBe a[Controller]
    }

    "bind ControllerInterface as a singleton so all views share one controller" in {
      val c1 = injector.getInstance(classOf[ControllerInterface])
      val c2 = injector.getInstance(classOf[ControllerInterface])
      c1 should be theSameInstanceAs c2
    }

    "bind GameRulesInterface to the GameRules singleton" in {
      injector.getInstance(classOf[GameRulesInterface]) should be theSameInstanceAs GameRules
    }

    "bind GeneratorStrategy to MediumGenerator" in {
      injector.getInstance(classOf[GeneratorStrategy]) shouldBe MediumGenerator
    }

    "inject the controller into the injected components" in {
      val controller = injector.getInstance(classOf[Controller])
      controller.generatorStrategy shouldBe MediumGenerator
      controller.rules should be theSameInstanceAs GameRules
    }
  }
}
