package com.muzisoft.division.web.api.dto.users.notice;

import com.muzisoft.division.domain.board.Notice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDetailsForUserResponse {

    private String title;
    private String contents;
    private boolean fixed;
    private String type;
    private String attachUrl;
    private String attachName;
    private String createdAt;

    public NoticeDetailsForUserResponse(Notice notice, String attachUrl) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        setTitle(notice.getTitle());
        setContents(notice.getContents());
        setFixed(notice.isFixed());
        if(notice.isFixed()) {
            setType("공지사항");
        }else {
            setType("일반");
        }
        setAttachUrl(attachUrl);
        if(attachUrl!=null && !attachUrl.equals("")){
            setAttachName(notice.getAttach().getOriginalName());
        }
        setCreatedAt(sdf.format(notice.getCreatedAt()));
    }
}
