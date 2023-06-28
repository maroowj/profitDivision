package com.muzisoft.division.web.api.dto.admin.manager;

import com.muzisoft.division.domain.manager.ManagerUsers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerUserDetailsResponse {

    private String managerUserSeq;
    private String managerKindsSeq;
    private String id;
    private String name;
    private String nickName;
    private String mobile;
    private String createdAt;
    private String imgUrl;

    public ManagerUserDetailsResponse(ManagerUsers managerUsers, String imgUrl) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.managerUserSeq = managerUsers.getSeq();
        if(managerUsers.getKinds()!=null) {
            this.managerKindsSeq = managerUsers.getKinds().getSeq();
        }
        this.id = managerUsers.getUser().getUserId();
        this.name = managerUsers.getUser().getUsername();
        this.nickName = managerUsers.getNickname();
        this.mobile = managerUsers.getUser().getUserDetails().getMobile();
        this.createdAt = sdf.format(managerUsers.getCreatedAt());
        this.imgUrl = imgUrl;
    }
}
