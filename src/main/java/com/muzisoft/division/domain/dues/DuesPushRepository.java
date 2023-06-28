package com.muzisoft.division.domain.dues;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DuesPushRepository extends JpaRepository<DuesPush, String> {
}