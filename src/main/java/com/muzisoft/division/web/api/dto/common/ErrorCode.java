package com.muzisoft.division.web.api.dto.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    /**
     * COMMON
     */
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(), "CMM-001", "잘못된 입력입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED.value(), "CMM-002", "Method Not Allowed"),
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "CMM-003", "Entity Not Found"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "CMM-004", "Server Error"),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST.value(), "CMM-005", "Invalid Type Value"),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "CMM-006", "접근이 거부되었습니다."),
    JSON_WRITE_ERROR(HttpStatus.UNAUTHORIZED.value(), "CMM-007", "JSON content that are not pure I/O problems"),

    /**
     * BUSINESS
     * MZS-1xxx
     */
    LOGIN_FAIL(HttpStatus.BAD_REQUEST.value(), "MZS-1000", "존재하지 않는 계정입니다.."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST.value(), "MZS-1000", "잘못된 비밀번호입니다."),
    WITHDRAWAL(HttpStatus.BAD_REQUEST.value(), "MZS-1000", "관리자에 의해 탈퇴 처리된 사용자입니다."),
    ALREADY_SIGNEDUP(HttpStatus.BAD_REQUEST.value(), "MZS-1001", "이미 가입한 사용자입니다."),
    USER_NOT_AUTHENTICATION(HttpStatus.UNAUTHORIZED.value(), "MZS-1002", "인증된 사용자가 아닙니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "MZS-1003", "사용자가 존재하지 않습니다."),
    USER_DETAILS_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "MZS-1004", "사용자 정보가 존재하지 않습니다."),
    GRADE_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "MZS-1005", "등급이 존재하지 않습니다."),
    RECOMMENDER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "MZS-1006", "추천인이 존재하지 않습니다."),
    INVESTMENT_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "MZS-1007", "투자내역이 존재하지 않습니다."),
    ENVIRONMENTS_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "MZS-1008", "환경설정이 존재하지 않습니다."),
    DUESMONTH_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "MZS-1009", "월별 회비 내역이 존재하지 않습니다."),
    DUES_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "MZS-1010", "개별 회비 내역이 존재하지 않습니다."),
    MANAGER_KINDS_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "MZS-1011", "관리자 등급(분류)이 존재하지 않습니다."),
    MANAGER_USERS_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "MZS-1012", "관리자 회원이 존재하지 않습니다."),
    ALREADY_INVESTMENT_STATUS_UPDATE(HttpStatus.BAD_REQUEST.value(), "MZS-1013", "이미 상태가 변경된 투자내역입니다."),
    EXPECTED_PROFIT_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "MZS-1014", "월별 가상 수익 내역이 존재하지 않습니다."),
    PROFIT_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "MZS-1015", "수익 내역이 존재하지 않습니다."),
    WITHDRAWAL_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "MZS-1016", "출금 정보가 존재하지 않습니다."),
    INTEREST_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "MZS-1017", "이자 내역이 존재하지 않습니다."),
    POPUP_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "MZS-1018", "팝업이 존재하지 않습니다."),
    NOTICE_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "MZS-1019", "공지사항이 존재하지 않습니다."),
    WITHDRAWAL_DISPOSAL_NOT_YET(HttpStatus.BAD_REQUEST.value(), "MZS-1020", "아직 처리 되지 않은 출금 요청이 있습니다."),
    EXCEED_REQUEST_AMOUNT(HttpStatus.BAD_REQUEST.value(), "MZS-1021", "요청된 금액이 총금액 보다 큽니다."),
    ALREADY_DISPOSED_WITHDRAWAL(HttpStatus.BAD_REQUEST.value(), "MZS-1022", "이미 처리된 출금 요청입니다."),
    INVALID_CODE_VALUE(HttpStatus.BAD_REQUEST.value(), "MZS-1023", "유효하지 않은 코드입니다."),
    BOARD_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "MZS-1024", "게시글이 존재하지 않습니다."),
    FAQ_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "MZS-1025", "자주 묻는 질문이 존재하지 않습니다."),
    MANAGER_PERMISSION_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "MZS-1025", "접근 권한 경로가 존재하지 않습니다."),

    /**
     * SOCIAL
     * MZS-2xxx
     */
    SOCIAL_COMMUNICATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "MZS-2000", "소셜 인증 과정 중 오류가 발생했습니다."),
    SOCIAL_AGREEMENT_ERROR(HttpStatus.BAD_REQUEST.value(), "MZS-2001", "필수동의 항목에 대해 동의가 필요합니다."),
    INVALID_SOCIAL_TYPE(HttpStatus.BAD_REQUEST.value(), "MZS-2002", "알 수 없는 소셜 타입입니다."),
    SOCIAL_TOKEN_VALID_FAILED(HttpStatus.UNAUTHORIZED.value(), "MZS-2003", "소셜 액세스 토큰 검증에 실패했습니다."),

    /**
     * SECURITY
     * MZS-3xxx
     */
    ACCESS_TOKEN_ERROR(HttpStatus.UNAUTHORIZED.value(), "MZS-3000", "액세스 토큰이 만료되거나 잘못된 값입니다."),
    REFRESH_TOKEN_ERROR(HttpStatus.UNAUTHORIZED.value(), "MZS-3001", "리프레시 토큰이 만료되거나 잘못된 값입니다."),
    TOKEN_PARSE_ERROR(HttpStatus.UNAUTHORIZED.value(), "MZS-3002", "해석할 수 없는 토큰입니다."),

    /**
     * SECURITY
     * MZS-4xxx
     */
    FILE_CONVERT_FAILED(HttpStatus.BAD_REQUEST.value(), "MZS-4000", "파일을 변환할 수 없습니다."),
    INVALID_FILE_FORMAT(HttpStatus.BAD_REQUEST.value(), "MZS-4001", "잘못된 형식의 파일입니다."),
    CLOUD_COMMUNICATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "MZS-4002", "파일 업로드 중 오류가 발생했습니다."),
            ;

    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}