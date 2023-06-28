package com.muzisoft.division.domain.file;

import com.muzisoft.division.domain.base.BaseTimeEntity;
import com.muzisoft.division.domain.listener.FileListener;
import com.muzisoft.division.utils.manager.SeqManager;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter(value = AccessLevel.PROTECTED)
@Table(name = "file_manager")
@Entity
@EntityListeners(FileListener.class)
public class EFileManager extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_file")
    @GenericGenerator(name = "seq_manager_file", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.INCREMENT_PARAM, value = "1"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "file_"),
            @org.hibernate.annotations.Parameter(name = SeqManager.NUMBER_FORMAT_PARAMETER, value = "%010d"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_COLUMN_PARAM, value = "seq")
    })
    @Column(nullable = false, updatable = false, length = 15)
    @Id
    private String seq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "directory_seq")
    private EDirectoryManager directoryManager;

    @Column(nullable = false, length = 100)
    private String fileName;

    @Column(nullable = false, length = 100)
    private String originalName;

    @Column(length = 10)
    private String extension;

    @Column
    private int fileSize;

    @Column(length = 200)
    private String comment;

    public EFileManager(EDirectoryManager directoryManager, String fileName, String originName, String fileExtension,
                        int fileSize, String etc) {
        this.directoryManager = directoryManager;
        this.fileName = fileName;
        this.originalName = originName;
        this.extension = fileExtension;
        this.fileSize = fileSize;
        this.comment = etc;
    }

    public void replaceWith(EFileManager fileManager) {
        setFileName(fileManager.getFileName());
        setDirectoryManager(fileManager.getDirectoryManager());
        setOriginalName(fileManager.getOriginalName());
        setExtension(fileManager.getExtension());
        setFileSize(fileManager.getFileSize());
        setComment(fileManager.getComment());
    }
}