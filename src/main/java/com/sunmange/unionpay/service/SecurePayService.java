package com.sunmange.unionpay.service;


import com.alibaba.fastjson.JSONObject;

import java.sql.SQLIntegrityConstraintViolationException;

public interface SecurePayService {


    String pay(JSONObject jsonObject) throws SQLIntegrityConstraintViolationException;

}
