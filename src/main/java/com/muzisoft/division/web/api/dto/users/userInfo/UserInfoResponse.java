package com.muzisoft.division.web.api.dto.users.userInfo;

import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserAccount;
import com.muzisoft.division.domain.user.UserDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {

    private String userId;
    private String name;
    private String birth;
    private String mobile;
    private String email;
    private String address;
    private String bankName;
    private String accountNumber;

    public UserInfoResponse(User user, UserDetails userDetails, UserAccount userAccount) {
        setUserId(user.getUserId());
        setName(userDetails.getName());
        setBirth(userDetails.getBirth());
        setMobile(userDetails.getMobile());
        setEmail(userDetails.getEmail());
        setAddress(userDetails.getAddress());
        if(userAccount!=null) {
            setBankName(userAccount.getBankName());
            setAccountNumber(userAccount.getAccountNumber());
        }
    }
}
