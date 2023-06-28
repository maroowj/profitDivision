package com.muzisoft.division.web.api.dto.users.dues;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DuesConfirmRequest {

    private int year;
    private int month;
    private int amount;
    private String bankName;
    private String accountNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date paidAt;
}
