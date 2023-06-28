package com.muzisoft.division.domain.reverses;

import com.muzisoft.division.domain.base.BaseTimeEntity;
import com.muzisoft.division.domain.user.User;
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
public class ReservesLog extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_reserves_log")
    @GenericGenerator(name = "seq_manager_reserves_log", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "rsvl_"),
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

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, columnDefinition = "tinyint(1)")
    private int type; // 0: 신청 , 1: 적립, 2: 출금

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private int residual;

    public static ReservesLog saveLog(UserDetails userDetails, String content, int amount, int residual) {
        ReservesLog reservesLog = new ReservesLog();

        reservesLog.setUserDetails(userDetails);
        reservesLog.setContent(content);
        reservesLog.setAmount(amount);
        reservesLog.setResidual(residual);
        reservesLog.setType(1);
        return reservesLog;
    }

    public static ReservesLog requestLog(UserDetails userDetails, String content, int amount, int residual) {
        ReservesLog reservesLog = new ReservesLog();

        reservesLog.setUserDetails(userDetails);
        reservesLog.setContent(content);
        reservesLog.setAmount(amount);
        reservesLog.setResidual(residual);
        reservesLog.setType(0);
        return reservesLog;
    }

    public static ReservesLog useLog(UserDetails userDetails, String content, int amount, int residual) {
        ReservesLog reservesLog = new ReservesLog();

        reservesLog.setUserDetails(userDetails);
        reservesLog.setContent(content);
        reservesLog.setAmount(amount);
        reservesLog.setResidual(residual);
        reservesLog.setType(2);
        return reservesLog;
    }

    public static ReservesLog failLog(UserDetails userDetails, String content, int amount, int residual) {
        ReservesLog reservesLog = new ReservesLog();

        reservesLog.setUserDetails(userDetails);
        reservesLog.setContent(content);
        reservesLog.setAmount(amount);
        reservesLog.setResidual(residual);
        reservesLog.setType(3);
        return reservesLog;
    }
}
