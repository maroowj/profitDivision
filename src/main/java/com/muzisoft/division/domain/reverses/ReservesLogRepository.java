package com.muzisoft.division.domain.reverses;

import com.muzisoft.division.domain.user.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservesLogRepository extends JpaRepository<ReservesLog, String>, ReservesLogQueryRepository {

    Optional<ReservesLog> findTopByUserDetailsOrderByCreatedAtDesc(UserDetails userDetails);
}