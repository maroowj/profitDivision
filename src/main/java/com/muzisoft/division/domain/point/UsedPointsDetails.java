package com.muzisoft.division.domain.point;

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
public class UsedPointsDetails extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_used_points_details")
    @GenericGenerator(name = "seq_manager_used_points_details", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.INCREMENT_PARAM, value = "1"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "ptdt_"),
            @org.hibernate.annotations.Parameter(name = SeqManager.NUMBER_FORMAT_PARAMETER, value = "%010d"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_COLUMN_PARAM, value = "seq")
    })
    @Column(nullable = false, updatable = false, length = 15)
    @Id
    private String seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "used_points_seq", nullable = false)
    private UsedPoints usedPoints;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_points_seq", nullable = false)
    private UserPoints userPoints;

    @Column(nullable = false)
    private int amount;

    public static UsedPointsDetails create(UsedPoints usedPoints, UserPoints userPoints, int amount) {
        UsedPointsDetails usedPointsDetails = new UsedPointsDetails();
        usedPointsDetails.setUsedPoints(usedPoints);
        usedPointsDetails.setUserPoints(userPoints);
        usedPointsDetails.setAmount(amount);
        return usedPointsDetails;
    }
}
