package com.muzisoft.division.web.exception.business;

import com.muzisoft.division.web.api.dto.common.ErrorCode;

public class CInvalidValueException extends CBusinessException {
    public CInvalidValueException(ErrorCode errorCode) {
            super(errorCode);
    }

    public static class CAlreadySignedupException extends CInvalidValueException {
        public CAlreadySignedupException() {
            super(ErrorCode.ALREADY_SIGNEDUP);
        }
    }

    public static class CLoginFailedException extends CInvalidValueException {
        public CLoginFailedException() {
            super(ErrorCode.LOGIN_FAIL);
        }
    }

    public static class CInvalidPasswordException extends CInvalidValueException {
        public CInvalidPasswordException() {
            super(ErrorCode.INVALID_PASSWORD);
        }
    }

    public static class CWithdrawalException extends CInvalidValueException {
        public CWithdrawalException() {
            super(ErrorCode.WITHDRAWAL);
        }
    }

    public static class CAlreadyInvestmentStatusUpdateException extends CInvalidValueException {
        public CAlreadyInvestmentStatusUpdateException() {
            super(ErrorCode.ALREADY_INVESTMENT_STATUS_UPDATE);
        }
    }

    public static class CInvalidCodeException extends CInvalidValueException {
        public CInvalidCodeException() {
            super(ErrorCode.INVALID_CODE_VALUE);
        }
    }
}
