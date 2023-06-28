package com.muzisoft.division.web.api.dto.admin.interest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterestListPerUserResponse {

    private String userDetailsSeq;
    private String membershipNumber;
    private String name;
    private String userId;
    private String level;
    private long total;
    private long amount;
}
