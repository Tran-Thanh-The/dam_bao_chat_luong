package com.UserManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.UserManagement.Entity.Score;

public interface ScoreRepository extends JpaRepository<Score, Long> {

}
