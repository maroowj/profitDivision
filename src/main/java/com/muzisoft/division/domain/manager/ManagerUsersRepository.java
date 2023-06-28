package com.muzisoft.division.domain.manager;

import com.muzisoft.division.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManagerUsersRepository extends JpaRepository<ManagerUsers, String> {

    List<ManagerUsers> findAllByDeletedIsFalse();

    Optional<ManagerUsers> findByUser(User user);
    List<ManagerUsers> findByKinds(ManagerKinds managerKinds);
}