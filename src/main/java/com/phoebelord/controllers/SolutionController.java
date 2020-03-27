package com.phoebelord.controllers;

import com.phoebelord.ChristmasDinner;
import com.phoebelord.algorithms.genetic.crossover.CrossoverType;
import com.phoebelord.algorithms.genetic.selection.SelectionType;
import com.phoebelord.dao.ConfigRepository;
import com.phoebelord.exception.ForbiddenException;
import com.phoebelord.exception.NotFoundException;
import com.phoebelord.model.Config;
import com.phoebelord.model.MaximisationType;
import com.phoebelord.model.Solution;
import com.phoebelord.security.CurrentUser;
import com.phoebelord.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class SolutionController {

  @Autowired
  ConfigRepository configRepository;
  private Logger log = LoggerFactory.getLogger(SolutionController.class);


  @GetMapping(value = "/api/solution/{configId}")
  @PreAuthorize("hasRole('USER')")
  @ResponseBody
  public Solution[] getASolution(@CurrentUser UserPrincipal currentUser,
                                 @PathVariable int configId,
                                 @RequestParam String maximisation,
                                 @RequestParam String selection,
                                 @RequestParam String crossover,
                                 @RequestParam int iterations,
                                 @RequestParam int selectionSize,
                                 @RequestParam int generationSize,
                                 @RequestParam float mutationRate) {
    Config config = configRepository.findById(configId).orElseThrow(() -> new NotFoundException("Config", "id", configId));
    if (currentUser.getId() != config.getCreatedBy()) {
      log.info(currentUser.getUsername() + " attempted to get the solution to someone else's config");
      throw new ForbiddenException("You do not have access to this config");
    }
    log.info(currentUser.getUsername() + " getting solution for config " + config.getId() +
      "[Maximisation: " + maximisation +
      ", Selection: " + selection +
      ", Crossover: " + crossover +
      ", Iterations: " + iterations +
      ", selectionSize: " + selectionSize +
      ", generationsSize: " + generationSize +
      ", mutation rate: " + mutationRate + "]");

    return ChristmasDinner.getSolution(config,
      MaximisationType.valueOf(maximisation),
      SelectionType.valueOf(selection),
      CrossoverType.valueOf(crossover),
      iterations,
      selectionSize,
      generationSize,
      mutationRate);
  }
}
