package com.phoebelord.controllers;

import com.phoebelord.dao.*;
import com.phoebelord.exception.ForbiddenException;
import com.phoebelord.exception.NotFoundException;
import com.phoebelord.model.Config;
import com.phoebelord.model.User;
import com.phoebelord.payload.ApiResponse;
import com.phoebelord.payload.NewConfigRequest;
import com.phoebelord.payload.ConfigDTO;
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

  @Autowired
  GuestRepository guestRepository;

  @Autowired
  RelationshipRepository relationshipRepository;

  @Autowired
  TableRepository tableRepository;

  @PostMapping("/create")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> createConfig(@Valid @RequestBody NewConfigRequest newConfigRequest) {
    Config config = configService.createConfig(newConfigRequest);
    configRepository.save(config);

    URI location = ServletUriComponentsBuilder
      .fromCurrentRequest().path("/{configId}")
      .buildAndExpand(config.getId()).toUri();

    return ResponseEntity.created(location).body(new ApiResponse(true, "Config Created Successfully"));
  }

  @PostMapping("/edit")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> editConfig(@CurrentUser UserPrincipal currentUser,
                                      @Valid @RequestBody ConfigDTO configDTO) {

    Config config = configRepository.findById(configDTO.getId()).orElseThrow(() -> new NotFoundException("Config", "id", configDTO.getId()));
    if(currentUser.getId() != config.getCreatedBy()) {
      throw new ForbiddenException("You do not have access to this config");
    }
    config = configService.editConfig(configDTO);
    configRepository.save(config);

    URI location = ServletUriComponentsBuilder
      .fromCurrentRequest().path("/{configId}")
      .buildAndExpand(config.getId()).toUri();

    return ResponseEntity.created(location).body(new ApiResponse(true, "Config edited Successfully"));
  }

  @DeleteMapping("/delete/{configId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> deleteConfig(@CurrentUser UserPrincipal currentUser, @PathVariable Integer configId) {
    Config config = configRepository.findById(configId).orElseThrow(() -> new NotFoundException("Config", "id", configId));
    if(currentUser.getId() != config.getCreatedBy()) {
      throw new ForbiddenException("You do not have access to this config");
    }

    config.getGuests().forEach(guest -> {
      guest.getRelationships().forEach(relationship -> relationshipRepository.delete(relationship));
      guestRepository.delete(guest);
    });
    config.getTables().forEach(table -> tableRepository.delete(table));
    configRepository.delete(config);

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{configId}")
  @PreAuthorize("hasRole('USER')")
  public ConfigDTO getConfigById(@CurrentUser UserPrincipal currentUser,
                                 @PathVariable Integer configId) {

    Config config = configRepository.findById(configId).orElseThrow(() -> new NotFoundException("Config", "id", configId));
    if(currentUser.getId() != config.getCreatedBy()) {
      throw new ForbiddenException("You do not have access to this config");
    }
    return configService.createConfigDTO(config);
  }

  @GetMapping("/all")
  @PreAuthorize("hasRole('USER')")
  public List<ConfigDTO> getCurrentUserConfigs(@CurrentUser UserPrincipal currentUser) {
    User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new NotFoundException("User", "id", currentUser.getId()));
    List<Config> configs = configRepository.findAllByCreatedBy(user.getId());
    List<ConfigDTO> configResponses = new ArrayList<>();
    configs.forEach(config -> configResponses.add(new ConfigDTO(config)));
    return configResponses;
  }
}
