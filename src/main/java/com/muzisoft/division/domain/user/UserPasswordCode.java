package com.muzisoft.division.domain.user;

import com.muzisoft.division.domain.base.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@DynamicInsert
@DynamicUpdate
@Entity
@Getter
@Setter(value = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table
public class UserPasswordCode extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    @Id
    private int seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, columnDefinition = "tinyint(1) default 0")
    private boolean used;

    @Column(nullable = false)
    private Date expireTime;

    public static UserPasswordCode create(User user, String code) {
        UserPasswordCode userPasswordCode = new UserPasswordCode();
        Date today = new Date();
        Date expireTime = new Date();
        expireTime.setDate(today.getDate()+7);

        userPasswordCode.setUser(user);
        userPasswordCode.setCode(code);
        userPasswordCode.setUsed(false);
        userPasswordCode.setExpireTime(expireTime);
        return userPasswordCode;
    }

    public void updateUsed() {
        setUsed(true);
    }
}
