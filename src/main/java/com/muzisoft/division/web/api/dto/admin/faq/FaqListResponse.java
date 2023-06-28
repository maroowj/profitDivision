package com.muzisoft.division.web.api.dto.admin.faq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaqListResponse {

    private int faqNo;
    private String faqSeq;
    private String title;
    private String name;
    private String nickname;
    private String createdAt;
}
