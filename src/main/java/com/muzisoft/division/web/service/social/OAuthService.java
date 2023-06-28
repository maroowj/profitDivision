package com.muzisoft.division.web.service.social;

import com.muzisoft.division.utils.enums.SocialType;
import com.muzisoft.division.web.client.OAuthClient;
import com.muzisoft.division.web.client.dto.OAuthTokenResponse;
import com.muzisoft.division.web.client.dto.SocialProfile;
import com.muzisoft.division.web.exception.social.CSocialException.CInvalidSocialTypeException;
import com.muzisoft.division.web.exception.social.CSocialException.CSocialCommunicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

    private final List<OAuthClient> socialOAuthList;
    private final HttpServletResponse response;

    public String oauthRedirectURL(SocialType socialType) {
        OAuthClient oauthClient = this.findSocialOauthByType(socialType);
        return oauthClient.getOauthRedirectURL();
    }

    public void request(SocialType socialType) {
        OAuthClient oauthClient = this.findSocialOauthByType(socialType);
        String redirectURL = oauthClient.getOauthRedirectURL();
        try {
            response.sendRedirect(redirectURL);
        } catch (IOException e) {
            log.error(e.toString());
            throw new CSocialCommunicationException();
        }
    }

    public OAuthTokenResponse tokenInfo(SocialType socialType, String code) {
        OAuthClient socialOauth = this.findSocialOauthByType(socialType);
        return socialOauth.getTokenInfo(code);
    }

    public SocialProfile profile(SocialType socialType, String accessToken) {
        OAuthClient socialOauth = this.findSocialOauthByType(socialType);
        return socialOauth.getProfile(accessToken);
    }

    private OAuthClient findSocialOauthByType(SocialType socialType) {
        return socialOAuthList.stream()
                .filter(x -> x.type() == socialType)
                .findFirst()
                .orElseThrow(CInvalidSocialTypeException::new);
    }
}
