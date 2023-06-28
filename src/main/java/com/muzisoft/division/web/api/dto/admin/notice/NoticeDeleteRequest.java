package com.muzisoft.division.web.api.dto.admin.notice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDeleteRequest {

    private List<String> noticeSeq;
}
