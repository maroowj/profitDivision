package com.muzisoft.division.web.api.controller.users;

import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.users.reserve.ReserveOverviewResponse;
import com.muzisoft.division.web.api.dto.users.reserve.ReservesConversionRequest;
import com.muzisoft.division.web.api.dto.users.reserve.UserReservesListResponse;
import com.muzisoft.division.web.service.users.ReservesServiceUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user/reserves")
@RequiredArgsConstructor
public class ReservesApiControllerUser {

    private final ReservesServiceUser reservesServiceUser;

    @GetMapping
    public Page<UserReservesListResponse> userReservesList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 20) Pageable pageable,
                                                           CommonCondition condition) {
        return reservesServiceUser.userReservesList(pageable, condition);
    }

    @GetMapping("/overview")
    public ReserveOverviewResponse reservesOverview() {
        return reservesServiceUser.reserveOverview();
    }

    @PostMapping
    public void reservesConversionRequest(@RequestBody ReservesConversionRequest request) {
        reservesServiceUser.reservesConversion(request);
    }
}
