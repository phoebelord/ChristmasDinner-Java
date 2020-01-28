package com.phoebelord.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Solution {
  private List<Arrangement> arrangements;
  private int happinessScore;

  public Solution(List<Table> tables, List<Person> people, int happinessScore) {
    this.happinessScore = happinessScore;
    this.arrangements = initialiseArrangements(tables, people);
  }

  private List<Arrangement> initialiseArrangements(List<Table> tables, List<Person> people) {
    List<Arrangement> arrangements = new ArrayList<>();
    for(Table table : tables) {
      Arrangement arrangement = new Arrangement(table.getShape(), people.subList(table.getOffset(), table.getOffset() + table.getSize()).stream().map(Person::getName).collect(Collectors.toList()));
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
