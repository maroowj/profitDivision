package com.muzisoft.division.web.api.controller.users;

import com.muzisoft.division.web.api.dto.users.popup.PopupListResponseForUser;
import com.muzisoft.division.web.service.users.PopupServiceUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/common/popup")
public class PopupApiControllerUser {

    private final PopupServiceUser popupServiceUser;

    @GetMapping
    public List<PopupListResponseForUser> popupListResponseForUser() {
        return popupServiceUser.popupListResponseForUser();
    }
}
