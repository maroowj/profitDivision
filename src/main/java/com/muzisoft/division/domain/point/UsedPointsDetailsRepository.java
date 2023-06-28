package com.muzisoft.division.domain.point;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsedPointsDetailsRepository extends JpaRepository<UsedPointsDetails, String> {
}