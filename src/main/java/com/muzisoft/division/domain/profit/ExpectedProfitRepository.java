package com.muzisoft.division.domain.profit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpectedProfitRepository extends JpaRepository<ExpectedProfit, String>, ExpectedProfitQueryRepository {

    Optional<ExpectedProfit> findByReferenceYearAndReferenceMonth(int year, int month);
    ExpectedProfit findTopByOrderBySeqDesc();
}