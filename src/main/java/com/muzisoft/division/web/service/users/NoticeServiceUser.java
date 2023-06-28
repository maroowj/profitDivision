package com.muzisoft.division.web.service.users;

import com.muzisoft.division.domain.board.Notice;
import com.muzisoft.division.domain.board.NoticeRepository;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.utils.handler.FileHandler;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.users.notice.NoticeCondition;
import com.muzisoft.division.web.api.dto.users.notice.NoticeDetailsForUserResponse;
import com.muzisoft.division.web.api.dto.users.notice.NoticeListForUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceUser {

    private final NoticeRepository noticeRepository;
    private final FileHandler fileHandler;

    @Transactional
    public NoticeListForUserResponse noticeListForUser(Pageable pageable, NoticeCondition noticeCondition, CommonCondition condition) {
        NoticeListForUserResponse result = new NoticeListForUserResponse();


        List<NoticeListForUserResponse.NoticeList> noticeList = noticeRepository.noticeListForUser();
        Page<NoticeListForUserResponse.NormalList> normalList = noticeRepository.normalListForUser(pageable, noticeCondition, condition);

        result.setNoticeList(noticeList);
        result.setNormalList(normalList);

        return result;
    }

    @Transactional
    public NoticeDetailsForUserResponse noticeDetailsForUser(String noticeSeq) {
        Notice notice = EntityUtils.noticeThrowable(noticeRepository, noticeSeq);

        String attachUrl = null;
        if(!ObjectUtils.isEmpty(notice.getAttach())) {
            attachUrl = fileHandler.fileUrl(notice.getAttach());
        }

        return new NoticeDetailsForUserResponse(notice, attachUrl);
    }
}
