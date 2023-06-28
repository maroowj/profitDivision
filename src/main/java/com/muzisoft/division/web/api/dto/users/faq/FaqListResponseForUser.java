package com.muzisoft.division.web.api.dto.users.faq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaqListResponseForUser {

    private String title;
    private String contents;
}
