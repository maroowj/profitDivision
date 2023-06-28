package com.muzisoft.division.domain.profit;

import com.muzisoft.division.domain.base.BaseTimeEntity;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.utils.manager.SeqManager;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@DynamicInsert
@DynamicUpdate
@Entity
@Getter
@Setter(value = AccessLevel.PROTECTED)
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table
public class ExpectedProfitDetails extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_expected_profit_details")
    @GenericGenerator(name = "seq_manager_expected_profit_details", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "epdt_"),
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
    @JoinColumn(name = "expected_profit_seq", nullable = false)
    private ExpectedProfit expectedProfit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_deatils_seq", nullable = false)
    private UserDetails userDetails;

    @Column(nullable = false)
    private int userLevel;

    @Column(nullable = false)
    private long userTotal;

    @Column(nullable = false)
    private int amount;

    public static ExpectedProfitDetails create(ExpectedProfit expectedProfit, UserDetails userDetails,
                                               int userLevel, long userTotal, int amount) {
        ExpectedProfitDetails expectedProfitDetails = new ExpectedProfitDetails();

        expectedProfitDetails.setExpectedProfit(expectedProfit);
        expectedProfitDetails.setUserDetails(userDetails);
        expectedProfitDetails.setUserLevel(userLevel);
        expectedProfitDetails.setUserTotal(userTotal);
        expectedProfitDetails.setAmount(amount);
        return expectedProfitDetails;
    }
}
