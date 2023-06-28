package com.muzisoft.division.domain.manager;

import com.muzisoft.division.domain.base.BaseTimeEntity;
import com.muzisoft.division.domain.file.EFileManager;
import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.utils.FileManagerUtils;
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
public class ManagerUsers extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_manager_users")
    @GenericGenerator(name = "seq_manager_manager_users", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.INCREMENT_PARAM, value = "1"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "mgus_"),
            @org.hibernate.annotations.Parameter(name = SeqManager.NUMBER_FORMAT_PARAMETER, value = "%010d"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_COLUMN_PARAM, value = "seq")
    })
    @Column(nullable = false, updatable = false, length = 15)
    @Id
    private String seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    @Column(length = 50)
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_kinds_seq")
    private ManagerKinds kinds;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profilePicture")
    private EFileManager profilePicture;

    @Column(columnDefinition = "tinyint default 0")
    private boolean deleted;

    public static ManagerUsers create(User user, String nickname, ManagerKinds managerKinds, EFileManager profilePicture) {
        ManagerUsers managerUsers = new ManagerUsers();

        managerUsers.setUser(user);
        managerUsers.setNickname(nickname);
        managerUsers.setKinds(managerKinds);
        managerUsers.setProfilePicture(profilePicture);
        return managerUsers;
    }

    public void update(String nickname, ManagerKinds managerKinds, EFileManager profilePicture, boolean defaultImage) {
        if(!FileManagerUtils.equals(getProfilePicture(), profilePicture)) {
            if(profilePicture!=null) {
                setProfilePicture(profilePicture);
            }else {
                if(defaultImage==true) {
                    setProfilePicture(null);
                }
            }
        }else {
            FileManagerUtils.delete(profilePicture);
        }
        setNickname(nickname);
        setKinds(managerKinds);
    }

    public void delete() {
        setDeleted(true);
        setProfilePicture(null);
    }

    public void updateGrade(ManagerKinds managerKinds) {
        setKinds(managerKinds);
    }

    public void deleteManagerKinds() {
        setKinds(null);
    }
}
