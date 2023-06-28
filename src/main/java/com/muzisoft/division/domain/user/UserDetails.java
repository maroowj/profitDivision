package com.muzisoft.division.domain.user;

import com.muzisoft.division.domain.base.BaseTimeEntity;
import com.muzisoft.division.domain.file.EFileManager;
import com.muzisoft.division.utils.manager.SeqManager;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@DynamicInsert
@DynamicUpdate
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table
public class UserDetails extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_user_details")
    @GenericGenerator(name = "seq_manager_user_details", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.INCREMENT_PARAM, value = "1"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "usdt_"),
            @org.hibernate.annotations.Parameter(name = SeqManager.NUMBER_FORMAT_PARAMETER, value = "%010d"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_COLUMN_PARAM, value = "seq")
    })
    @Column(nullable = false, updatable = false, length = 15)
    @Id
    private String seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_seq", nullable = false)
    private Grade grade;

    @Column(nullable = false, columnDefinition = "char(4)")
    private String membershipNumber;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(length = 20)
    private String birth;

    @Column(length = 20)
    private String mobile;

    @Column(length = 100)
    private String email;

    @Column
    private String address;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image")
    private EFileManager image;

    @Column(nullable = false, columnDefinition = "char(5)")
    private String recommendCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommender")
    private User recommender;

    @Column(nullable = false, columnDefinition = "tinyint(1) default 0")
    private int accepted;

    @Column(nullable = false, columnDefinition = "tinyint(1)")
    private int cycle;

    @Column(nullable = false, columnDefinition = "tinyint(1)")
    private boolean receiveType;

    @Column(nullable = false)
    private long investment;

    public static UserDetails create(String name, String email, String recommendCode, String membershipNumber, Grade grade) {
        UserDetails userDetails = new UserDetails();
        userDetails.setName(name);
        userDetails.setEmail(email);
        userDetails.setRecommendCode(recommendCode);
        userDetails.setMembershipNumber(membershipNumber);
        userDetails.setGrade(grade);
        userDetails.setInvestment(0);
        return userDetails;
    }

    public static UserDetails createDirect(String name, String birth, String mobile, String email, String address,
                                           User recommender, String recommendCode, String membershipNumber, Grade grade) {
        UserDetails userDetails = new UserDetails();
        userDetails.setName(name);
        userDetails.setBirth(birth);
        userDetails.setMobile(mobile);
        userDetails.setEmail(email);
        userDetails.setAddress(address);
        userDetails.setRecommender(recommender);
        userDetails.setRecommendCode(recommendCode);
        userDetails.setMembershipNumber(membershipNumber);
        userDetails.setGrade(grade);
        userDetails.setInvestment(0);
        return userDetails;
    }

    public static UserDetails createManager(String name, String mobile, String recommendCode, String membershipNumber, Grade grade) {
        UserDetails userDetails = new UserDetails();
        userDetails.setName(name);
        userDetails.setMobile(mobile);
        userDetails.setRecommendCode(recommendCode);
        userDetails.setMembershipNumber(membershipNumber);
        userDetails.setGrade(grade);
        userDetails.setAccepted(1);
        userDetails.setInvestment(0);
        return userDetails;
    }

    public void updateManager(String name, String mobile) {
        setName(name);
        setMobile(mobile);
    }

    public void updateInvestment(long investment) {
        setInvestment(investment);
    }

    public void gradeUpdate(Grade grade) {
        setGrade(grade);
    }

    public void approval(int accepted) {
        setAccepted(accepted);
    }

    public void updateInfo(String name, String birth, String mobile, String email, String address) {
        setName(name);
        setBirth(birth);
        setMobile(mobile);
        setEmail(email);
        setAddress(address);
    }
}
