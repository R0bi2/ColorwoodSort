#!/bin/bash
# Startet das Spiel im Docker-Container und reicht die Swing-GUI per X11 an den Host durch.
# Unter WSL2 (WSLg) und den meisten Linux-Desktops funktioniert das direkt.
# Falls die GUI nicht erscheint, auf dem Host einmal `xhost +local:` ausfuehren.
set -e

docker build -t colorwoodsort .

docker run -it --rm \
  -e DISPLAY="${DISPLAY:-:0}" \
  -v /tmp/.X11-unix:/tmp/.X11-unix \
  colorwoodsort
