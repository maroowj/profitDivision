package com.muzisoft.division.domain.point;

import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.web.api.dto.admin.interest.InterestDetailsResponse;
import com.muzisoft.division.web.api.dto.admin.interest.InterestListPerUserResponse;
import com.muzisoft.division.web.api.dto.common.BasicCondition;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestDetailsQueryRepository {

    Page<InterestListPerUserResponse> interestListByInterest(Pageable pageable, Interest interest, BasicCondition basicCondition, CommonCondition condition);

    Page<InterestDetailsResponse> interestDetails(Pageable pageable, UserDetails userDetails, CommonCondition condition);
}