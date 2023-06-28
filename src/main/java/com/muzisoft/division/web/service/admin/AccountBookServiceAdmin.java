package com.muzisoft.division.web.service.admin;

import com.muzisoft.division.domain.board.AccountBook;
import com.muzisoft.division.domain.board.AccountBookRepository;
import com.muzisoft.division.web.api.dto.admin.accountBook.AccountBookDetailsResponse;
import com.muzisoft.division.web.api.dto.admin.accountBook.AccountBookSaveAndUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountBookServiceAdmin {

    private final AccountBookRepository accountBookRepository;

    @Transactional
    public void saveOrUpdate(AccountBookSaveAndUpdateRequest request) {
        Optional<AccountBook> optionalAccountBook = accountBookRepository.findByYear(request.getYear());

        if(optionalAccountBook.isPresent()) {
            AccountBook accountBook = optionalAccountBook.get();
            accountBook.update(request.getYear(), request.getContents(), request.getEtc());
        }else {
            accountBookRepository.save(
                    AccountBook.create(
                            request.getYear(),
                            request.getContents(),
                            request.getEtc()
                    )
            );
        }
    }

    @Transactional
    public AccountBookDetailsResponse details(int year) {
        AccountBookDetailsResponse response = new AccountBookDetailsResponse();
        Optional<AccountBook> optionalAccountBook = accountBookRepository.findByYear(year);
        if(optionalAccountBook.isPresent()) {
            AccountBook accountBook = optionalAccountBook.get();
            return new AccountBookDetailsResponse(accountBook);
        }else {
            return response;
        }
    }
}
