package com.muzisoft.division.web.service.users;

import com.muzisoft.division.domain.board.FaqRepository;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.users.faq.FaqListResponseForUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FaqServiceUser {

    private final FaqRepository faqRepository;

    public Page<FaqListResponseForUser> faqListResponseForUsers(Pageable pageable, CommonCondition condition) {
        return faqRepository.faqListForUser(pageable, condition);
    }
}
