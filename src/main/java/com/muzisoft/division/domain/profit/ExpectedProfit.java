package com.muzisoft.division.domain.profit;

import com.muzisoft.division.domain.base.BaseTimeEntity;
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
public class ExpectedProfit extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_expected_profit")
    @GenericGenerator(name = "seq_manager_expected_profit", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.INCREMENT_PARAM, value = "1"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "eprf_"),
            @org.hibernate.annotations.Parameter(name = SeqManager.NUMBER_FORMAT_PARAMETER, value = "%010d"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_COLUMN_PARAM, value = "seq")
    })
    @Column(nullable = false, updatable = false, length = 15)
    @Id
    private String seq;

    @Column(nullable = false)
    private long amount;

    @Column(nullable = false)
    private int referenceYear;

    @Column(nullable = false)
    private int referenceMonth;

    public static ExpectedProfit create(long amount, int referenceYear, int referenceMonth) {
        ExpectedProfit expectedProfit = new ExpectedProfit();

        expectedProfit.setAmount(amount);
        expectedProfit.setReferenceYear(referenceYear);
        expectedProfit.setReferenceMonth(referenceMonth);
        return expectedProfit;
    }
}
