package com.muzisoft.division.web.api.controller.users;

import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.users.faq.FaqListResponseForUser;
import com.muzisoft.division.web.service.users.FaqServiceUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/common/faq")
public class FaqApiControllerUser {

    private final FaqServiceUser faqServiceUser;

    @GetMapping
    public Page<FaqListResponseForUser> faqListResponseForUsers(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable,
                                                                CommonCondition condition) {
        return faqServiceUser.faqListResponseForUsers(pageable, condition);
    }
}
