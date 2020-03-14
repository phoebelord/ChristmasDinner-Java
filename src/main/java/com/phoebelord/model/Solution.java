package com.phoebelord.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Solution implements Comparable {
  private List<Arrangement> arrangements;
  private int happinessScore;
  private int generationNumber = 0;

  public Solution(List<Table> tables, List<Guest> guests, int happinessScore) {
    this.happinessScore = happinessScore;
    this.arrangements = initialiseArrangements(tables, guests);
  }

  public Solution(List<Table> tables, List<Guest> guests, int happinessScore, int generationNumber) {
    this.happinessScore = happinessScore;
    this.arrangements = initialiseArrangements(tables, guests);
    this.generationNumber = generationNumber;
  }

  public Solution() {
    arrangements = new ArrayList<>();
    happinessScore = 0;
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

  public int getGenerationNumber() {
    return generationNumber;
  }

  @Override
  public int compareTo(Object o) {
    Solution other = (Solution) o;
    return Integer.compare(this.generationNumber, other.generationNumber);
  }

}
