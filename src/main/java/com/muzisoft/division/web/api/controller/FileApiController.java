package com.muzisoft.division.web.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;

@Slf4j
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileApiController {

    @GetMapping(
            value = "/**",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] file(HttpServletRequest request) throws Exception {
        String filePath = new File("").getAbsolutePath() + "/files/" + request.getRequestURI()
                .split(request.getContextPath() + "/files/")[1];
        FileInputStream in = new FileInputStream(filePath);
        byte[] image = IOUtils.toByteArray(in);
        in.close();
        return image;
    }
}

