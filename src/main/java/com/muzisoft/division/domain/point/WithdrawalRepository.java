package com.muzisoft.division.domain.point;

import com.muzisoft.division.domain.user.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal, String>, WithdrawalQueryRepository {

    boolean existsAllByUserDetailsAndAcceptedAndSource(UserDetails userDetails, int accepted, int source);


}