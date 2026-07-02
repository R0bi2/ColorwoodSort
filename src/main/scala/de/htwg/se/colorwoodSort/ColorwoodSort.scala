package de.htwg.se.colorwoodSort
import com.google.inject.Guice
import de.htwg.se.colorwoodSort.model.*
import de.htwg.se.colorwoodSort.controller.*
import de.htwg.se.colorwoodSort.Aview.*

/** Einstiegspunkt der ColorwoodSort-Anwendung.
  *
  * Startet GUI und TUI ueber Google Guice Dependency Injection.
  * Beide Views teilen sich denselben [[de.htwg.se.colorwoodSort.controller.ControllerInterface]]-Singleton
  * und bleiben durch das Observer Pattern synchron.
  */
object colorwoodSort {
  def main(args: Array[String]): Unit =
    // Dependency Injection: Der Injector baut den Objektgraphen anhand des Moduls auf.
    // Kein `new Controller()` mehr in der Anwendung - Guice injiziert den (Singleton-)Controller
    // in GUI und TUI, dadurch beobachten beide Views automatisch dieselbe Instanz.
    val injector = Guice.createInjector(new ColorwoodSortModule)
    val gui = injector.getInstance(classOf[Gui])
    val tui = injector.getInstance(classOf[View])
    tui.startGame(3, 4, List("R", "G", "Y"))
}
