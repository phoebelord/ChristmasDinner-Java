package com.phoebelord.controllers;

import com.phoebelord.ChristmasDinner;
import com.phoebelord.algorithms.AlgorithmType;
import com.phoebelord.dao.ConfigRepository;
import com.phoebelord.exception.NotFoundException;
import com.phoebelord.model.Config;
import com.phoebelord.model.MaximisationType;
import com.phoebelord.model.Solution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SolutionController {

  @Autowired
  ConfigRepository configRepository;

  @GetMapping(value = "/api/solution")
  @ResponseBody
  public Solution[] getSolution(@RequestParam String dataSet, @RequestParam String algorithmType) {
     return new Solution[]{ChristmasDinner.getSolution(dataSet, AlgorithmType.valueOf(algorithmType))};
  }

  @GetMapping(value = "/api/solution/{configId}")
  @ResponseBody
  public Solution getASolution(@PathVariable int configId, @RequestParam String type) {
    Config config = configRepository.findById(configId).orElseThrow(() -> new NotFoundException("Config", "id", configId));
    return ChristmasDinner.getSolution(config, MaximisationType.valueOf(type));
  }
}
