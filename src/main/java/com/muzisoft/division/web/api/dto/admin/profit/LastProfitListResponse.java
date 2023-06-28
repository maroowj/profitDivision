package com.muzisoft.division.web.api.dto.admin.profit;

import com.muzisoft.division.domain.profit.Profit;
import com.nimbusds.oauth2.sdk.util.date.SimpleDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LastProfitListResponse {

    private String profitSeq;
    private long amount;
    private int yield;
    private String createdAt;

}
