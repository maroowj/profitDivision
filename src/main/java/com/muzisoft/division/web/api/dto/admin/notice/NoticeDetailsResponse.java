package com.muzisoft.division.web.api.dto.admin.notice;

import com.muzisoft.division.domain.board.Notice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDetailsResponse {

    private String noticeSeq;
    private String name;
    private String title;
    private String contents;
    private String attachUrl;
    private String attachName;
    private boolean fixed;

    public NoticeDetailsResponse(Notice notice, String attachUrl) {
        setNoticeSeq(notice.getSeq());
        setName(notice.getWriter().getName());
        setTitle(notice.getTitle());
        setContents(notice.getContents());
        setAttachUrl(attachUrl);
        if(attachUrl!=null && !attachUrl.equals("")){
            setAttachName(notice.getAttach().getOriginalName());
        }
        setFixed(notice.isFixed());
    }
}
