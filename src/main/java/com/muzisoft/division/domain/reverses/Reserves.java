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
public class Reserves extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_reserves")
    @GenericGenerator(name = "seq_manager_reserves", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "rsvs_"),
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

    @Column(nullable = false, columnDefinition = "tinyint(1) default 1")
    private boolean usable;

    @Column(nullable = false)
    private int savedAmount;

    @Column(nullable = false)
    private int usedAmount;

    public static Reserves saveByProfit(UserDetails userDetails, int savedAmount) {
        Reserves reserves = new Reserves();

        reserves.setUserDetails(userDetails);
        reserves.setSavedAmount(savedAmount);
        reserves.setUsable(true);
        return reserves;
    }

    public static Reserves saveByAdmin(UserDetails userDetails, int savedAmount) {
        Reserves reserves = new Reserves();

        reserves.setUserDetails(userDetails);
        reserves.setSavedAmount(savedAmount);
        reserves.setUsable(true);
        return reserves;
    }

    public void usePoint(boolean usable, int usedAmount) {
        setUsable(usable);
        setUsedAmount(usedAmount);
    }
}
