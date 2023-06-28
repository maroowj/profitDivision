package com.muzisoft.division.web.api.dto.users.notice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeListForUserResponse {

    private List<NoticeList> noticeList;
    private Page<NormalList> normalList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoticeList {
        private String noticeSeq;
        private int noticeNo;
        private boolean fixed;
        private String title;
        private String createdAt;
        private String type;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NormalList {
        private String noticeSeq;
        private int noticeNo;
        private boolean fixed;
        private String title;
        private String createdAt;
        private String type;
    }
}
