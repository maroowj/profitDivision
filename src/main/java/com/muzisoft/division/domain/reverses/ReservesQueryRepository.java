package com.muzisoft.division.domain.reverses;

import com.muzisoft.division.domain.user.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservesQueryRepository {

    long totalSavedAmount(UserDetails userDetails);
    long totalUsedAmount(UserDetails userDetails);
}