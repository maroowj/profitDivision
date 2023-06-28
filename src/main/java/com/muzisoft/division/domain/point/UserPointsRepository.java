package com.muzisoft.division.domain.point;

import com.muzisoft.division.domain.common.enums.PointSupplier;
import com.muzisoft.division.domain.user.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPointsRepository extends JpaRepository<UserPoints, String>, UserPointsQueryRepository {

    List<UserPoints> findAllByUserDetailsAndSupplierAndUsable(UserDetails userDetails, PointSupplier supplier, boolean usable);
}