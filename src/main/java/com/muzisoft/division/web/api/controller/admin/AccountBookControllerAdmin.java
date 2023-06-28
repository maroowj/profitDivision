package com.muzisoft.division.web.api.controller.admin;

import com.muzisoft.division.web.api.dto.admin.accountBook.AccountBookDetailsResponse;
import com.muzisoft.division.web.api.dto.admin.accountBook.AccountBookSaveAndUpdateRequest;
import com.muzisoft.division.web.service.admin.AccountBookServiceAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/accounts")
public class AccountBookControllerAdmin {

    private final AccountBookServiceAdmin accountBookServiceAdmin;

    @GetMapping("/{year}")
    public AccountBookDetailsResponse details(@PathVariable("year") int year) {
        return accountBookServiceAdmin.details(year);
    }

    @PostMapping
    public void saveOrUpdate(@RequestBody AccountBookSaveAndUpdateRequest request) {
        accountBookServiceAdmin.saveOrUpdate(request);
    }
}
