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
public class Investment extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_investment")
    @GenericGenerator(name = "seq_manager_investment", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.INCREMENT_PARAM, value = "1"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "ivst_"),
            @org.hibernate.annotations.Parameter(name = SeqManager.NUMBER_FORMAT_PARAMETER, value = "%010d"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_COLUMN_PARAM, value = "seq")
    })
    @Column(nullable = false, updatable = false, length = 15)
    @Id
    private String seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_details_seq", nullable = false)
    private UserDetails userDetails;

    @Column(nullable = false)
    private long total;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 20)
    private String bankName;

    @Column(nullable = false, length = 50)
    private String accountNumber;

    @Column(nullable = false)
    private Date depositedAt;

    @Column(columnDefinition = "text")
    private String comment;

    @Column(nullable = false, columnDefinition = "tinyint(1) default 0")
    private int status;

    @Column
    private Date acceptedAt;

    public static Investment create(UserDetails userDetails, String name, String bankName, String accountNumber, int amount, Date depositedAt) {
        Investment investment = new Investment();
        investment.setUserDetails(userDetails);
        investment.setName(name);
        investment.setBankName(bankName);
        investment.setAccountNumber(accountNumber);
        investment.setAmount(amount);
        investment.setDepositedAt(depositedAt);
        investment.setStatus(0);
        return investment;
    }

    public void accept(int amount, long total) {
        setAmount(amount);
        setTotal(total);
        setStatus(1);
        setAcceptedAt(new Date());
    }

    public void decline(String comment) {
        setStatus(2);
        setComment(comment);
    }
}
