package com.muzisoft.division.web.api.dto.admin.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDeleteRequest {

    private List<String> boardSeq;
}
