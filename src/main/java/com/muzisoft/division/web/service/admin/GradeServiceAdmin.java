package com.muzisoft.division.web.service.admin;

import com.muzisoft.division.domain.user.Grade;
import com.muzisoft.division.domain.user.GradeRepository;
import com.muzisoft.division.domain.user.UserDetailsRepository;
import com.muzisoft.division.web.api.dto.admin.member.GradeListResponse;
import com.muzisoft.division.web.api.dto.admin.member.GradeSaveAndUpdateRequest;
import com.muzisoft.division.web.exception.business.CEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GradeServiceAdmin {

    private final GradeRepository gradeRepository;
    private final UserDetailsRepository userDetailsRepository;

    // 회원 등급 목록
    @Transactional
    public List<GradeListResponse> gradeList() {
        List<Grade> grades = gradeRepository.findAll();
        List<GradeListResponse> gradeList = new ArrayList<>();
        int rowNum = 1;
        for(Grade grade : grades) {
            int count = userDetailsRepository.countByGrade(grade);
            GradeListResponse response = new GradeListResponse(grade, count);
            response.setRowNum(rowNum);
            gradeList.add(response);
            rowNum++;
        }
        return gradeList;
    }

    // 회원 등급 수정
    @Transactional
    public void update(int seq, GradeSaveAndUpdateRequest request) {
        Grade foundGrade = gradeRepository.findById(seq).orElseThrow(CEntityNotFoundException.CGradeNotFoundException::new);
        foundGrade.update(
                request.getTitle(),
                request.getPercent(),
                request.getComment()
            );
    }

    // 회원 등급 추가 (실질적으로 사용하지 않음)
    @Transactional
    public void create(GradeSaveAndUpdateRequest request) {
        gradeRepository.save(
            Grade.create(request.getTitle(), request.getPercent(), request.getComment())
        );
    }


}
