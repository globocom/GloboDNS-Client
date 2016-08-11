package com.globo.globodns.client.exception;


import com.globo.globodns.client.GloboDnsException;

public class GloboDnsHttpException extends GloboDnsException {
    private int httpStatusCode;
    public GloboDnsHttpException(String msg) {
        super(msg);
    }

    public GloboDnsHttpException(String msg, Throwable e) {
        super(msg, e);
    }
    public GloboDnsHttpException(String msg, int httpStatusCode) {
        super(msg);
        this.httpStatusCode = httpStatusCode;
    }

    public GloboDnsHttpException(String msg, Throwable e, int httpStatusCode) {
        super(msg, e);
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
