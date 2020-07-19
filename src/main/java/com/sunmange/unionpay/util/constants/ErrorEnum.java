package com.sunmange.unionpay.util.constants;

/**
 * @author: burning
 * @date: 2018/10/24 10:16
 */
public enum ErrorEnum {
    /*
     * 错误信息
     * */
    E_1010("1010", "登记日志表失败"),
    E_1011("1011", "登记交易表失败"),
    E_1012("1012", "更新日志表失败"),
    E_1013("1013", "更新交易表失败"),
    E_2012("2012", "银联返回交易失败"),
    E_2013("2013", "验签失败");

    private String errorCode;

    private String errorMsg;

    ErrorEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
