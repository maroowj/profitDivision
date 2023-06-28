package com.muzisoft.division.web.api.dto.users.board;

import com.muzisoft.division.domain.board.Board;
import com.muzisoft.division.domain.board.Notice;
import com.muzisoft.division.web.api.dto.admin.board.BoardDetailsResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDetailsForUserResponse {

    private String title;
    private String contents;
    private boolean fixed;
    private String type;
    private String attachUrl;
    private String attachName;
    private String createdAt;

    public BoardDetailsForUserResponse(Board board, String attachUrl) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        setTitle(board.getTitle());
        setContents(board.getContents());
        setFixed(board.isFixed());
        if(board.isFixed()) {
            setType("공지사항");
        }else {
            setType("일반");
        }
        setAttachUrl(attachUrl);
        if(attachUrl!=null && !attachUrl.equals("")){
            setAttachName(board.getAttach().getOriginalName());
        }
        setCreatedAt(sdf.format(board.getCreatedAt()));
    }
}
