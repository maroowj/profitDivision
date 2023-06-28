package com.muzisoft.division.web.api.dto.admin.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardListResponse {

    private int noticeNo;
    private String noticeSeq;
    private String title;
    private String name;
    private String nickname;
    private String createdAt;
}
