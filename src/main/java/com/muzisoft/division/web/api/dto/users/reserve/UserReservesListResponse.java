package com.muzisoft.division.web.api.dto.users.reserve;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserReservesListResponse {

    private int rowNum;
    private String createdAt;
    private String content;
    private int type;
    private int amount;
    private int residual;
}
