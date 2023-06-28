package com.muzisoft.division.domain.dues;

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
public class DuesMonth extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_dues_month")
    @GenericGenerator(name = "seq_manager_dues_month", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.INCREMENT_PARAM, value = "1"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "dmnt_"),
            @org.hibernate.annotations.Parameter(name = SeqManager.NUMBER_FORMAT_PARAMETER, value = "%010d"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_COLUMN_PARAM, value = "seq")
    })
    @Column(nullable = false, updatable = false, length = 15)
    @Id
    private String seq;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false, name = "_year")
    private int year;

    @Column(nullable = false, name = "_month")
    private int month;

    @Column(nullable = false)
    private int requestedAt;

    public static DuesMonth create(int amount, int year, int month, int requestedAt) {
        DuesMonth duesMonth = new DuesMonth();

        duesMonth.setAmount(amount);
        duesMonth.setYear(year);
        duesMonth.setMonth(month);
        duesMonth.setRequestedAt(requestedAt);
        return duesMonth;
    }
}
