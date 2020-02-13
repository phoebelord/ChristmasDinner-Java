package com.phoebelord.service;

import com.phoebelord.dao.UserRepository;
import com.phoebelord.exception.NotFoundException;
import com.phoebelord.model.User;
import com.phoebelord.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("userService")
public class UserServiceImpl implements UserDetailsService {

  private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

  private final UserRepository userRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public boolean isUniqueEmail(String email) {
    return userRepository.findByEmail(email) == null;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

    return UserPrincipal.create(user);
  }

  @Transactional
  public UserDetails loadUserById(int id) {
    User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User", "id", id));
    return UserPrincipal.create(user);
  }


}