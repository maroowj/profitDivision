package com.muzisoft.division.web.service.admin;

import com.muzisoft.division.domain.board.Board;
import com.muzisoft.division.domain.board.BoardRepository;
import com.muzisoft.division.domain.board.Notice;
import com.muzisoft.division.domain.board.NoticeRepository;
import com.muzisoft.division.domain.file.EFileManager;
import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.domain.user.UserDetailsRepository;
import com.muzisoft.division.domain.user.UserRepository;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.utils.handler.FileHandler;
import com.muzisoft.division.web.api.dto.admin.board.*;
import com.muzisoft.division.web.api.dto.admin.notice.*;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceAdmin {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final BoardRepository boardRepository;
    private final FileHandler fileHandler;

    @Transactional
    public void createBoard(BoardSaveRequest request, EFileManager attach) {
        User securityUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = EntityUtils.userThrowable(userRepository, securityUser.getSeq());
        UserDetails userDetails = user.getUserDetails();

        Optional<Board> recentBoard = boardRepository.findTopByOrderByCreatedAtDesc();

        int noticeNo = 1;
        if(recentBoard.isPresent()) {
            noticeNo = recentBoard.get().getBoardNo() + 1;
        }

        boolean fixed = false;
        if(request.getFixed().equals("true")) {
            fixed = true;
        }else {
            fixed = false;
        }

        boardRepository.save(
                Board.create(
                        userDetails,
                        request.getTitle(),
                        request.getContents(),
                        fixed,
                        attach,
                        noticeNo
                )
        );
    }

    @Transactional
    public Page<BoardListResponse> boardList(Pageable pageable, CommonCondition condition) {
        Page<BoardListResponse> result = boardRepository.boardList(pageable, condition);

//        int pageSize = pageable.getPageSize();
//        int pageNumber = pageable.getPageNumber();
//        int totalCount = (int) result.getTotalElements();
//
//        int rowNum = (pageSize * pageNumber) + 1;
//
//        int dec = pageSize * pageNumber;
//
//        for(NoticeListResponse response : result.getContent()) {
//            response.setRowNum(totalCount - dec);
//            dec++;
//        }

        return result;
    }

    @Transactional
    public BoardDetailsResponse boardDetails(String boardSeq) {
        Board board = EntityUtils.boardThrowable(boardRepository, boardSeq);

        String attachUrl = null;
        if(!ObjectUtils.isEmpty(board.getAttach())) {
            attachUrl = fileHandler.fileUrl(board.getAttach());
        }
        return new BoardDetailsResponse(board, attachUrl);
    }

    @Transactional
    public void updateBoard(String boardSeq, BoardUpdateRequest request, EFileManager attach) {
        Board board = EntityUtils.boardThrowable(boardRepository, boardSeq);
//        UserDetails writer = EntityUtils.userDetailsThrowable(userDetailsRepository, request.getUserDetailsSeq());
        User securityUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = EntityUtils.userThrowable(userRepository, securityUser.getSeq());
        UserDetails userDetails = user.getUserDetails();

        boolean fixed = false;
        if(request.getFixed().equals("true")) {
            fixed = true;
        }else {
            fixed = false;
        }

        board.update(
                userDetails,
                request.getTitle(),
                request.getContents(),
                fixed,
                attach,
                request.isAttachPresent()
        );
    }

    @Transactional
    public void deleteBoard(BoardDeleteRequest request) {
        for(String noticeSeq : request.getBoardSeq()) {
            Board board = EntityUtils.boardThrowable(boardRepository, noticeSeq);
            boardRepository.delete(board);
        }
    }
}
