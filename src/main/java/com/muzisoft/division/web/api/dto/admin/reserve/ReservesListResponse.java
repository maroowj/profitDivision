package com.muzisoft.division.web.api.dto.admin.reserve;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservesListResponse {

    private String userDetailsSeq;
    private String membershipNumber;
    private String userId;
    private String name;
    private String mobile;
    private int amount;
    private String createdAt;
    private String content;
}
