package com.muzisoft.division.web.api.controller.users;

import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.users.notice.NoticeCondition;
import com.muzisoft.division.web.api.dto.users.notice.NoticeDetailsForUserResponse;
import com.muzisoft.division.web.api.dto.users.notice.NoticeListForUserResponse;
import com.muzisoft.division.web.service.users.NoticeServiceUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/common/notices")
@RequiredArgsConstructor
public class NoticeApiControllerUser {

    private final NoticeServiceUser noticeServiceUser;

    @GetMapping
    public NoticeListForUserResponse noticeListForUser(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 20)Pageable pageable,
                                                             NoticeCondition noticeCondition, CommonCondition condition) {
        return noticeServiceUser.noticeListForUser(pageable, noticeCondition, condition);
    }

    @GetMapping("/{noticeSeq}")
    public NoticeDetailsForUserResponse noticeDetailsForUser(@PathVariable("noticeSeq") String noticeSeq) {
        return noticeServiceUser.noticeDetailsForUser(noticeSeq);
    }
}
