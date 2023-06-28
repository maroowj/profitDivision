package com.muzisoft.division.web.api.dto.admin.reserve;

import com.muzisoft.division.domain.setup.Environments;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversionRateResponse {

    private int conversionRate;

    public ConversionRateResponse(Environments environments) {
        this.conversionRate = environments.getConversionRate();
    }
}
