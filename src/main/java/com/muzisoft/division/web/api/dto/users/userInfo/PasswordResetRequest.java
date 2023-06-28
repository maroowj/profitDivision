package com.muzisoft.division.web.api.dto.users.userInfo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordResetRequest {

    private String password;
}
