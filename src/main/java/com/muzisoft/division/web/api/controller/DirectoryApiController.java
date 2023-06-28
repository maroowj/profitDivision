package com.muzisoft.division.web.api.controller;

import com.muzisoft.division.web.api.dto.directory.DirectoryCreateRequest;
import com.muzisoft.division.web.service.DirectoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/directories")
@RequiredArgsConstructor
public class DirectoryApiController {

    private final DirectoryService directoryService;

    @PostMapping
    public void create(@RequestBody @Validated DirectoryCreateRequest request) {
        directoryService.create(request);
    }
}
