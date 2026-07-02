package de.htwg.se.colorwoodSort.Aview

import com.google.inject.Inject
import de.htwg.se.colorwoodSort.controller.*
import de.htwg.se.colorwoodSort.model.{Color as GameColor, *}
import de.htwg.se.colorwoodSort.util.Observer
import scala.swing.*
import scala.swing.event.*
import java.awt.{Color as AwtColor, BasicStroke, RenderingHints, Font as AwtFont, GradientPaint}

/** GUI-View des Spiels.
  *
  * Die GUI ist - genau wie die TUI - ein Observer des Controllers (Observer Pattern). Beide Views
  * registrieren sich beim selben Controller und werden bei jedem ControllerEvent benachrichtigt,
  * dadurch bleiben TUI und GUI immer synchron. Das Spiel ist komplett in der GUI spielbar:
  * Zuege per Klick, Undo/Redo/New Game per Button, Gewinn-Dialog mit direktem Neustart.
  *
  * Bedienung: Erst auf die Quell-Pipe klicken (wird hervorgehoben), dann auf die Ziel-Pipe.
  * Die GUI schickt daraus den gleichen Input-String ("from to") an den Controller wie die TUI.
  */
// Task 10: Auch die GUI kennt nur noch das ControllerInterface
// Dependency Injection: Der Controller wird von Guice in den Konstruktor injiziert
class Gui @Inject() (controller: ControllerInterface) extends MainFrame with Observer[ControllerEvent] {

  controller.add(this)

  // ------------------------------ Zustand der GUI ------------------------------
  private var selectedPipe: Option[Int] = None
  private var statusText: String = "Waehle eine Quell-Pipe."
  private var gameWon: Boolean = false

  // ------------------------------ Farben (Holz-Look) ------------------------------
  private val woodBase = new AwtColor(146, 96, 60)
  private val woodDark = new AwtColor(132, 86, 53)
  private val woodLine = new AwtColor(112, 72, 44)
  private val slotColor = new AwtColor(84, 52, 30)
  private val slotBorder = new AwtColor(64, 39, 22)
  private val creamText = new AwtColor(250, 240, 222)
  private val highlightColor = new AwtColor(255, 214, 90)
  private val buttonColor = new AwtColor(98, 62, 36)

  private def awtColor(c: GameColor): AwtColor = c match {
    case GameColor.R => new AwtColor(228, 74, 74)
    case GameColor.G => new AwtColor(88, 190, 94)
    case GameColor.Y => new AwtColor(245, 195, 46)
    case GameColor.B => new AwtColor(90, 190, 235)
    case GameColor.P => new AwtColor(170, 100, 210)
  }

  // ------------------------------ Layout-Masse ------------------------------
  private val pipeWidth = 78
  private val blockHeight = 54
  private val gap = 34
  private val topMargin = 45

  // ------------------------------ Spielfeld-Panel ------------------------------
  private val boardPanel: Panel = new Panel {
    // Ohne preferredSize kollabiert das Panel im BorderPanel auf 0 Pixel
    preferredSize = new Dimension(620, 400)
    listenTo(mouse.clicks)

    reactions += { case e: MouseClicked =>
      if (gameWon) {
        if (playAgainButtonRect.contains(e.point)) startNewGame()
      } else {
        pipeAt(e.point.x, e.point.y).foreach(handlePipeClick)
      }
    }

    // Bereich des "Noch eine Runde"-Buttons im Gewonnen-Overlay
    private def playAgainButtonRect: java.awt.Rectangle = {
      val w = 220
      val h = 48
      new java.awt.Rectangle((size.width - w) / 2, size.height / 2 + 20, w, h)
    }

    override def paintComponent(g: Graphics2D): Unit = {
      super.paintComponent(g)
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

      paintWood(g)

      Option(controller.gameState).foreach { st =>
        val height = st.pipeHeight
        val totalWidth = st.pipes.size * pipeWidth + (st.pipes.size - 1) * gap
        val startX = (size.width - totalWidth) / 2

        st.pipes.zipWithIndex.foreach { case (pipe, idx) =>
          val x = startX + idx * (pipeWidth + gap)
          drawPipe(g, pipe, idx, x, topMargin, height)
        }
      }

      if (gameWon) paintWinOverlay(g)
    }

    // Gewonnen-Overlay direkt im Spielfenster (statt separatem Dialog)
    private def paintWinOverlay(g: Graphics2D): Unit = {
      g.setColor(new AwtColor(0, 0, 0, 130))
      g.fillRect(0, 0, size.width, size.height)

      g.setColor(creamText)
      g.setFont(new AwtFont("Serif", AwtFont.BOLD, 34))
      val text = "Glueckwunsch, geloest!"
      val fm = g.getFontMetrics
      g.drawString(text, (size.width - fm.stringWidth(text)) / 2, size.height / 2 - 20)

      val r = playAgainButtonRect
      g.setColor(highlightColor)
      g.fillRoundRect(r.x, r.y, r.width, r.height, 18, 18)
      g.setColor(new AwtColor(70, 45, 20))
      g.setFont(new AwtFont("SansSerif", AwtFont.BOLD, 18))
      val btnText = "Noch eine Runde"
      val bfm = g.getFontMetrics
      g.drawString(
        btnText,
        r.x + (r.width - bfm.stringWidth(btnText)) / 2,
        r.y + (r.height + bfm.getAscent - bfm.getDescent) / 2
      )
    }

    // Holzbretter als Hintergrund
    private def paintWood(g: Graphics2D): Unit = {
      val plankWidth = 90
      var x = 0
      var i = 0
      while (x < size.width) {
        g.setColor(if (i % 2 == 0) woodBase else woodDark)
        g.fillRect(x, 0, plankWidth, size.height)
        g.setColor(woodLine)
        g.drawLine(x, 0, x, size.height)
        x += plankWidth
        i += 1
      }
      // dezente horizontale Maserung
      g.setColor(new AwtColor(0, 0, 0, 14))
      var y = 18
      while (y < size.height) {
        g.drawLine(0, y, size.width, y)
        y += 37
      }
    }

    private def drawPipe(g: Graphics2D, pipe: Pipe, idx: Int, x: Int, y: Int, height: Int): Unit = {
      val pipeHeightPx = height * blockHeight + 14

      // Eingelassener Holz-Slot
      g.setColor(slotBorder)
      g.fillRoundRect(x - 5, y - 20, pipeWidth + 10, pipeHeightPx + 25, 26, 26)
      g.setPaint(new GradientPaint(x.toFloat, y.toFloat, slotColor.darker(), x.toFloat, (y + pipeHeightPx).toFloat, slotColor))
      g.fillRoundRect(x, y - 15, pipeWidth, pipeHeightPx + 15, 20, 20)

      // Farbbloecke (content(0) ist unten)
      pipe.content.zipWithIndex.foreach { case (color, level) =>
        val blockY = y + pipeHeightPx - (level + 1) * blockHeight
        drawBlock(g, awtColor(color), x, blockY)
      }

      // Auswahl-Rahmen
      if (selectedPipe.contains(idx)) {
        g.setColor(highlightColor)
        g.setStroke(new BasicStroke(4f))
        g.drawRoundRect(x - 6, y - 21, pipeWidth + 12, pipeHeightPx + 27, 28, 28)
        g.setStroke(new BasicStroke(1f))
      }

      // Nummer unter der Pipe
      g.setColor(creamText)
      g.setFont(new AwtFont("SansSerif", AwtFont.BOLD, 17))
      val label = (idx + 1).toString
      val fm = g.getFontMetrics
      g.drawString(label, x + (pipeWidth - fm.stringWidth(label)) / 2, y + pipeHeightPx + 28)
    }

    // Glaenzender Wuerfel mit rundem Emblem (wie im Vorbild)
    private def drawBlock(g: Graphics2D, c: AwtColor, x: Int, blockY: Int): Unit = {
      val bx = x + 5
      val by = blockY + 4
      val bw = pipeWidth - 10
      val bh = blockHeight - 8

      // Grundkoerper mit Verlauf
      g.setPaint(new GradientPaint(bx.toFloat, by.toFloat, c.brighter(), bx.toFloat, (by + bh).toFloat, c.darker()))
      g.fillRoundRect(bx, by, bw, bh, 16, 16)

      // Glanz oben
      g.setColor(new AwtColor(255, 255, 255, 70))
      g.fillRoundRect(bx + 4, by + 3, bw - 8, bh / 3, 10, 10)

      // Rundes Emblem in der Mitte
      val d = math.min(bw, bh) / 2
      g.setColor(c.darker())
      g.fillOval(bx + (bw - d) / 2, by + (bh - d) / 2, d, d)
      g.setColor(new AwtColor(255, 255, 255, 90))
      g.drawOval(bx + (bw - d) / 2, by + (bh - d) / 2, d, d)

      // Kontur
      g.setColor(new AwtColor(0, 0, 0, 60))
      g.drawRoundRect(bx, by, bw, bh, 16, 16)
    }

    private def pipeAt(px: Int, py: Int): Option[Int] = {
      Option(controller.gameState).flatMap { st =>
        val height = st.pipeHeight
        val totalWidth = st.pipes.size * pipeWidth + (st.pipes.size - 1) * gap
        val startX = (size.width - totalWidth) / 2
        val pipeHeightPx = height * blockHeight + 14

        st.pipes.indices.find { idx =>
          val x = startX + idx * (pipeWidth + gap)
          px >= x - 5 && px <= x + pipeWidth + 5 &&
          py >= topMargin - 20 && py <= topMargin + pipeHeightPx + 5
        }
      }
    }
  }

  // ------------------------------ Statuszeile & Buttons ------------------------------
  private val statusLabel = new Label(statusText) {
    foreground = creamText
    font = new AwtFont("SansSerif", AwtFont.PLAIN, 14)
  }

  private def styledButton(text: String)(action: => Unit): Button = new Button(Action(text)(action)) {
    focusPainted = false
    font = new AwtFont("SansSerif", AwtFont.BOLD, 13)
    background = buttonColor
    foreground = creamText
    opaque = true
    border = Swing.EmptyBorder(8, 18, 8, 18)
  }

  private val undoButton = styledButton("Undo") { controller.processInput("u") }
  private val redoButton = styledButton("Redo") { controller.processInput("r") }
  private val newGameButton = styledButton("New Game") { startNewGame() }

  private def startNewGame(): Unit = {
    selectedPipe = None
    gameWon = false
    controller.startGame(3, 4, List("R", "G", "Y"))
  }

  // ------------------------------ Klick-Logik ------------------------------
  private def handlePipeClick(idx: Int): Unit = {
    selectedPipe match {
      case None =>
        if (controller.gameState.pipes(idx).content.isEmpty) {
          setStatus(s"Pipe ${idx + 1} ist leer. Waehle eine Pipe mit Bloecken.")
        } else {
          selectedPipe = Some(idx)
          setStatus(s"Pipe ${idx + 1} gewaehlt. Waehle die Ziel-Pipe.")
        }
      case Some(from) if from == idx =>
        selectedPipe = None
        setStatus("Auswahl aufgehoben.")
      case Some(from) =>
        selectedPipe = None
        // Gleicher Input-Weg wie die TUI -> Controller bleibt die einzige Logik-Instanz
        controller.processInput(s"${from + 1} ${idx + 1}")
    }
    boardPanel.repaint()
  }

  private def setStatus(text: String): Unit = {
    statusText = text
    statusLabel.text = text
  }

  // ------------------------------ Observer Pattern ------------------------------
  override def update(event: ControllerEvent): Unit = {
    // Events koennen aus dem TUI-Thread kommen -> Swing-Updates immer auf den EDT legen
    Swing.onEDT {
      event match {
        case ControllerEvent.StateChanged(_) =>
          gameWon = false
          setStatus("Waehle eine Quell-Pipe.")
          boardPanel.repaint()
        case ControllerEvent.Message(text) =>
          setStatus(text)
          if (text == "You solved it!") gameWon = true
          boardPanel.repaint()
      }
    }
  }

  // ------------------------------ Frame-Aufbau ------------------------------
  title = "Colorwood Sort"
  contents = new BorderPanel {
    background = woodDark

    layout(new Label("Colorwood Sort") {
      foreground = creamText
      font = new AwtFont("Serif", AwtFont.BOLD, 26)
      border = Swing.EmptyBorder(12, 0, 8, 0)
      opaque = true
      background = woodDark
    }) = BorderPanel.Position.North

    layout(boardPanel) = BorderPanel.Position.Center

    layout(new BoxPanel(Orientation.Vertical) {
      background = woodDark
      contents += new FlowPanel {
        background = woodDark
        contents ++= Seq(undoButton, redoButton, newGameButton)
      }
      contents += new FlowPanel {
        background = woodDark
        contents += statusLabel
      }
    }) = BorderPanel.Position.South
  }

  pack() // Fenster auf die Wunschgroesse aller Komponenten bringen
  centerOnScreen()

  // Fenster schliessen beendet das ganze Programm (auch die wartende TUI)
  override def closeOperation(): Unit = {
    controller.processInput("q")
    sys.exit(0)
  }

  visible = true
}
