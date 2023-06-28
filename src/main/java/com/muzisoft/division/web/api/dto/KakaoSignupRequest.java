package com.muzisoft.division.web.api.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoSignupRequest {

    private String sid;
    private String nickname;
    private String gender;
    private String age;
    private String email;
    private String birth;
    private String thumbnailImageUrl;
    private String provider;
}
