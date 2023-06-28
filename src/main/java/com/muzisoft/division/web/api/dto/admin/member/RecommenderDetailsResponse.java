package com.muzisoft.division.web.api.dto.admin.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommenderDetailsResponse {

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
    private String recommender;
}
