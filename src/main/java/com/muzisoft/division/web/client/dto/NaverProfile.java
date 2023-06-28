package com.muzisoft.division.web.client.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NaverProfile {

    private String resultcode;
    private String message;
    private Response response;

    @Getter
    @ToString
    public static class Response {
        private String id;
        private String nickname;
        private String email;
        private String profile_image;
    }
}
