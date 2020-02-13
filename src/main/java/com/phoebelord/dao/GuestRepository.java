package com.phoebelord.dao;

import com.phoebelord.model.Config;
import com.phoebelord.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Integer> {

  Optional<Guest> findByNameAndConfig(String name, Config config);
}
