package com.muzisoft.division.domain.dues;

import com.muzisoft.division.domain.base.BaseTimeEntity;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.utils.manager.SeqManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@DynamicInsert
@DynamicUpdate
@Entity
@Getter
@Setter(value = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table
public class Dues extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_dues")
    @GenericGenerator(name = "seq_manager_dues", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "dues_"),
            @org.hibernate.annotations.Parameter(name = SeqManager.NUMBER_FORMAT_PARAMETER, value = "%010d"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_COLUMN_PARAM, value = "seq"),
            @org.hibernate.annotations.Parameter(name = SeqManager.OPT_PARAM, value = "pooled"),
            @org.hibernate.annotations.Parameter(name = SeqManager.INITIAL_PARAM, value = "1"),
            @org.hibernate.annotations.Parameter(name = SeqManager.INCREMENT_PARAM, value = "1000")
    })
    @Column(nullable = false, updatable = false, length = 15)
    @Id
    private String seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dues_month_seq", nullable = false)
    private DuesMonth duesMonth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_details_seq", nullable = false)
    private UserDetails userDetails;

    @Column
    private int amount;

    @Column(nullable = false, columnDefinition = "tinyint(1) default 0")
    private int paymentState;

    @Column
    private Date paidAt;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(length = 20)
    private String bankName;

    @Column(length = 50)
    private String accountNumber;

    @Column
    private Date confirmedAt;

    @Column(columnDefinition = "text")
    private String comment;

    @Column(name = "_read", nullable = false, columnDefinition = "tinyint(1) default 1")
    private boolean read;

    public static Dues create(DuesMonth duesMonth, UserDetails userDetails, String name) {
        Dues dues = new Dues();

        dues.setDuesMonth(duesMonth);
        dues.setUserDetails(userDetails);
        dues.setName(name);
        dues.setAmount(0);
        return dues;
    }

    public void updatePaymentState(int paymentState, String comment) {
        this.setPaymentState(paymentState);
        this.setRead(true);
        if(paymentState==2) {
            this.setConfirmedAt(new Date());
        }
        this.setComment(comment);
    }

    public void requestConfirm(int amount, String bankName, String accountNumber, Date paidAt) {
        this.setAmount(amount);
        this.setBankName(bankName);
        this.setAccountNumber(accountNumber);
        this.setPaidAt(paidAt);
        this.setPaymentState(1);
        this.setRead(false);
    }
}
