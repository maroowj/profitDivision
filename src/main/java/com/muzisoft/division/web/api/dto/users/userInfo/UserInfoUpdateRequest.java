package com.muzisoft.division.web.api.dto.users.userInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoUpdateRequest {

    private String name;
    private String birth;
    private String mobile;
    private String email;
    private String address;
    private String bankName;
    private String accountNumber;

}
