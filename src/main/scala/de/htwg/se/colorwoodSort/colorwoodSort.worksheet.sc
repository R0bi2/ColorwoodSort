val x = 2

2 + x

enum Color:
  case R, G, Y, B
//case class ColorBlock(color: Color)

case class Pipe(capacity: Int = 1, content: List[Color] = Nil) //old list type ColorBlock
case class GameState(pipes: Vector[Pipe])

//val p0 = Pipe(3, List(ColorBlock(Color.R), ColorBlock(Color.G)))

val p1 = Pipe(3, List(Color.G, Color.G, Color.G))
val p2 = Pipe(2, List(Color.Y, Color.Y))

val state = GameState(Vector(p1, p2))

println(state.pipes(0).content(0))
val y = state.pipes(1).content(1)

val pipe = state.pipes(1)
val newContent = pipe.content.updated(1, Color.G)
val newPipe = pipe.copy(content = newContent)
val state2 = state.copy(pipes = state.pipes.updated(1, newPipe))

def printPipes(pipeCount: Int, height: Int, width: Int): String =
  if (pipeCount <= 0 || height <= 0 || width <= 0) "\n\nInvalid dimensions for pipes.\n"
  else {
    val wallLine = ("|" + " " * width + "|" + "  ") * pipeCount + "\n"
    val body = wallLine * height
    val bottomLine = ("+" + "-" * width + "+" + "  ") * pipeCount + "\n"
    "\n\n" + body + bottomLine
  }

printPipes(4, 4, 3)

// Wir nehmen 'var' statt 'val', damit wir den Wert ändern dürfen.
// Und wir nennen sie 'counter', damit sie sich nicht mit Zeile 7 beißt.

var counter: Int = 5

while (counter > 0) {
  println(counter)
  counter -= 1 // Das funktioniert jetzt wunderbar!
}

var c = 4
var d = 3

println(c + d)
