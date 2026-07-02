package de.htwg.se.colorwoodSort

import com.google.inject.{AbstractModule, Scopes}
import de.htwg.se.colorwoodSort.controller.{Controller, ControllerInterface}
import de.htwg.se.colorwoodSort.model.{GameRules, GameRulesInterface, GeneratorStrategy, MediumGenerator}

/** ------------------------------------------------------- Dependency Injection -------------------------------------------------------
  *
  * Das Guice-Modul bindet die Komponenten-Interfaces (Task 10) an konkrete Instanzen.
  * Nur hier wird entschieden, welche Implementierung hinter einem Interface steckt -
  * der Rest der Anwendung kennt ausschliesslich die Interfaces.
  *
  *   - ControllerInterface -> Controller (als Singleton, damit TUI und GUI denselben
  *     Controller beobachten und synchron bleiben)
  *   - GameRulesInterface  -> GameRules (Regel-Komponente des Models)
  *   - GeneratorStrategy   -> MediumGenerator (Standard-Schwierigkeitsgrad)
  */
class ColorwoodSortModule extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[ControllerInterface]).to(classOf[Controller]).in(Scopes.SINGLETON)
    bind(classOf[GameRulesInterface]).toInstance(GameRules)
    bind(classOf[GeneratorStrategy]).toInstance(MediumGenerator)
  }
}
