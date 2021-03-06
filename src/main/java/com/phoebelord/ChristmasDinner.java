package com.phoebelord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoebelord.algorithms.Algorithm;
import com.phoebelord.algorithms.AlgorithmFactory;
import com.phoebelord.algorithms.AlgorithmType;
import com.phoebelord.algorithms.genetic.crossover.CrossoverType;
import com.phoebelord.algorithms.genetic.selection.SelectionType;
import com.phoebelord.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@SpringBootApplication
@EntityScan(basePackageClasses = {
  ChristmasDinner.class,
  Jsr310JpaConverters.class
})
public class ChristmasDinner {

  private static Logger log = LoggerFactory.getLogger(ChristmasDinner.class);

  public static void main(String[] args) {
    SpringApplication.run(ChristmasDinner.class, args);
  }

  public static Solution[] getSolution(Config config,
                                       MaximisationType maximisationType,
                                       SelectionType selection,
                                       CrossoverType crossover,
                                       int iterations,
                                       int selectionSize,
                                       int generationSize,
                                       float mutationRate) {
    List<Guest> guests = config.getGuests();
    List<Table> tables = config.getTables();

    log.info("Calculating solution for Config[ID: " + config.getId() + ", Name: " + config.getName() + ", No. Guests: " + config.getGuests().size() + "]");

    Solution[] solutions;
    if (guests.size() > 0 && tables.size() > 0) {
      Algorithm algorithm = AlgorithmFactory.createAlgorithm(AlgorithmType.Genetic, guests, tables, maximisationType, selection, crossover, iterations, selectionSize, generationSize, mutationRate);
      solutions = algorithm.calculateSolution();
      log.info("Score: " + solutions[solutions.length - 1].getHappinessScore());
    } else {
      solutions = new Solution[0];
    }

    return solutions;
  }

  private static <T> List<T> initialiseFromFile(String filename, Class<T> clazz) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(getResource("data/" + filename), mapper.getTypeFactory().constructCollectionType(List.class, clazz));
  }

  private static URL getResource(String filename) throws FileNotFoundException {
    URL resource = ClassLoader.getSystemClassLoader().getResource(filename);
    if (resource != null) {
      return ClassLoader.getSystemClassLoader().getResource(filename);
    } else {
      throw new FileNotFoundException("Resource " + filename + " not found");
    }
  }
}
