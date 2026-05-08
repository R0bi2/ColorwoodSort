package de.htwg.se.colorwoodSort.controller

import de.htwg.se.colorwoodSort.model.GameState

enum ControllerEvent {
  case StateChanged(state: GameState)
  case Message(text: String)
}
