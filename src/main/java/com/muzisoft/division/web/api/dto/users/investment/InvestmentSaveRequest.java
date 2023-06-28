package com.muzisoft.division.web.api.dto.users.investment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvestmentSaveRequest {

    private String userDetailsSeq;
    private String name;
    private String bankName;
    private String accountNumber;
    private int amount;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date depositedAt;
}
