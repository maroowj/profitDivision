package com.muzisoft.division.web.api.dto.admin.dues;

import com.muzisoft.division.domain.dues.DuesMonth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DuesStatsResponse {

    private int total;
    private int paid;
    private int unpaid;

}
