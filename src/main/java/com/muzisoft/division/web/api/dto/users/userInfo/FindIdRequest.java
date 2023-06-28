package com.muzisoft.division.web.api.dto.users.userInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindIdRequest {

    private String name;
    private String mobile;
}
