package com.muzisoft.division.web.api.dto.users.dues;

import com.muzisoft.division.domain.dues.Dues;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotPaidDuesResponse {

    private int notPaidYear;
    private int notPaidMonth;

    public NotPaidDuesResponse(Dues dues) {
        setNotPaidYear(dues.getDuesMonth().getYear());
        setNotPaidMonth(dues.getDuesMonth().getMonth());
    }
}
