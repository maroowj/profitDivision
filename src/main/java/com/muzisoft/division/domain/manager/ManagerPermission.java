package com.muzisoft.division.domain.manager;

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
public class ManagerPermission {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_manager_permission")
    @GenericGenerator(name = "seq_manager_manager_permission", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.INCREMENT_PARAM, value = "1"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "mgpm_"),
            @org.hibernate.annotations.Parameter(name = SeqManager.NUMBER_FORMAT_PARAMETER, value = "%010d"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_COLUMN_PARAM, value = "seq")
    })
    @Column(nullable = false, updatable = false, length = 15)
    @Id
    private String seq;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, length = 50)
    private String url;

    public static ManagerPermission create(String title, String url) {
        ManagerPermission managerPermission = new ManagerPermission();

        managerPermission.setTitle(title);
        managerPermission.setUrl(url);
        return managerPermission;
    }
}
