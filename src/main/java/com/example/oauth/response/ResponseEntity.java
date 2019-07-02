package com.example.oauth.response;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class ResponseEntity {
    private Date timestamp;
    private int status;
    private String message;
    private Object data;

    public ResponseEntity(HttpStatus status, String message, Object data) {
        this.timestamp = new Date();
        this.status = status.value();
        this.message = message;
        this.data = data;
    }

    public ResponseEntity(HttpStatus status, String message) {
        this.timestamp = new Date();
        this.status = status.value();
        this.message = message;
    }

    public ResponseEntity(HttpStatus status, Object data) {
        this.timestamp = new Date();
        this.status = status.value();
        this.data = data;
    }

    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
}
