package com.muzisoft.division.domain.reverses;

import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.web.api.dto.admin.reserve.ReservesDetailsResponse;
import com.muzisoft.division.web.api.dto.admin.reserve.ReservesListResponse;
import com.muzisoft.division.web.api.dto.common.BasicCondition;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.users.reserve.UserReservesListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservesLogQueryRepository {

    Page<ReservesListResponse> reservesLogList(Pageable pageable, BasicCondition basicCondition, CommonCondition condition);
    Page<ReservesDetailsResponse> reservesDetails(Pageable pageable, UserDetails userDetails, CommonCondition condition);

    Page<UserReservesListResponse> userReservesList(Pageable pageable, CommonCondition condition, UserDetails userDetails);
}