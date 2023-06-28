package com.muzisoft.division.web.api.dto.admin.reserve;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservesDetailsResponse {

    private String createdAt;
    private int amount;
    private int residual;
    private int type;
}
