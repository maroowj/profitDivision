package com.muzisoft.division.web.api.dto.admin.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaitingListResponse {

    private String userDetailsSeq;
    private String userId;
    private String name;
    private String birth;
    private String mobile;
    private String email;
    private String address;
    private String recommender;
    private String createdAt;
    private int accepted;
}
