package com.muzisoft.division.web.api.dto.users.profit;

import com.muzisoft.division.domain.common.enums.ProfitLogType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfitLogListResponse {

    private int rowNum;
    private String createdAt;
    private String content;
    private int amount;
    private long total;
    private ProfitLogType type;
}
