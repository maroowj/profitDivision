package com.muzisoft.division.web.api.dto.admin.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsResponseInAdmin {

    private String membershipNumber;
    private String userDetailsSeq;
    private String name;
    private Grade grade;
    private String level;
    private String userId;
    private String birth;
    private String mobile;
    private String email;
    private String address;
    private String bankName;
    private String accountNumber;
    private String createdAt;
    private String recommender;
    private boolean withdrawal;
    private String withdrawalReason;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Grade {
        private int gradeSeq;
        private String title;
    }
}
