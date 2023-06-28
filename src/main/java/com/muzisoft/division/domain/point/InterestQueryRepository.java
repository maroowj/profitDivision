package com.muzisoft.division.domain.point;

import com.muzisoft.division.web.api.dto.admin.interest.InterestListPerUserResponse;
import com.muzisoft.division.web.api.dto.admin.interest.InterestListResponse;
import com.muzisoft.division.web.api.dto.common.BasicCondition;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestQueryRepository {

    Page<InterestListResponse> interestList(Pageable pageable, CommonCondition cond);
}