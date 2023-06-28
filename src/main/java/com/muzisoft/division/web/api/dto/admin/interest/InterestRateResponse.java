package com.muzisoft.division.web.api.dto.admin.interest;

import com.muzisoft.division.domain.setup.Environments;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterestRateResponse {

    private float interestRate;

    public InterestRateResponse(Environments environments) {
        this.interestRate = environments.getInterestRate();
    }

}
