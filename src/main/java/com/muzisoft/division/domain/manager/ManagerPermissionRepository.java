package com.muzisoft.division.domain.manager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerPermissionRepository extends JpaRepository<ManagerPermission, String> {
}