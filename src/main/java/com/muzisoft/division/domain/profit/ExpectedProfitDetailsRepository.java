package com.muzisoft.division.domain.profit;

import com.muzisoft.division.domain.user.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpectedProfitDetailsRepository extends JpaRepository<ExpectedProfitDetails, String>, ExpectedProfitDetailsQueryRepository {

    void deleteAllByExpectedProfit(ExpectedProfit expectedProfit);
    ExpectedProfitDetails findByExpectedProfitAndUserDetails(ExpectedProfit expectedProfit, UserDetails userDetails);
}