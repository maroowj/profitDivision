package com.muzisoft.division.domain.user;

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
public class UserAccount extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_user_account")
    @GenericGenerator(name = "seq_manager_user_account", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.INCREMENT_PARAM, value = "1"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "usac_"),
            @org.hibernate.annotations.Parameter(name = SeqManager.NUMBER_FORMAT_PARAMETER, value = "%010d"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_COLUMN_PARAM, value = "seq")
    })
    @Column(nullable = false, updatable = false, length = 15)
    @Id
    private String seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_details_seq", nullable = false)
    private UserDetails userDetails;

    @Column(nullable = false, length = 20)
    private String bankName;

    @Column(nullable = false, length = 50)
    private String accountNumber;

    @Column(nullable = false, columnDefinition = "tinyint(1) default 1")
    private boolean used;

    public static UserAccount create(UserDetails userDetails, String bankName, String accountNumber) {
        UserAccount userAccount = new UserAccount();
        userAccount.setUserDetails(userDetails);
        userAccount.setBankName(bankName);
        userAccount.setAccountNumber(accountNumber);
        userAccount.setUsed(true);

        return userAccount;
    }

    public void update(String bankName, String accountNumber) {
        setBankName(bankName);
        setAccountNumber(accountNumber);
    }
}
