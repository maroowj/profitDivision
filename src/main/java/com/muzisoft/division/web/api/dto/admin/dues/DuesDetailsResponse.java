package com.muzisoft.division.web.api.dto.admin.dues;

import com.muzisoft.division.domain.dues.Dues;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DuesDetailsResponse {

    private String duesSeq;
    private String membershipNumber;
    private String userId;
    private String name;
    private String mobile;
    private String bankName;
    private String accountNumber;
    private int amount;
    private int paidAmount;
    private int paymentState;
    private String comment;
    private int year;
    private int month;

    public DuesDetailsResponse(Dues dues, String userId) {
        this.duesSeq = dues.getSeq();
        this.membershipNumber = dues.getUserDetails().getMembershipNumber();
        this.userId = userId;
        this.name = dues.getUserDetails().getName();
        this.mobile = dues.getUserDetails().getMobile();
        this.bankName = dues.getBankName();
        this.accountNumber = dues.getAccountNumber();
        this.amount = dues.getDuesMonth().getAmount();
        this.paidAmount = dues.getAmount();
        this.paymentState = dues.getPaymentState();
        this.comment = dues.getComment();
        this.year = dues.getDuesMonth().getYear();
        this.month = dues.getDuesMonth().getMonth();
    }
}
