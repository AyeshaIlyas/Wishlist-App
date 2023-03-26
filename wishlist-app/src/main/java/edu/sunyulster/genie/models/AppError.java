package edu.sunyulster.genie.models;

public class AppError {

    private int statusCode;
    private String msg;

    public AppError() {

    }

    public AppError(int statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return String.format("ERROR (%s) %s", statusCode, msg);
    }

}
