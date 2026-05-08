package de.htwg.se.colorwoodSort
import de.htwg.se.colorwoodSort.model.*
import de.htwg.se.colorwoodSort.controller.*
import de.htwg.se.colorwoodSort.view.*

/*
 * Gut — das ist der richtige Move. Hier sind deine 10 Kern-Bausteine für Scala (reicht für 80%):
 *
 *val / var → Variablen
 *List(...) → Daten speichern
 *.head / .tail → erstes Element / Rest
 *.last → oberstes Element
 *.size → Länge
 *.take(n) / .drop(n) → vorne nehmen / entfernen
 *.takeRight(n) / .dropRight(n) → hinten nehmen / entfernen
 *.map(f) → jedes Element verändern
 *.filter(f) → Elemente auswählen
 *if (...) ... else ... → Logik
 *
 *Das ist dein „Alphabet“.
 *
 * Merksatz: Alles Komplexe ist nur Kombination dieser 10.
 */

/** ------------------------------------------------------ main method to run the game ----------------------------------------------
  */

object colorwoodSort {
  val eol = "\n"
  def main(args: Array[String]): Unit = {

    View.startGame(3, height = 4, colorStrings = List("R", "G", "Y"))
  }
}

/** ------------------------------------------------------- -------------------------------------------------------
  */

/*  JDepend testing:
    java -cp jdepend-2.10.jar jdepend.textui.JDepend D:\SE\colorwoodsort-se_SS_2026\target\scala-3.8.2\classes
 */

/*  Graphviz output
    dot -Tpng colorwood.dot -o colorwood.png

    (base) PS C:\Users\rohef\OneDrive\Desktop> dot -Tpng colorwood.dot -o colorwoodGraph.png
 */

/* sbt clean compile test coverage
  sbt coverageReport
sonar-scanner
 */
