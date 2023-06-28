package com.muzisoft.division.web.api.dto.users.dues;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDuesListResponse {

    private int rowNum;
    private String paidAt;
    private int amount;
    private String comment;
    private String confirmedAt;
    private int paymentState;
    private int year;
    private int month;
}
