val x = 2

2 + x

enum Color { case R, G, B, Y }
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
