package com.muzisoft.division.web.api.dto.admin.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListResponse {

    private Page<UserList> userList;
    private int userCount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserList {
        private String membershipNumber;
        private String userDetailsSeq;
        private String userId;
        private String name;
        private String birth;
        private String mobile;
        private String email;
        private String address;
        private long investment;
        private String level;
        private String createdAt;
        private Grade grade;
        private boolean withdrawal;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Grade {
        private int gradeSeq;
        private String title;
    }
}
