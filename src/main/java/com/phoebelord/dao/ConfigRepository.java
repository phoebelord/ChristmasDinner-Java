package com.phoebelord.dao;

import com.phoebelord.model.Config;
import com.phoebelord.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Integer> {

  List<Config> findAllByCreatedBy(int userId);
}
