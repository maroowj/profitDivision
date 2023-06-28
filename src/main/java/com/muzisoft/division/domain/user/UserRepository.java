package com.muzisoft.division.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUserId(String userId);
    Optional<User> findByUserId(String userId);
    Optional<User> findBySnsIdAndProvider(String snsId, String provider);
    Optional<User> findTopByOrderBySeqDesc();
    Optional<User> findByUserDetails(UserDetails userDetails);
    Optional<User> findByUserIdAndUsername(String userId, String Username);

}