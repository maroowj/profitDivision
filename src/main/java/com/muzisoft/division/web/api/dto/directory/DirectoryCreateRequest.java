package com.muzisoft.division.web.api.dto.directory;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DirectoryCreateRequest {

    @NotEmpty
    private String div;

    @NotEmpty
    private String dirName;

    @NotEmpty
    private String path;

    private String comment;

}
