package com.phoebelord;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoebelord.algorithms.Algorithm;
import com.phoebelord.algorithms.GeneticAlgorithm;
import com.phoebelord.algorithms.NaiveAlgorithm;
import com.phoebelord.algorithms.Solution;
import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;
import com.phoebelord.model.Table;

import java.net.URL;
import java.util.List;

public class ChristmasDinner {
  private static List<Person> people;
  private static List<Seat> seats;
  private static List<Table> tables;

  public static void main(String[] args) {
    initialiseFromFile("data_c");
    System.out.println("\nTables: " + tables);
    System.out.println("Seats: " + seats);
    System.out.println("\nPeople: " + people);

//    Algorithm naiveAlgorithm = new NaiveAlgorithm(people, seats, tables);
//    Solution solution = naiveAlgorithm.calculateSolution();

    Algorithm geneticAlgorithm = new GeneticAlgorithm(people, seats, tables);
    Solution solution = geneticAlgorithm.calculateSolution();

    System.out.println("Arrangement: " + solution.getArrangement());
    System.out.println("Score: " + solution.getHappinessScore());
  }

  private static void initialiseFromFile(String dataSet) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      people = mapper.readValue(getResource(dataSet + "/people.json"), new TypeReference<List<Person>>() {});
      seats = mapper.readValue(getResource(dataSet + "/seats.json"), new TypeReference<List<Seat>>() {});
      tables = mapper.readValue(getResource(dataSet + "/tables.json"), new TypeReference<List<Table>>() {});
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static URL getResource(String filename) {
    return ClassLoader.getSystemClassLoader().getResource(filename);
  }
}
