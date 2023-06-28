package com.muzisoft.division.web.api.dto.admin.member;

import com.muzisoft.division.domain.user.Grade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeListResponse {

    private int rowNum;
    private int seq;
    private String title;
    private float percent;
    private String comment;
    private int count;

    public GradeListResponse(Grade grade, int count) {
        this.seq = grade.getSeq();
        this.title = grade.getTitle();
        this.percent = grade.getPercent();
        this.comment = grade.getComment();
        this.count = count;
    }
}
