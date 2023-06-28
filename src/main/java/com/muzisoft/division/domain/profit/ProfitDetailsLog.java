package com.muzisoft.division.domain.profit;

import com.muzisoft.division.domain.base.BaseTimeEntity;
import com.muzisoft.division.domain.common.enums.ProfitLogType;
import com.muzisoft.division.domain.point.InterestDetails;
import com.muzisoft.division.domain.point.Withdrawal;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.utils.converter.AccountBookTypeConverter;
import com.muzisoft.division.utils.converter.ProfitLogTypeConverter;
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
public class ProfitDetailsLog extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_profit_details_log")
    @GenericGenerator(name = "seq_manager_profit_details_log", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "plog_"),
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
    @JoinColumn(name = "user_details_seq", nullable = false)
    private UserDetails userDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profit_details_seq")
    private ProfitDetails profitDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_details_seq")
    private InterestDetails interestDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "withdrawal_details_seq")
    private Withdrawal withdrawal;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private long total;

    @Convert(converter = ProfitLogTypeConverter.class)
    @Column(nullable = false, columnDefinition = "tinyint(1)")
    private ProfitLogType type;

    @Column(length = 255)
    private String content;

    public static ProfitDetailsLog createByProfit(ProfitDetails profitDetails, UserDetails userDetails, int amount, long total, String content) {
        ProfitDetailsLog profitDetailsLog = new ProfitDetailsLog();

        profitDetailsLog.setProfitDetails(profitDetails);
        profitDetailsLog.setUserDetails(userDetails);
        profitDetailsLog.setAmount(amount);
        profitDetailsLog.setTotal(total);
        profitDetailsLog.setContent(content);
        profitDetailsLog.setType(ProfitLogType.DEPOSIT);
        return profitDetailsLog;
    }

    public static ProfitDetailsLog createByInterest(InterestDetails interestDetails, UserDetails userDetails, int amount, long total, String content) {
        ProfitDetailsLog profitDetailsLog = new ProfitDetailsLog();

        profitDetailsLog.setInterestDetails(interestDetails);
        profitDetailsLog.setUserDetails(userDetails);
        profitDetailsLog.setAmount(amount);
        profitDetailsLog.setTotal(total);
        profitDetailsLog.setContent(content);
        profitDetailsLog.setType(ProfitLogType.DEPOSIT);
        return profitDetailsLog;
    }

    public static ProfitDetailsLog createByWithdrawal(Withdrawal withdrawal, UserDetails userDetails, int amount, long total, String content) {
        ProfitDetailsLog profitDetailsLog = new ProfitDetailsLog();

        profitDetailsLog.setWithdrawal(withdrawal);
        profitDetailsLog.setUserDetails(userDetails);
        profitDetailsLog.setAmount(amount);
        profitDetailsLog.setTotal(total);
        profitDetailsLog.setContent(content);
        profitDetailsLog.setType(ProfitLogType.WITHDRAWAL);
        return profitDetailsLog;
    }
}
