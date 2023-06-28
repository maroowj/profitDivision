package com.muzisoft.division.web.api.dto.users.userInfo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DuplicateCheckIdRequest {

    @NotEmpty
    private String id;
}
