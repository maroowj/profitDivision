package com.muzisoft.division.domain.profit;

import com.muzisoft.division.domain.base.BaseTimeEntity;
import com.muzisoft.division.domain.common.enums.ProfitLogType;
import com.muzisoft.division.domain.point.InterestDetails;
import com.muzisoft.division.domain.point.Withdrawal;
import com.muzisoft.division.domain.user.UserDetails;
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
import java.util.GregorianCalendar;

@DynamicInsert
@DynamicUpdate
@Entity
@Getter
@Setter(value = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table
public class RemainderLog extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_remainder_log")
    @GenericGenerator(name = "seq_manager_remainder_log", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.INCREMENT_PARAM, value = "1"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "rlog_"),
            @org.hibernate.annotations.Parameter(name = SeqManager.NUMBER_FORMAT_PARAMETER, value = "%010d"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_COLUMN_PARAM, value = "seq")
    })
    @Column(nullable = false, updatable = false, length = 15)
    @Id
    private String seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profit_seq", nullable = false)
    private Profit profit;

    @Column(nullable = false)
    private long profitAmount;

    @Column(nullable = false)
    private int remainder;

    @Column(length = 255)
    private String content;

    public static RemainderLog create(Profit profit, long profitAmount, int remainder) {
        RemainderLog remainderLog = new RemainderLog();
        GregorianCalendar today = new GregorianCalendar();
        String content = today.get(today.YEAR) + "년 " + (today.get(today.MONTH)+1) + "월 " + today.get(today.DAY_OF_MONTH) + "일 수익 배분";

        remainderLog.setProfit(profit);
        remainderLog.setProfitAmount(profitAmount);
        remainderLog.setRemainder(remainder);
        remainderLog.setContent(content);
        return remainderLog;
    }

}
