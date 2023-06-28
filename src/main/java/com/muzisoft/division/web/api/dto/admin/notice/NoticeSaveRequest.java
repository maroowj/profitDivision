package com.muzisoft.division.web.api.dto.admin.notice;

import com.muzisoft.division.domain.file.EFileManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeSaveRequest {

    MultipartFile attach;

    private String userDetailsSeq;
    private String title;
    private String contents;
    private String fixed;
}
