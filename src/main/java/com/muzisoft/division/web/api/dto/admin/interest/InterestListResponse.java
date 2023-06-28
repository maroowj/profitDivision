package com.muzisoft.division.web.api.dto.admin.interest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterestListResponse {

    private String interestSeq;
    private float interestRate;
    private long total;
    private long amount;
    private String createdAt;
}
