package com.muzisoft.division.domain.dues;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DuesMonthRepository extends JpaRepository<DuesMonth, String> {

    Optional<DuesMonth> findByYearAndMonth(int year, int month);
}