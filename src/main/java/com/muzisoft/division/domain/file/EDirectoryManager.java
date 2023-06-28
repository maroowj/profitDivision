package com.muzisoft.division.domain.file;

import com.muzisoft.division.utils.manager.SeqManager;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter(value = AccessLevel.PROTECTED)
@Table(name = "directory_manager")
@Entity
public class EDirectoryManager {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_directory")
    @GenericGenerator(name = "seq_manager_directory", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.INCREMENT_PARAM, value = "1"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "dirt_"),
            @org.hibernate.annotations.Parameter(name = SeqManager.NUMBER_FORMAT_PARAMETER, value = "%010d"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_COLUMN_PARAM, value = "seq")
    })
    @Column(nullable = false, updatable = false, length = 15)
    @Id
    private String seq;

    @Column(length = 15)
    private String dirDiv;

    @Column(length = 15)
    private String dirName;

    @Column(length = 200)
    private String dirPath;

    @Column(length = 200)
    private String comment;

    public static EDirectoryManager create(String div, String dirName, String path) {
        EDirectoryManager directoryManager = new EDirectoryManager();
        directoryManager.setDirDiv(div);
        directoryManager.setDirName(dirName);
        directoryManager.setDirPath(path);
        return directoryManager;
    }

}