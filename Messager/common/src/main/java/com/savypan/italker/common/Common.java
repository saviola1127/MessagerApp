package com.savypan.italker.common;

public class Common {

    public interface Constant {
        //手机号11位，第一位总位1
        String REGEX_MOBILE = "[1][3，4，5，7，8][0-9]{9}";

        //基础的网络请求地址
        String API_URL = "http://192.168.50.72:8080/api/";
    }
}
