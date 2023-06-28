package com.muzisoft.division.web.api.controller.users;

import com.muzisoft.division.web.api.dto.admin.accountBook.AccountBookDetailsResponse;
import com.muzisoft.division.web.service.admin.AccountBookServiceAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/common/accounts")
@RequiredArgsConstructor
public class AccountBookApiControllerUser {

    private final AccountBookServiceAdmin accountBookServiceAdmin;

    @GetMapping("/{year}")
    public AccountBookDetailsResponse details(@PathVariable("year") int year) {
        return accountBookServiceAdmin.details(year);
    }
}
