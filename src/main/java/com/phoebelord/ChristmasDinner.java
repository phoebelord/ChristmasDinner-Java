package com.phoebelord;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoebelord.algorithms.*;
import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;
import com.phoebelord.model.Solution;
import com.phoebelord.model.Table;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ChristmasDinner {
  private static List<Person> people;
  private static List<Seat> seats;
  private static List<Table> tables;

  public static void main(String[] args) {

    if(args.length != 1) {
      System.out.println("Requires data set argument!");
      return;
    }

    try {
      initialiseFromFile(args[0]);
      System.out.println("\nTables: " + tables);
      System.out.println("Seats: " + seats);
      System.out.println("\nPeople: " + people);

      Algorithm algorithm = AlgorithmFactory.createAlgorithm(AlgorithmType.Genetic, people, seats, tables);
      Solution solution = algorithm.calculateSolution();

      System.out.println("Arrangement: " + solution.getArrangement());
      System.out.println("Score: " + solution.getHappinessScore());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void initialiseFromFile(String dataSet) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    people = mapper.readValue(getResource(dataSet + "/people.json"), new TypeReference<List<Person>>() {});
    seats = mapper.readValue(getResource(dataSet + "/seats.json"), new TypeReference<List<Seat>>() {});
    tables = mapper.readValue(getResource(dataSet + "/tables.json"), new TypeReference<List<Table>>() {});

  }

  private static URL getResource(String filename) throws FileNotFoundException {
    URL resource = ClassLoader.getSystemClassLoader().getResource(filename);
    if(resource != null) {
      return ClassLoader.getSystemClassLoader().getResource(filename);
    } else {
      throw new FileNotFoundException("Resource " + filename + " not found");
    }
  }
}
