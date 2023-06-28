package com.muzisoft.division.web.service;

import com.muzisoft.division.domain.file.DirectoryManagerRepository;
import com.muzisoft.division.domain.file.EDirectoryManager;
import com.muzisoft.division.web.api.dto.directory.DirectoryCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DirectoryService {

    private final DirectoryManagerRepository directoryManagerRepository;

    @Transactional
    public void create(DirectoryCreateRequest request) {
        directoryManagerRepository.save(EDirectoryManager.create(request.getDiv(), request.getDirName(), request.getPath()));
    }
}
