package com.muzisoft.division.web.api.dto.admin.accountBook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountBookSaveAndUpdateRequest {

    private int year;
    private Map<String, Object> contents;
    private String etc;
}
