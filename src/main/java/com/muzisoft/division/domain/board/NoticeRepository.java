package com.muzisoft.division.domain.board;

import com.muzisoft.division.web.api.dto.admin.notice.NoticeListResponse;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, String>, NoticeQueryRepository {

    Optional<Notice> findTopByOrderByCreatedAtDesc();
}