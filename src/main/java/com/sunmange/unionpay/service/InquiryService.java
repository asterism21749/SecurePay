package com.sunmange.unionpay.service;

import com.alibaba.fastjson.JSONObject;

public interface InquiryService {

    JSONObject purchaseStateInquiry(JSONObject inData);
}
