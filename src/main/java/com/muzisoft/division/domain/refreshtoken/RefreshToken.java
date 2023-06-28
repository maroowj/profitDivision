package com.muzisoft.division.domain.refreshtoken;

import com.muzisoft.division.domain.base.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@DynamicInsert
@DynamicUpdate
@Entity
@Getter @Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "token_key", nullable = false, updatable = false)
    private String key;

    @Column(nullable = false)
    private String token;

    public void update(String token) {
        this.token = token;
    }

    public static RefreshToken create(String key, String token) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setKey(key);
        refreshToken.setToken(token);
        return refreshToken;
    }
}
