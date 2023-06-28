package com.muzisoft.division.web.api.dto.admin.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerKindsSaveAndUpdateRequest {

    private String name;
    private List<String> abilities;
}
