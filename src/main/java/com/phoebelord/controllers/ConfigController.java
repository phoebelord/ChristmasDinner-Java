package com.phoebelord.controllers;

import com.phoebelord.dao.ConfigRepository;
import com.phoebelord.dao.UserRepository;
import com.phoebelord.exception.ForbiddenException;
import com.phoebelord.exception.NotFoundException;
import com.phoebelord.model.Config;
import com.phoebelord.model.User;
import com.phoebelord.payload.ApiResponse;
import com.phoebelord.payload.ConfigRequest;
import com.phoebelord.payload.ConfigResponse;
import com.phoebelord.security.CurrentUser;
import com.phoebelord.security.UserPrincipal;
import com.phoebelord.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

  @Autowired
  ConfigService configService;

  @Autowired
  ConfigRepository configRepository;

  @Autowired
  UserRepository userRepository;

  @PostMapping("/create")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> createConfig(@Valid @RequestBody ConfigRequest configRequest) {
    Config config = configService.createConfig(configRequest);
    configRepository.save(config);

    URI location = ServletUriComponentsBuilder
      .fromCurrentRequest().path("/{configId}")
      .buildAndExpand(config.getId()).toUri();

    return ResponseEntity.created(location).body(new ApiResponse(true, "Config Created Successfully"));
  }

  @GetMapping("/{configId}")
  @PreAuthorize("hasRole('USER')")
  public ConfigResponse getConfigById(@CurrentUser UserPrincipal currentUser,
                           @PathVariable Integer configId) {

    Config config = configRepository.findById(configId).orElseThrow(() -> new NotFoundException("Config", "id", configId));
    if(currentUser.getId() != config.getCreatedBy()) {
      throw new ForbiddenException("You do not have access to this config");
    }
    return configService.createConfigResponse(config);
  }

  @GetMapping("/all")
  @PreAuthorize("hasRole('USER')")
  public List<ConfigResponse> getCurrentUserConfigs(@CurrentUser UserPrincipal currentUser) {
    User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new NotFoundException("User", "id", currentUser.getId()));
    List<Config> configs = configRepository.findAllByCreatedBy(user.getId());
    List<ConfigResponse> configResponses = new ArrayList<>();
    configs.forEach(config -> configResponses.add(new ConfigResponse(config)));
    return configResponses;
  }
}
