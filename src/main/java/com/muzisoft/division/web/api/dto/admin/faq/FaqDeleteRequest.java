package com.muzisoft.division.web.api.dto.admin.faq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaqDeleteRequest {

    private List<String> faqSeq;
}
