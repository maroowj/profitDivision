package com.muzisoft.division.web.api.dto.admin.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Named;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerUpdateRequest {

    MultipartFile profilePicture;
    private String password;
    private String name;
    private String nickName;
    private String mobile;
    private String managerKindsSeq;
    private boolean defaultImg;
}
