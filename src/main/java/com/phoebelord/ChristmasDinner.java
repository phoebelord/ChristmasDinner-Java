package com.phoebelord;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoebelord.algorithms.Algorithm;
import com.phoebelord.algorithms.GeneticAlgorithm;
import com.phoebelord.algorithms.Solution;
import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;

import java.net.URL;
import java.util.List;

public class ChristmasDinner {
  private static List<Person> people;
  private static List<Seat> seats;

  public static void main(String[] args) {
    initialiseFromFile("data_b");
    System.out.println("Seats: " + seats);
    System.out.println("\nPeople: " + people);

    Algorithm geneticAlgorithm = new GeneticAlgorithm(seats.size(), people, seats, 58);
    Solution solution = geneticAlgorithm.calculateSolution(people, seats);
    System.out.println("Arrangement: " + solution.getArrangement());
    System.out.println("Score: " + solution.getHappinessScore());
  }

  private static void initialiseFromFile(String dataSet) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      people = mapper.readValue(getResource(dataSet + "/people.json"), new TypeReference<List<Person>>() {});
      seats = mapper.readValue(getResource(dataSet + "/seats.json"), new TypeReference<List<Seat>>() {});
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static URL getResource(String filename) {
    return ClassLoader.getSystemClassLoader().getResource(filename);
  }
}
