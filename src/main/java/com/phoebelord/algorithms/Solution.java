package com.phoebelord.algorithms;

import com.phoebelord.model.Person;

public class Solution {
  private Person[] arrangement;
  private int happinessScore;

  Solution(Person[] arrangement, int happinessScore) {
    this.arrangement = arrangement;
    this.happinessScore = happinessScore;
  }

  public Person[] getArrangement() {
    return arrangement;
  }

  public int getHappinessScore() {
    return happinessScore;
  }

}
