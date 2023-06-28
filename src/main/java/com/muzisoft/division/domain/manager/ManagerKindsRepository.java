package com.muzisoft.division.domain.manager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerKindsRepository extends JpaRepository<ManagerKinds, String> {

    List<ManagerKinds> findAllByUsableIsTrue();
}