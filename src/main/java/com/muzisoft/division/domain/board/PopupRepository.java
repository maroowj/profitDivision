package com.muzisoft.division.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PopupRepository extends JpaRepository<Popup, String>, PopupQueryRepository {

    List<Popup> findAllByExposureOrderByCreatedAtDesc(boolean exposure);
}