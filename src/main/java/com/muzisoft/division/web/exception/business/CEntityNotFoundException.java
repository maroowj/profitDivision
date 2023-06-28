package com.muzisoft.division.web.exception.business;

import com.muzisoft.division.web.api.dto.common.ErrorCode;

public class CEntityNotFoundException extends CBusinessException {
    public CEntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static class CUserNotFoundException extends CEntityNotFoundException {
        public CUserNotFoundException() {
            super(ErrorCode.USER_NOT_FOUND);
        }
    }

    public static class CGradeNotFoundException extends CEntityNotFoundException {
        public CGradeNotFoundException() {
            super(ErrorCode.GRADE_NOT_FOUND);
        }
    }

    public static class CUserDetailsNotFoundException extends CEntityNotFoundException {
        public CUserDetailsNotFoundException() {
            super(ErrorCode.USER_DETAILS_NOT_FOUND);
        }
    }

    public static class CRecommenderNotFoundException extends CEntityNotFoundException {
        public CRecommenderNotFoundException() {
            super(ErrorCode.RECOMMENDER_NOT_FOUND);
        }
    }

    public static class CInvestmentNotFoundException extends CEntityNotFoundException {
        public CInvestmentNotFoundException() {
            super(ErrorCode.RECOMMENDER_NOT_FOUND);
        }
    }

    public static class CEnvironmentsNotFoundException extends CEntityNotFoundException {
        public CEnvironmentsNotFoundException() {
            super(ErrorCode.ENVIRONMENTS_NOT_FOUND);
        }
    }

    public static class CDuesMonthNotFoundException extends CEntityNotFoundException {
        public CDuesMonthNotFoundException() {
            super(ErrorCode.DUESMONTH_NOT_FOUND);
        }
    }

    public static class CDuesNotFoundException extends CEntityNotFoundException {
        public CDuesNotFoundException() {
            super(ErrorCode.DUES_NOT_FOUND);
        }
    }

    public static class CManagerKindsNotFoundException extends CEntityNotFoundException {
        public CManagerKindsNotFoundException() {
            super(ErrorCode.MANAGER_KINDS_NOT_FOUND);
        }
    }

    public static class CManagerUserNotFoundException extends CEntityNotFoundException {
        public CManagerUserNotFoundException() {
            super(ErrorCode.MANAGER_USERS_NOT_FOUND);
        }
    }

    public static class CExpectedProfitNotFoundException extends CEntityNotFoundException {
        public CExpectedProfitNotFoundException() {
            super(ErrorCode.EXPECTED_PROFIT_NOT_FOUND);
        }
    }

    public static class CProfitNotFoundException extends CEntityNotFoundException {
        public CProfitNotFoundException() {
            super(ErrorCode.PROFIT_NOT_FOUND);
        }
    }

    public static class CWithdrawalNotFoundException extends CEntityNotFoundException {
        public CWithdrawalNotFoundException() {
            super(ErrorCode.WITHDRAWAL_NOT_FOUND);
        }
    }

    public static class CInterestNotFoundException extends CEntityNotFoundException {
        public CInterestNotFoundException() {
            super(ErrorCode.INTEREST_NOT_FOUND);
        }
    }
    public static class CPopupNotFoundException extends CEntityNotFoundException {
        public CPopupNotFoundException() {
            super(ErrorCode.POPUP_NOT_FOUND);
        }
    }
    public static class CNoticeNotFoundException extends CEntityNotFoundException {
        public CNoticeNotFoundException() {
            super(ErrorCode.NOTICE_NOT_FOUND);
        }
    }
    public static class CBoardNotFoundException extends CEntityNotFoundException {
        public CBoardNotFoundException() {
            super(ErrorCode.BOARD_NOT_FOUND);
        }
    }
    public static class CFaqNotFoundException extends CEntityNotFoundException {
        public CFaqNotFoundException() {
            super(ErrorCode.FAQ_NOT_FOUND);
        }
    }
    public static class CManagerPermissionNotFoundException extends CEntityNotFoundException {
        public CManagerPermissionNotFoundException() {
            super(ErrorCode.MANAGER_PERMISSION_NOT_FOUND);
        }
    }
}
