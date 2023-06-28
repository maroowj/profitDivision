package com.muzisoft.division.domain.profit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfitDetailsRepository extends JpaRepository<ProfitDetails, String>, ProfitDetailsQueryRepository {
}