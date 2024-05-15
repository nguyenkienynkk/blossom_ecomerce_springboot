package com.ngkk.webapp_springboot.exceptions;

public class ExpiredTokenException extends Exception{
    public ExpiredTokenException(String message) {
        super(message);
    }
}
