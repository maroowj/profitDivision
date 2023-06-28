package com.muzisoft.division.web.api.dto.users.dues;

import com.muzisoft.division.domain.setup.Environments;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DuesInformationResponse {

    private int duesDate;
    private int duesAmount;

    public DuesInformationResponse(Environments environments) {
        setDuesDate(environments.getDuesDate());
        setDuesAmount(environments.getDuesAmount());
    }
}
