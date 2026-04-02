enum Color { case R, G, B, Y }

case class Pipe(content: List[Color], capacity: Int)

case class GameState(pipes: Vector[Pipe])

val p1 = Pipe(List(Color.R, Color.G), 3)
val p2 = Pipe(Nil, 3)
val state = GameState(Vector(p1, p2))

println(state.pipes(0).content)
