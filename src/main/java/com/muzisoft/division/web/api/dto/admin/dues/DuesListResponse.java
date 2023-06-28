package com.muzisoft.division.web.api.dto.admin.dues;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import javax.inject.Named;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DuesListResponse {

    private Page<DuesList> duesList;
    private int duesWaitingCount;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DuesList {
        private String duesSeq;
        private String membershipNumber;
        private String userId;
        private String name;
        private String mobile;
        private String bankName;
        private String accountNumber;
        private int amount;
        private int paidAmount;
        private String paidAt;
        private int paymentState;
    }
}
