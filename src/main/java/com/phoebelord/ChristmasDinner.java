package com.phoebelord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoebelord.algorithms.Algorithm;
import com.phoebelord.algorithms.NaiveAlgorithm;
import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;
import com.phoebelord.algorithms.Solution;

import java.net.URL;
import java.util.Arrays;

public class ChristmasDinner {
  private static Person[] people;
  private static Seat[] seats;

  public static void main(String[] args) {
    initialiseFromFile("data_b");
    System.out.println("Seats: " + Arrays.toString(seats));
    System.out.println("\nPeople: " + Arrays.toString(people));

    Algorithm naiveAlgorithm = new NaiveAlgorithm();
    Solution solution = naiveAlgorithm.calculateSolution(people, seats);
    System.out.println("Arrangement: " + Arrays.toString(solution.getArrangement()));
    System.out.println("Score: " + solution.getHappinessScore());
  }

  private static void initialiseFromFile(String dataSet) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      people = mapper.readValue(getResource(dataSet + "/people.json"), Person[].class);
      seats = mapper.readValue(getResource(dataSet + "/seats.json"), Seat[].class);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static URL getResource(String filename) {
    return ClassLoader.getSystemClassLoader().getResource(filename);
  }
}
