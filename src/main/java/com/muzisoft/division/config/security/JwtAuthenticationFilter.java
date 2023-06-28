package com.muzisoft.division.config.security;

import com.muzisoft.division.utils.CookieUtils;
import com.muzisoft.division.web.api.dto.common.ErrorCode;
import com.muzisoft.division.web.exception.business.CEntityNotFoundException;
import com.muzisoft.division.web.exception.security.CSecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private static final String ACCESS_TOKEN = "AccessToken";
    private static final String REFRESH_TOKEN = "RefreshToken";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String token = null;

        if(CookieUtils.existsFor(httpServletRequest, ACCESS_TOKEN)) {
            if (CookieUtils.existsFor(httpServletRequest, REFRESH_TOKEN)) {
                token = CookieUtils.getValue(httpServletRequest, ACCESS_TOKEN);
            } else {
                Cookie cookie = CookieUtils.getCookie(httpServletRequest, ACCESS_TOKEN);
                cookie.setValue(null);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
//                return;
            }
        } else {
            token = jwtProvider.resolveToken(httpServletRequest);
        }

//        log.info("[Verifying token]");
//        log.info(((HttpServletRequest) request).getRequestURL().toString());

        try {
//            if(StringUtils.hasText(token) && jwtProvider.validationToken(token)) {
//                Authentication authentication = jwtProvider.getAuthentication(token);
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            } else {
//                log.error("Security Error 1");
//                throw new CSecurityException.CAuthenticationEntryPointException();
//            }
            if(StringUtils.hasText(token)) {
                if (jwtProvider.validationToken(token)) {
                    Authentication authentication = jwtProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    if (CookieUtils.existsFor(httpServletRequest, "savedId")) {
//                        System.out.println("REQUEST URI: " + request.getRequestURI());
//                        System.out.println("TOKEN RESET <<<");
                        if (!request.getRequestURI().endsWith("logout")) {
                            Cookie kc = CookieUtils.getCookie(httpServletRequest, "savedId");
                            kc.setMaxAge(60 * 60 * 24 * 7);
                            kc.setPath("/");
                            kc.setHttpOnly(true);
                            response.addCookie(kc);
                            Cookie rc = CookieUtils.getCookie(httpServletRequest, REFRESH_TOKEN);
                            rc.setMaxAge(60 * 60 * 24 * 7);
                            rc.setPath("/");
                            rc.setHttpOnly(true);
                            response.addCookie(rc);
                        }
                    } else {
//                        if (!request.getRequestURI().endsWith("logout")) {
//                            Cookie rc = CookieUtils.getCookie(httpServletRequest, REFRESH_TOKEN);
//                            rc.setMaxAge(-1);
//                            rc.setPath("/");
//                            rc.setHttpOnly(true);
//                            response.addCookie(rc);
//                        }
                    }
                } else {
                    String rt = CookieUtils.getValue(httpServletRequest, REFRESH_TOKEN);
                    if (StringUtils.hasText(rt) && jwtProvider.validationToken(rt)) {
                        Cookie cookie = new Cookie("AccessToken", jwtProvider.generateAccessToken(rt));
                        cookie.setMaxAge(-1);
                        cookie.setPath("/");
                        cookie.setHttpOnly(true);
                        response.addCookie(cookie);
                    } else {
                        throw new CSecurityException.CAuthenticationEntryPointException();
                    }
                }
            } else {
                if (CookieUtils.existsFor(httpServletRequest, "savedId") && CookieUtils.existsFor(httpServletRequest, REFRESH_TOKEN)) {
                    String rt = CookieUtils.getValue(httpServletRequest, REFRESH_TOKEN);

                    if (StringUtils.hasText(rt) && jwtProvider.validationToken(rt)) {
                        Cookie cookie = new Cookie("AccessToken", jwtProvider.generateAccessToken(rt));
                        cookie.setMaxAge(-1);
                        cookie.setPath("/");
                        cookie.setHttpOnly(true);
                        response.addCookie(cookie);
                    } else {
                        throw new CSecurityException.CAuthenticationEntryPointException();
                    }
                } else {
//                log.error("Security Error 1");
                    throw new CSecurityException.CAuthenticationEntryPointException();
                }
            }
        } catch (CEntityNotFoundException.CUserNotFoundException e) {
//            log.error("Entity Not Found");
            request.setAttribute("exception", ErrorCode.USER_NOT_FOUND.getCode());
        } catch (CSecurityException.CAuthenticationEntryPointException e) {
//            log.error("Security Error 2");
            request.setAttribute("exception", ErrorCode.ACCESS_TOKEN_ERROR.getCode());
        } finally {
            chain.doFilter(request, response);
        }
    }

/*
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String token = jwtProvider.resolveToken((HttpServletRequest) request);

        log.info("[Verifying token]");
        log.info(((HttpServletRequest) request).getRequestURL().toString());

        try {
            if(StringUtils.hasText(token) && jwtProvider.validationToken(token)) {
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new CSecurityException.CAuthenticationEntryPointException();
            }
        } catch (CEntityNotFoundException.CUserNotFoundException e) {
            request.setAttribute("exception", ErrorCode.USER_NOT_FOUND.getCode());
        } catch (CSecurityException.CAuthenticationEntryPointException e) {
            request.setAttribute("exception", ErrorCode.ACCESS_TOKEN_ERROR.getCode());
        } finally {
            chain.doFilter(request, response);
        }
    }
    */
}
