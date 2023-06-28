package com.muzisoft.division.web.api.controller.admin;

import com.muzisoft.division.web.api.dto.admin.AdminBasicResponse;
import com.muzisoft.division.web.service.admin.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/admin/basic")
@RequiredArgsConstructor
public class BasicApiControllerAdmin {

    private final ManagerService managerService;

    @GetMapping
    public AdminBasicResponse adminBasicResponse() {
        return managerService.adminBasicResponse();
    }
}
