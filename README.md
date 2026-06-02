## sbt project compiled with Scala 3

### Usage

This is a normal sbt project. You can compile code with `sbt compile`, run it with `sbt run`, and `sbt console` will start a Scala 3 REPL.

For more information on the sbt-dotty plugin, see the
[scala3-example-project](https://github.com/scala/scala3-example-project/blob/main/README.md).

# ColorwoodSort

A Colorwood Sort implementation in **Scala 3** using **MVC Architecture**, **Observer Pattern**, **ScalaTest**, and **SonarQube**.

---

# Console Commands

## Build and Run

Compile the project:

```bash
sbt compile
```

Run the application:

```bash
sbt run
```

Start a Scala REPL:

```bash
sbt console
```

---

## Test Coverage

Compile, run tests, and generate coverage data:

```bash
sbt clean compile test coverage
```

Generate coverage report:

```bash
sbt coverageReport
```

---

## SonarQube

Run static code analysis:

```bash
sonar-scanner
```

---

## JDepend

Generate package dependency metrics:

```bash
java -cp jdepend-2.10.jar jdepend.textui.JDepend target\scala-3.8.2\classes
```

---

## Graphviz

Convert a generated `.dot` file into a PNG image:

```bash
dot -Tpng colorwood.dot -o colorwood.png
```

Example:

```bash
dot -Tpng colorwood.dot -o colorwoodGraph.png
```

---

# Scala Quick Reference

## Variables

```scala
val x = 42   // immutable
var y = 42   // mutable
```

## Lists

```scala
List(1, 2, 3)
```

## Accessing Elements

```scala
list.head
list.tail
list.last
```

## Size

```scala
list.size
```

## Taking and Dropping Elements

```scala
list.take(n)
list.drop(n)

list.takeRight(n)
list.dropRight(n)
```

## Transforming Lists

```scala
list.map(f)
```

## Filtering Lists

```scala
list.filter(f)
```

## Conditional Logic

```scala
if (condition)
  value1
else
  value2
```

---

# Personal Scala Cheat Sheet

The following concepts cover a large portion of everyday Scala programming:

* `val` / `var`
* `List(...)`
* `.head` / `.tail`
* `.last`
* `.size`
* `.take(n)` / `.drop(n)`
* `.takeRight(n)` / `.dropRight(n)`
* `.map(f)`
* `.filter(f)`
* `if / else`

> Everything complex is ultimately built from combinations of these basic building blocks.

