package com.phoebelord.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Solution {
  private List<Arrangement> arrangements;
  private int happinessScore;

  public Solution(List<Table> tables, List<Guest> guests, int happinessScore) {
    this.happinessScore = happinessScore;
    this.arrangements = initialiseArrangements(tables, guests);
  }

  private List<Arrangement> initialiseArrangements(List<Table> tables, List<Guest> guests) {
    List<Arrangement> arrangements = new ArrayList<>();
    for(Table table : tables) {
      Arrangement arrangement = new Arrangement(table.getShape(), guests.subList(table.getOffset(), table.getOffset() + table.getCapacity()).stream().map(Guest::getName).collect(Collectors.toList()));
      arrangements.add(arrangement);
    }
    return arrangements;
  }

  public List<Arrangement> getArrangements() {
    return arrangements;
  }

  public int getHappinessScore() {
    return happinessScore;
  }
}
