package com.phoebelord.controllers;

import com.phoebelord.dao.UserRepository;
import com.phoebelord.payload.UserIdentityAvailability;
import com.phoebelord.payload.UserSummary;
import com.phoebelord.security.CurrentUser;
import com.phoebelord.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

  @Autowired
  UserRepository userRepository;

  @GetMapping("/user/checkEmailAvailability")
  public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
    boolean isAvailable = !userRepository.existsByEmail(email);
    return new UserIdentityAvailability(isAvailable);
  }

  @GetMapping("/user/me")
  @PreAuthorize("hasRole('USER')")
  public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
    UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername());
    return userSummary;
  }
}
