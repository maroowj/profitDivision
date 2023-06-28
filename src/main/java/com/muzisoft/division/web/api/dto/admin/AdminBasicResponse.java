package com.muzisoft.division.web.api.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminBasicResponse {

    private String name;
    private String grade;
    private String profileImage;
}
