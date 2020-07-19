package com.sunmange.unionpay.controller;

import com.alibaba.fastjson.JSONObject;
import com.sunmange.unionpay.service.SecurePayService;
import com.sunmange.unionpay.util.SecurePayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

@Controller
@RequestMapping("securepay")
public class SecurePayController {

    private static final Logger logger = LoggerFactory.getLogger(SecurePayController.class);

    @Autowired
    SecurePayService securePayService;

    @Autowired
    SecurePayUtil securePayUtil;

    @PostMapping(value = "/purchase")
    public void purchase(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLIntegrityConstraintViolationException {

        logger.info("-----------------------开始进入SecurePay 消费接口-----------------------");

        JSONObject inData = securePayUtil.request2Json(req);

        String html = "";
        html = securePayService.pay(inData);
        logger.info("-----------------------结束SecurePay 消费接口-----------------------");

        resp.getWriter().write(html);

        logger.info("-----------------------重定向至银联支付页面完成-----------------------");

    }
}
