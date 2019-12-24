package com.phoebelord;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoebelord.algorithms.Algorithm;
import com.phoebelord.algorithms.AlgorithmFactory;
import com.phoebelord.algorithms.AlgorithmType;
import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;
import com.phoebelord.model.Solution;
import com.phoebelord.model.Table;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@SpringBootApplication
public class ChristmasDinner {

  private static List<Person> people;
  private static List<Seat> seats;
  private static List<Table> tables;

  public static void main(String[] args) {
    SpringApplication.run(ChristmasDinner.class, args);
  }

  public static Solution getSolution(String filename, AlgorithmType algorithmType) {
    try {
      initialiseFromFile(filename);
      System.out.println("\nTables: " + tables);
      System.out.println("Seats: " + seats);
      System.out.println("\nPeople: " + people);

      Algorithm algorithm = AlgorithmFactory.createAlgorithm(algorithmType, people, seats, tables);
      Solution solution = algorithm.calculateSolution();

      System.out.println("Arrangement: " + solution.getArrangement());
      System.out.println("Score: " + solution.getHappinessScore());

      return solution;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private static void initialiseFromFile(String dataSet) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    people = mapper.readValue(getResource("data/" + dataSet + "/people.json"), new TypeReference<List<Person>>() {});
    seats = mapper.readValue(getResource("data/" + dataSet + "/seats.json"), new TypeReference<List<Seat>>() {});
    tables = mapper.readValue(getResource("data/" + dataSet + "/tables.json"), new TypeReference<List<Table>>() {});

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
