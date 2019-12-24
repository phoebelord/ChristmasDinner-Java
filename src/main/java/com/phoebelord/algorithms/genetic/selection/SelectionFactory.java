package com.phoebelord.algorithms.genetic.selection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SelectionFactory {

  private static RouletteWheelSelection rouletteWheelSelection;

  public static Selection createSelectionMethod(SelectionType selectionType) {
    switch (selectionType) {
      case ROULETTE: return rouletteWheelSelection;
      case TOURNAMENT:
      default: return new TournamentSelection(); //throw exception?
    }
  }

  @Autowired
  public void setRouletteWheelSelection(RouletteWheelSelection rouletteWheelSelection) {
    SelectionFactory.rouletteWheelSelection = rouletteWheelSelection;
  }
}
