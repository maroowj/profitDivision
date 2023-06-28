package com.muzisoft.division.web.api.dto.admin.dues;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DuesPushSaveRequest {

    private int year;
    private int month;
    private String contents;
}
