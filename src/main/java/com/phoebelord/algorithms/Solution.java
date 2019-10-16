package com.phoebelord.algorithms;

import com.phoebelord.model.Person;

import java.util.List;

public class Solution {
  private List<Person> arrangement;
  private int happinessScore;

  Solution(List<Person> arrangement, int happinessScore) {
    this.arrangement = arrangement;
    this.happinessScore = happinessScore;
  }

  public List<Person> getArrangement() {
    return arrangement;
  }

  public int getHappinessScore() {
    return happinessScore;
  }

}
