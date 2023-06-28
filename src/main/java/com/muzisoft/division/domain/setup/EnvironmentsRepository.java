package com.muzisoft.division.domain.setup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvironmentsRepository extends JpaRepository<Environments, Long> {
}