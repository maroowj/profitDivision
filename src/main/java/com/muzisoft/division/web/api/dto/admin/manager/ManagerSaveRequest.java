package com.muzisoft.division.web.api.dto.admin.manager;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManagerSaveRequest {

    MultipartFile profilePicture;

    @NotEmpty
    private String id;

    @NotEmpty
    private String password;

    private String name;

    private String nickName;

    private String mobile;

    private String managerKindsSeq;
}
