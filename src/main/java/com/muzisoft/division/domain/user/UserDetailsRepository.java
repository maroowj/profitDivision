package com.muzisoft.division.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, String>, UserDetailsQueryRepository {

    Optional<UserDetails> findByRecommendCode(String recommendCode);
    Optional<UserDetails> findTopByOrderByMembershipNumberDesc();

    Optional<UserDetails> findByNameAndMobile(String name, String mobile);
}