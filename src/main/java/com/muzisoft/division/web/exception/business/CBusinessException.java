package com.muzisoft.division.web.exception.business;

import com.muzisoft.division.web.api.dto.common.ErrorCode;
import lombok.Getter;

@Getter
public class CBusinessException extends RuntimeException {

    private ErrorCode errorCode;

    public CBusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public static class CWithdrawalDisposalNotYetException extends CBusinessException{
        public CWithdrawalDisposalNotYetException() {
            super(ErrorCode.WITHDRAWAL_DISPOSAL_NOT_YET);
        }
    }

    public static class CExceedRequestAmount extends CBusinessException{
        public CExceedRequestAmount() {
            super(ErrorCode.EXCEED_REQUEST_AMOUNT);
        }
    }

    public static class CAlreadyWithdrawalDisposed extends CBusinessException{
        public CAlreadyWithdrawalDisposed() {
            super(ErrorCode.ALREADY_DISPOSED_WITHDRAWAL);
        }
    }
}
