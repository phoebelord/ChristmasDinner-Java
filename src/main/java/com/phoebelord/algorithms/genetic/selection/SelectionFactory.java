package com.phoebelord.algorithms.genetic.selection;

public class SelectionFactory {
  public static Selection createSelectionMethod(SelectionType selectionType) {
    switch (selectionType) {
      case TOURNAMENT:
      default: return new TournamentSelection(); //throw exception?
    }
  }
}
