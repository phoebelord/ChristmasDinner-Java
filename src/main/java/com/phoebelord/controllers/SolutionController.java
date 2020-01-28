package com.phoebelord.controllers;

import com.phoebelord.ChristmasDinner;
import com.phoebelord.algorithms.AlgorithmType;
import com.phoebelord.model.Solution;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class SolutionController {

  @RequestMapping(value = "/", method = RequestMethod.GET)
  @CrossOrigin(origins = "http://localhost:3000")
  @ResponseBody
  public Solution[] getSolution(@RequestParam String dataSet, @RequestParam String algorithmType) {
     return new Solution[]{ChristmasDinner.getSolution(dataSet, AlgorithmType.valueOf(algorithmType))};
  }
}
