package com.muzisoft.division.web.api.dto.admin.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardUpdateRequest {

    MultipartFile attach;

    private String userDetailsSeq;
    private String title;
    private String contents;
    private String fixed;
    private boolean attachPresent;
}
