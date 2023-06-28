package com.muzisoft.division.web.api.dto.admin.interest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterestDetailsResponse {

    private String createdAt;
    private long total;
    private long amount;
}
