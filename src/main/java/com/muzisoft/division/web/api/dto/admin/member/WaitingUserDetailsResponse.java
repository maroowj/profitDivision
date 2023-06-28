package com.muzisoft.division.web.api.dto.admin.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaitingUserDetailsResponse {

    private User userDetails;
    private Recommender recommenderDetails;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        private String userDetailsSeq;
        private int accepted;
        private String userid;
        private String name;
        private String birth;
        private String mobile;
        private String email;
        private String address;
        private String createdAt;
        private String recommenderId;
        private String recommenderSeq;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Recommender {
        private String grade;
        private String level;
        private String membershipNumber;
        private String userId;
        private String name;
        private String birth;
        private String mobile;
        private String email;
        private String address;
        private String bankName;
        private String accountNumber;
        private String createdAt;
        private String recommenderId;
        private boolean withdrawal;
    }
}
