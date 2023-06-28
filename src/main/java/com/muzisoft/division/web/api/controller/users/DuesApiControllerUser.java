package com.muzisoft.division.web.api.controller.users;

import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.users.dues.DuesConfirmRequest;
import com.muzisoft.division.web.api.dto.users.dues.DuesInformationResponse;
import com.muzisoft.division.web.api.dto.users.dues.NotPaidDuesResponse;
import com.muzisoft.division.web.api.dto.users.dues.UserDuesListResponse;
import com.muzisoft.division.web.service.users.DuesServiceUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user/dues")
@RequiredArgsConstructor
public class DuesApiControllerUser {

    private final DuesServiceUser duesServiceUser;

    @GetMapping("/information")
    public DuesInformationResponse duesInformation() {
        return duesServiceUser.duesInformation();
    }

    @GetMapping("/payment-state")
    public List<NotPaidDuesResponse> duesPaymentState() {
        return duesServiceUser.notPaidDues();
    }

    @GetMapping
    public Page<UserDuesListResponse> userDuesList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 20) Pageable pageable,
                                                   CommonCondition condition) {
        return duesServiceUser.userDuesList(pageable, condition);
    }

    @PutMapping
    public void requestConfirm(@RequestBody DuesConfirmRequest request) {
        duesServiceUser.requestConfirm(request);
    }
}
