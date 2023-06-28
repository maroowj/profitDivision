package com.muzisoft.division.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FaqRepository extends JpaRepository<Faq, String>, FaqQueryRepository {

    Optional<Faq> findTopByOrderByCreatedAtDesc();
}