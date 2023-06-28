package com.muzisoft.division.web.api.dto.admin.reserve;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservesSaveRequest {

    private List<String> userDetailsSeq;
    private int savedAmount;
    private String content;
}
