package com.muzisoft.division.domain.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DirectoryManagerRepository extends JpaRepository<EDirectoryManager, String> {
    Optional<EDirectoryManager> findByDirName(String dirName);
}
