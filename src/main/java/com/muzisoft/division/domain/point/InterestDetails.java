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

@DynamicInsert
@DynamicUpdate
@Entity
@Getter
@Setter(value = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table
public class InterestDetails extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_interest_details")
    @GenericGenerator(name = "seq_manager_interest_details", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "itdt_"),
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
    @JoinColumn(name = "interest_seq", nullable = false)
    private Interest interest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_details_seq", nullable = false)
    private UserDetails userDetails;

    @Column(nullable = false)
    private long total;

    @Column(nullable = false)
    private long amount;

    public static InterestDetails create(Interest interest, UserDetails userDetails, long total, long amount) {
        InterestDetails interestDetails = new InterestDetails();

        interestDetails.setInterest(interest);
        interestDetails.setUserDetails(userDetails);
        interestDetails.setTotal(total);
        interestDetails.setAmount(amount);
        return interestDetails;
    }
}
