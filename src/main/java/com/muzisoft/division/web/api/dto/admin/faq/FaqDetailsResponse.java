package com.muzisoft.division.web.api.dto.admin.faq;

import com.muzisoft.division.domain.board.Faq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaqDetailsResponse {

    private String faqSeq;
    private String name;
    private String title;
    private String contents;
    private String attachUrl;
    private String attachName;
    private boolean fixed;

    public FaqDetailsResponse(Faq faq, String attachUrl) {
        setFaqSeq(faq.getSeq());
        setName(faq.getWriter().getName());
        setTitle(faq.getTitle());
        setContents(faq.getContents());
        setAttachUrl(attachUrl);
        if(attachUrl!=null && !attachUrl.equals("")){
            setAttachName(faq.getAttach().getOriginalName());
        }
        setFixed(faq.isFixed());
    }
}
