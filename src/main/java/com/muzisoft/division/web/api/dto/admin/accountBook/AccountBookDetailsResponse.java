package com.muzisoft.division.web.api.dto.admin.accountBook;

import com.muzisoft.division.domain.board.AccountBook;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountBookDetailsResponse {

    private int year;
    private Map<String, Object> contents;
    private String etc;

    public AccountBookDetailsResponse(AccountBook accountBook) {
        this.year=accountBook.getYear();
        this.contents=accountBook.getContents();
        this.etc=accountBook.getEtc();
    }
}
