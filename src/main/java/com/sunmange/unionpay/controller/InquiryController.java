package com.sunmange.unionpay.controller;

import com.alibaba.fastjson.JSONObject;
import com.sunmange.unionpay.service.InquiryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("inquiry")
public class InquiryController {
    private static final Logger logger = LoggerFactory.getLogger(InquiryController.class);

    @Autowired
    InquiryService inquiryService;

    @RequestMapping("/transactionStateInquiry")
    public JSONObject transactionStateInquiry() {
//    public JSONObject transactionStateInquiry(@RequestBody JSONObject inData){
        logger.info("-----------------------开始进入交易状态查询接口-----------------------");

        JSONObject inData = new JSONObject();

        inData.put("orderId", "20200717121130000000000001");
        inData.put("txnTime", "20200717121130");

        JSONObject outData = new JSONObject();

        inquiryService.purchaseStateInquiry(inData);

        logger.info("-----------------------结束交易状态查询接口-----------------------");

        return outData;
    }
}
