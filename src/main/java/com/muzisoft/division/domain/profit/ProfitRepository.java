package com.muzisoft.division.domain.profit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProfitRepository extends JpaRepository<Profit, String>, ProfitQueryRepository {
    List<Profit> findAllByCreatedAtBetween(Date dateFrom, Date dateTo);
    Optional<Profit> findTopByOrderByCreatedAtDesc();
}