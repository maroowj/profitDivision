package com.muzisoft.division.config.security;

import com.muzisoft.division.web.api.dto.common.TokenResponse;
import com.muzisoft.division.web.exception.security.CSecurityException;
import com.muzisoft.division.web.service.security.CustomUserDetailsService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {

    public final String ROLES = "roles";
    private final Long accessTokenValidMillisecond = 60 * 60 * 1000L; //1 hours
//    private final Long accessTokenValidMillisecond = 15 * 1000L; //15 seconds
    private final Long refreshTokenValidMillisecond = 90 * 24 * 60 * 60 * 1000L; //90 days
//    private final Long refreshTokenValidMillisecond = 30 * 1000L; //30 seconds
    private final CustomUserDetailsService userDetailsService;
    @Value("${spring.jwt.secret}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * Jwt 생성
     */
    public TokenResponse createToken(String userPk, List<String> roles) {

        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put(ROLES, roles);

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(Jwts.claims().setSubject(userPk))
                .setExpiration(new Date(now.getTime() + refreshTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return new TokenResponse("bearer", accessToken, refreshToken, accessTokenValidMillisecond);
    }

    public String generateAccessToken(String refreshToken) {

        Claims claims = parseClaims(refreshToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

        claims.put(ROLES, userDetails.getAuthorities());

        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Jwt로 인증정보를 조회
     */
    public Authentication getAuthentication(String token) {

        Claims claims = parseClaims(token);

        if (ObjectUtils.isEmpty(claims.get(ROLES))) {
            throw new CSecurityException.CAuthenticationEntryPointException();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * 만료된 토큰이어도 refresh token을 검증 후 재발급할 수 있도록 claims 반환
     */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if(StringUtils.hasText(authorization) && authorization.startsWith("Bearer") && authorization.split(" ").length >= 2) {
            return authorization.split(" ")[1];
        } else {
            return null;
        }
    }

    /**
     * Jwt의 유효성 및 만료일자 확인
     */
    public boolean validationToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error(e.toString());
            return false;
        }
    }
}
