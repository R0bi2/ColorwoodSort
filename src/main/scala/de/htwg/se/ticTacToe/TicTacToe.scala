@main def TicTacToe(): Unit =
  println("Willkommen verdammt zu meinem Spiel ;| ");
  println(mesh(2, 2))

//val bar = "+---+---+---+ \n"
def bar(cellWith: Int = 3, cellNmb: Int = 3) =
  ("+" + "-" * cellWith) * cellNmb + "+\n";
def col(cellWith: Int = 3, cellNmb: Int = 3) =
  ("|" + " " * cellWith) * cellNmb + "|\n";

def mesh(cellWith: Int = 3, cellNmb: Int = 3) =
  (bar(cellWith, cellNmb) + col(cellWith, cellNmb)) * cellNmb + bar(cellWith, cellNmb);
