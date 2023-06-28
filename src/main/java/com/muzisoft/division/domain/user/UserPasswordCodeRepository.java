package com.muzisoft.division.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPasswordCodeRepository extends JpaRepository<UserPasswordCode, Integer> {

    List<UserPasswordCode> findAllByUserAndUsed(User user, boolean used);

    Optional<UserPasswordCode> findByCodeAndUsed(String code, boolean used);
}