package com.casic.atp;

/**
 * 自定义错误
 */
public class ATPException extends Exception {
    private int value;

    public ATPException() {
        super();
    }

    public ATPException(String msg) {
        super(msg);
    }

    public ATPException(String msg, int value) {
        super(msg);
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}