package com.muzisoft.division.domain.user;

import com.muzisoft.division.domain.base.BaseTimeEntity;
import com.muzisoft.division.domain.common.enums.Role;
import com.muzisoft.division.utils.manager.SeqManager;
import com.mysql.cj.protocol.ColumnDefinition;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@DynamicInsert
@DynamicUpdate
@Entity
@Getter @Setter(value = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USERS")
public class User extends BaseTimeEntity implements UserDetails {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_user")
    @GenericGenerator(name = "seq_manager_user", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.INCREMENT_PARAM, value = "1"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "user_"),
            @org.hibernate.annotations.Parameter(name = SeqManager.NUMBER_FORMAT_PARAMETER, value = "%010d"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_COLUMN_PARAM, value = "seq")
    })
    @Column(nullable = false, updatable = false, length = 15)
    @Id
    private String seq;

    @OneToOne(fetch = LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(nullable = false, name = "userdetails_seq")
    private com.muzisoft.division.domain.user.UserDetails userDetails;

    @Column(nullable = false, unique = true, length = 200)
    private String userId;

    @Column(nullable = false, length = 200)
    private String password;

    @Column(nullable = false, length = 20)
    private String username;

    @ElementCollection(fetch = EAGER)
    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

//    @Convert(converter = LoginTypeConverter.class)
//    @Column(nullable = false, columnDefinition = "char(1)")
//    private LoginType loginType;

    private String provider;

    @Column(length = 20)
    private String snsId;

    @Column(columnDefinition = "tinyint(1) default 0")
    private boolean withdrawal;

    @Column(length = 255)
    private String withdrawalReason;

    public static User create(String id, String password, String username, String email, String recommendCode, String membershipNumber, Grade grade, String snsId, String provider) {
        User user = new User();
        user.setUserId(id);
        user.setPassword(password);
        user.setUsername(username);
        user.setSnsId(snsId);
        user.setProvider(provider);
        user.setUserDetails(com.muzisoft.division.domain.user.UserDetails.create(username, email, recommendCode, membershipNumber, grade));
        user.setRoles(Collections.singletonList(Role.USER));
        return user;
    }

    public static User createDirect(String id, String password, String provider, String username,
                                    String birth, String mobile, String email, String address,
                                    User recommender, String recommendCode, String membershipNumber, Grade grade) {
        User user = new User();
        user.setUserId(id);
        user.setPassword(password);
        user.setProvider(provider);
        user.setUsername(username);
        user.setUserDetails(com.muzisoft.division.domain.user.UserDetails.createDirect(username, birth, mobile, email, address, recommender, recommendCode, membershipNumber, grade));
        user.setRoles(Collections.singletonList(Role.USER));
        return user;
    }

    public static User createManager(String id, String password, String username, String mobile,
                                     String recommendCode, String membershipNumber, Grade grade) {
        User user = new User();
        user.setUserId(id);
        user.setPassword(password);
        user.setUsername(username);
        user.setUserDetails(com.muzisoft.division.domain.user.UserDetails.createManager(username, mobile, recommendCode, membershipNumber, grade));
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ADMIN);
        roles.add(Role.USER);
        user.setRoles(roles);
        return user;
    }

    public void updateManager(String password, String username) {
        if(password!=null && !password.equals("")) {
            setPassword(password);
        }
        setUsername(username);
    }

   /* public static User createSocial(String id, String password, String provider, String snsId) {
        User user = create(id, password, provider);
        user.setSnsId(snsId);
        return user;
    }*/

    public void changePassword(String passwordToChange) {
        setPassword(passwordToChange);
    }

    public void withdrawal(String withdrawalReason) {
        setWithdrawal(true);
        setWithdrawalReason(withdrawalReason);
    }

    public void cancelWithdrawal() {
        setWithdrawal(false);
        setWithdrawalReason(null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream().map((role) -> new SimpleGrantedAuthority(role.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
