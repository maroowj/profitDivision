package com.muzisoft.division.web.api.dto.admin.dues;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DuesConfirmRequest {

    private int year;
    private int month;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date paidAt;
    private int amount;
    private String bankName;
    private String accountNumber;

}
