package com.muzisoft.division.web.service.admin;

import com.muzisoft.division.domain.board.Board;
import com.muzisoft.division.domain.board.BoardRepository;
import com.muzisoft.division.domain.board.Faq;
import com.muzisoft.division.domain.board.FaqRepository;
import com.muzisoft.division.domain.file.EFileManager;
import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.domain.user.UserDetailsRepository;
import com.muzisoft.division.domain.user.UserRepository;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.utils.handler.FileHandler;
import com.muzisoft.division.web.api.dto.admin.board.*;
import com.muzisoft.division.web.api.dto.admin.faq.*;
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
public class FaqServiceAdmin {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final FaqRepository faqRepository;
    private final FileHandler fileHandler;

    @Transactional
    public void createFaq(FaqSaveRequest request, EFileManager attach) {
        User securityUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = EntityUtils.userThrowable(userRepository, securityUser.getSeq());
        UserDetails userDetails = user.getUserDetails();

        Optional<Faq> recentFaq = faqRepository.findTopByOrderByCreatedAtDesc();

        int noticeNo = 1;
        if(recentFaq.isPresent()) {
            noticeNo = recentFaq.get().getFaqNo() + 1;
        }

        boolean fixed = false;
        if(request.getFixed().equals("true")) {
            fixed = true;
        }else {
            fixed = false;
        }

        faqRepository.save(
                Faq.create(
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
    public Page<FaqListResponse> faqList(Pageable pageable, CommonCondition condition) {
        Page<FaqListResponse> result = faqRepository.faqList(pageable, condition);

        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int totalCount = (int) result.getTotalElements();

        int rowNum = (pageSize * pageNumber) + 1;

        int dec = pageSize * pageNumber;

        for(FaqListResponse response : result.getContent()) {
            response.setFaqNo(totalCount - dec);
            dec++;
        }

        return result;
    }

    @Transactional
    public FaqDetailsResponse faqDetails(String faqSeq) {
        Faq faq = EntityUtils.faqThrowable(faqRepository, faqSeq);

        String attachUrl = null;
        if(!ObjectUtils.isEmpty(faq.getAttach())) {
            attachUrl = fileHandler.fileUrl(faq.getAttach());
        }
        return new FaqDetailsResponse(faq, attachUrl);
    }

    @Transactional
    public void updateFaq(String faqSeq, FaqUpdateRequest request, EFileManager attach) {
        Faq faq = EntityUtils.faqThrowable(faqRepository, faqSeq);
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

        faq.update(
                userDetails,
                request.getTitle(),
                request.getContents(),
                fixed,
                attach,
                request.isAttachPresent()
        );
    }

    @Transactional
    public void deleteFaq(FaqDeleteRequest request) {
        for(String faqSeq : request.getFaqSeq()) {
            Faq faq = EntityUtils.faqThrowable(faqRepository, faqSeq);
            faqRepository.delete(faq);
        }
    }
}
