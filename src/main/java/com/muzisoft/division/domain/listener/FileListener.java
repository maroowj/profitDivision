package com.muzisoft.division.domain.listener;

import com.muzisoft.division.domain.file.EFileManager;
import com.muzisoft.division.utils.FileManagerUtils;

import javax.persistence.PreRemove;


public class FileListener {

    @PreRemove
    public void preRemove(EFileManager fileManager) {
        FileManagerUtils.delete(fileManager);
    }
}
