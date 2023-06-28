package com.muzisoft.division.domain.point;

import com.muzisoft.division.domain.common.enums.PointSupplier;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.web.api.dto.admin.profit.TotalPointsResponse;

import java.util.Date;

public interface UserPointsQueryRepository {

    TotalPointsResponse totalPointsByUserDetailsAndPointsSupplier(UserDetails userDetails, PointSupplier pointSupplier);

    TotalPointsResponse totalPointsByUserDetailsAndPointsSupplierAndDateTo(UserDetails userDetails, PointSupplier pointSupplier, Date dateTo);

    long remainedTotalSavedAmount(Date dateTo, UserDetails userDetails);
    long remainedTotalUsedAmount(Date dateTo, UserDetails userDetails);

    // 사용자용 (수익금 + 이자)
    long remainedTotalSavedAmountForUser(UserDetails userDetails);
    long remainedTotalUsedAmountForUser(UserDetails userDetails);
}
