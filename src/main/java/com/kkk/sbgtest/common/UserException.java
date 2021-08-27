package com.kkk.sbgtest.common;

import lombok.Data;

/**
 * 사용자 예외처리
 * 예외처리도 결국 필요한 데이터를 제공해주고, 필요한 곳까지 운반해주는 기능임
 * UserRetCode와 페어로 사용됨
 * 예시
 * @Transactional(rollbackFor = UserException.class)
 */
@Data
public class UserException extends Exception {
    UserRetCode retCode = null;
    String addMsg = null;

    public UserException() {
        super();
    }

    public UserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(Throwable cause) {
        super(cause);
    }

    // User Construct
    public UserException(UserRetCode retCode) {
        this.retCode = retCode;
    }

    public UserException(UserRetCode retCode, String addMsg) {
        this.retCode = retCode;
        this.addMsg = addMsg;
    }

}
