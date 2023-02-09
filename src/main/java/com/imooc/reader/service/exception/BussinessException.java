package com.imooc.reader.service.exception;

/**
 * 自訂一個RuntimeException異常:代表業務處理時發生的異常
 * code 代表錯誤代碼
 * mes 說明錯誤狀況
 */
public class BussinessException extends RuntimeException{
    private String code;
    private String msg;
    public BussinessException(String code,String msg){
        //使用super的Constructor並傳入異常的message
        super(msg);
        this.code=code;
        this.msg=msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
