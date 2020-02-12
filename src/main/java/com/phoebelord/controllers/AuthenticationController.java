package com.phoebelord.controllers;

import com.phoebelord.dao.RoleRepository;
import com.phoebelord.dao.UserRepository;
import com.phoebelord.exception.AppException;
import com.phoebelord.model.Role;
import com.phoebelord.model.RoleName;
import com.phoebelord.model.User;
import com.phoebelord.payload.ApiResponse;
import com.phoebelord.payload.JwtAuthenticationResponse;
import com.phoebelord.payload.LoginRequest;
import com.phoebelord.payload.SignUpRequest;
import com.phoebelord.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

  private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  JwtTokenProvider jwtTokenProvider;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = jwtTokenProvider.generateToken(authentication);
    return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
    if(userRepository.existsByEmail(signUpRequest.getEmail())){
      return new ResponseEntity(new ApiResponse(false, "Email address already in use"), HttpStatus.BAD_REQUEST);
    }

    User user = new User();
    user.setEmail(signUpRequest.getEmail());
    user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

    Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User Role not set"));
    user.setRoles(Collections.singleton(userRole));

    User result = userRepository.save(user);
    URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{email}").buildAndExpand(result.getEmail()).toUri();

    return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
  }

}
