package com.muzisoft.division.domain.manager;

import com.muzisoft.division.domain.base.BaseTimeEntity;
import com.muzisoft.division.utils.manager.SeqManager;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.util.List;

@DynamicInsert
@DynamicUpdate
@Entity
@Getter
@Setter(value = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class ManagerKinds extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_manager_kinds")
    @GenericGenerator(name = "seq_manager_manager_kinds", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.INCREMENT_PARAM, value = "1"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "mgkd_"),
            @org.hibernate.annotations.Parameter(name = SeqManager.NUMBER_FORMAT_PARAMETER, value = "%010d"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_COLUMN_PARAM, value = "seq")
    })
    @Column(nullable = false, updatable = false, length = 15)
    @Id
    private String seq;

    @Column(nullable = false, length = 50)
    private String name;

    @Type(type = "json")
    @Column(columnDefinition = "JSON")
    private List<String> abilities;

    @Column(nullable = false, columnDefinition = "tinyint(1) default 1")
    private boolean usable;

    public static ManagerKinds create(String name, List<String> abilities) {
        ManagerKinds managerKinds = new ManagerKinds();

        managerKinds.setName(name);
        managerKinds.setAbilities(abilities);
        managerKinds.setUsable(true);
        return managerKinds;
    }

    public void update(String name, List<String> abilities) {
        setName(name);
        setAbilities(abilities);
    }

    public void unUsed() {
        setUsable(false);
    }
}
