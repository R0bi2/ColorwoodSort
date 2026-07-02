# ------------------------------------------------------- Task: Deploy on Docker -------------------------------------------------------
#
# Mehrstufiges Dockerfile:
#   Stufe 1 (build): offizielles sbt/Scala-Image kompiliert das Projekt und baut per
#                    sbt-assembly ein Fat-Jar mit allen Abhaengigkeiten.
#   Stufe 2 (run):   schlankes JRE-Image mit den X11-Bibliotheken, die Java Swing
#                    fuer die GUI braucht. Nur das Fat-Jar wird uebernommen.
#
# Bauen:   docker build -t colorwoodsort .
# Starten: ./docker-run.sh   (reicht die GUI per X11 auf den Host durch)

# ---------- Stufe 1: Build ----------
FROM sbtscala/scala-sbt:eclipse-temurin-21.0.11_10_1.12.13_3.3.8 AS build
WORKDIR /app

# Erst nur die Build-Definition kopieren, damit Docker die heruntergeladenen
# Abhaengigkeiten cachen kann und nicht bei jeder Codeaenderung neu aufloest
COPY build.sbt ./
COPY project/build.properties project/plugins.sbt ./project/
RUN sbt update

# Dann den Quellcode kopieren und das Fat-Jar bauen
COPY src ./src
RUN sbt assembly

# ---------- Stufe 2: Runtime ----------
FROM eclipse-temurin:21-jre

# X11- und Font-Bibliotheken fuer die Swing-GUI
RUN apt-get update && apt-get install -y --no-install-recommends \
    libxext6 libxrender1 libxtst6 libxi6 libxrandr2 fontconfig fonts-dejavu-core \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=build /app/target/scala-3.8.2/colorwoodSort.jar .

CMD ["java", "-jar", "colorwoodSort.jar"]
