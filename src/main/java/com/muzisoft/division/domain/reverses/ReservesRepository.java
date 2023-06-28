package com.muzisoft.division.domain.reverses;

import com.muzisoft.division.domain.user.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservesRepository extends JpaRepository<Reserves, String>, ReservesQueryRepository {

    List<Reserves> findAllByUserDetailsAndUsable(UserDetails userDetails, boolean usable);
}