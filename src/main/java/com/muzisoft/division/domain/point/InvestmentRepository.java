package com.muzisoft.division.domain.point;

import com.muzisoft.division.domain.user.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, String>, InvestmentQueryRepository {

    Optional<Investment> findTopByUserDetailsAndStatusOrderBySeqDesc(UserDetails userDetails, int status);
}