package com.muzisoft.division.web.service.admin;

import com.muzisoft.division.domain.board.Notice;
import com.muzisoft.division.domain.board.NoticeRepository;
import com.muzisoft.division.domain.file.EFileManager;
import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.domain.user.UserDetailsRepository;
import com.muzisoft.division.domain.user.UserRepository;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.utils.handler.FileHandler;
import com.muzisoft.division.web.api.dto.admin.notice.*;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceAdmin {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final NoticeRepository noticeRepository;
    private final FileHandler fileHandler;

    @Transactional
    public void createNotice(NoticeSaveRequest request, EFileManager attach) {
        User securityUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = EntityUtils.userThrowable(userRepository, securityUser.getSeq());
        UserDetails userDetails = user.getUserDetails();

        Optional<Notice> recentNotice = noticeRepository.findTopByOrderByCreatedAtDesc();

        int noticeNo = 1;
        if(recentNotice.isPresent()) {
            noticeNo = recentNotice.get().getNoticeNo() + 1;
        }

        boolean fixed = false;
        if(request.getFixed().equals("true")) {
            fixed = true;
        }else {
            fixed = false;
        }

        noticeRepository.save(
                Notice.create(
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
    public Page<NoticeListResponse> noticeList(Pageable pageable, CommonCondition condition) {
        Page<NoticeListResponse> result = noticeRepository.noticeList(pageable, condition);

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
    public NoticeDetailsResponse noticeDetails(String noticeSeq) {
        Notice notice = EntityUtils.noticeThrowable(noticeRepository, noticeSeq);

        String attachUrl = null;
        if(!ObjectUtils.isEmpty(notice.getAttach())) {
            attachUrl = fileHandler.fileUrl(notice.getAttach());
        }
        return new NoticeDetailsResponse(notice, attachUrl);
    }

    @Transactional
    public void updateNotice(String noticeSeq, NoticeUpdateRequest request, EFileManager attach) {
        Notice notice = EntityUtils.noticeThrowable(noticeRepository, noticeSeq);
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

        notice.update(
                userDetails,
                request.getTitle(),
                request.getContents(),
                fixed,
                attach,
                request.isAttachPresent()
        );
    }

    @Transactional
    public void deleteNotice(NoticeDeleteRequest request) {
        for(String noticeSeq : request.getNoticeSeq()) {
            Notice notice = EntityUtils.noticeThrowable(noticeRepository, noticeSeq);
            noticeRepository.delete(notice);
        }
    }
}
