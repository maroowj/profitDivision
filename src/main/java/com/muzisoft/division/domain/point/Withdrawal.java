package com.muzisoft.division.domain.point;

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
public class Withdrawal extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_withdrawal")
    @GenericGenerator(name = "seq_manager_withdrawal", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.INCREMENT_PARAM, value = "1"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "wdrl_"),
            @org.hibernate.annotations.Parameter(name = SeqManager.NUMBER_FORMAT_PARAMETER, value = "%010d"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_COLUMN_PARAM, value = "seq")
    })
    @Column(nullable = false, updatable = false, length = 15)
    @Id
    private String seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_details_seq", nullable = false)
    private UserDetails userDetails;

    @Column(nullable = false, length = 50)
    private String bankName;

    @Column(nullable = false, length = 50)
    private String accountNumber;

    @Column(nullable = false)
    private long totalBalance;

    @Column(nullable = false)
    private long amount;

    @Column(nullable = false, columnDefinition = "tinyint(1) default 0")
    private int accepted;

    @Column(columnDefinition = "text")
    private String comment;

    @Column(nullable = false)
    private Date requestedAt;

    @Column
    private Date acceptedAt;

    @Column(columnDefinition = "tinyint(1)")
    private int source; // 0:수익금 / 1: 적립금

    public static Withdrawal create(UserDetails userDetails, String bankName, String accountNumber, long totalBalance, long amount, int source) {
        Withdrawal withdrawal = new Withdrawal();

        withdrawal.setUserDetails(userDetails);
        withdrawal.setBankName(bankName);
        withdrawal.setAccountNumber(accountNumber);
        withdrawal.setTotalBalance(totalBalance);
        withdrawal.setAmount(amount);
        withdrawal.setSource(source);
        withdrawal.setRequestedAt(new Date());
        return withdrawal;
    }

    public void accept(long totalBalance) {
        setAccepted(1);
        setAcceptedAt(new Date());
        setTotalBalance(totalBalance);
    }

    public void refuse(String comment) {
        setAccepted(2);
        setComment(comment);
    }

    public void cancelWithdrawal() {
        setAccepted(3);
    }
}
