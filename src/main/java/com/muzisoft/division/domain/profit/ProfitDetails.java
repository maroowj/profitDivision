package com.muzisoft.division.domain.profit;

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

@DynamicInsert
@DynamicUpdate
@Entity
@Getter
@Setter(value = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table
public class ProfitDetails extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_profit_details")
    @GenericGenerator(name = "seq_manager_profit_details", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "prdt_"),
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
    @JoinColumn(name = "profit_seq", nullable = false)
    private Profit profit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_details_seq", nullable = false)
    private UserDetails userDetails;

    @Column(nullable = false)
    private long total;

    @Column(nullable = false)
    private int amount;

    @Column
    private int userLevel;

    public static ProfitDetails create(Profit profit, UserDetails userDetails, int userLevel, long total, int amount) {
        ProfitDetails profitDetails = new ProfitDetails();

        profitDetails.setProfit(profit);
        profitDetails.setUserDetails(userDetails);
        profitDetails.setUserLevel(userLevel);
        profitDetails.setTotal(total);
        profitDetails.setAmount(amount);
        return profitDetails;
    }
}
