package com.sunmange.unionpay.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sunmange.unionpay.acp.demo.DemoBase;
import com.sunmange.unionpay.acp.sdk.AcpService;
import com.sunmange.unionpay.acp.sdk.LogUtil;
import com.sunmange.unionpay.acp.sdk.SDKConfig;
import com.sunmange.unionpay.mapper.SecurePayMapper;
import com.sunmange.unionpay.service.SecurePayService;
import com.sunmange.unionpay.util.SecurePayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Service
public class SecurePayServiceImpl implements SecurePayService {

    private static final Logger logger = LoggerFactory.getLogger(SecurePayServiceImpl.class);

    @Autowired
    SecurePayMapper securePayMapper;

    @Autowired
    SecurePayUtil securePayUtil;

    @Override
    public String pay(JSONObject inData) throws SQLIntegrityConstraintViolationException {

        logger.info("-----------------------开始进行SecurePay业务处理-----------------------");
        //获取前置日期流水

        logger.info("-----------------------开始获取参数-----------------------");

        String fcbpdt = securePayUtil.getFcbpdt();
        String fcbpsq = securePayUtil.getFcbsq(fcbpdt);

        logger.info("-----------------------取商户号-----------------------");
        //获取商户号
        String subsys = "unionpay";
        String chanel = "SecurePay";
        JSONObject params = securePayMapper.getUnionPayParams(subsys, chanel);
        String merId = params.getString("merId");

        logger.info("-----------------------获取报文必要字段-----------------------");

        String txnTime = securePayUtil.getTxnTime();

        String tranam = inData.getString("txnAmt");
        //交易金额处理
        String txnAmt = securePayUtil.dealTranAmount(tranam);

        String orderDesc = inData.getString("orderDesc");
        String orderId = inData.getString("orderId");

        JSONObject unpy_logs = new JSONObject();
        unpy_logs.put("fcbpdt", fcbpdt);
        unpy_logs.put("fcbpsq", fcbpsq);
        unpy_logs.put("rqdata", inData.toJSONString());
        unpy_logs.put("orderId", orderId);

        logger.info("-----------------------开始登记unpy_logs表-----------------------");

        int i = 0;

        //登记logs表
        i = securePayMapper.regRequest(unpy_logs);

        if (i != 1) {
            //TODO
            logger.info("-----------------------登记unpy_logs表失败-----------------------");
            return "登记unpy_logs表失败";
        }

        logger.info("-----------------------登记unpy_logs表成功-----------------------");
        Map<String, String> contentData = new HashMap<String, String>();

        logger.info("-----------------------组装报文-----------------------");

        /***For an all-channel UnionPay system, all parameters can be left unchanged except the one encoding, which you need to set as required.***/
        // Constant value: 5.1.0
        contentData.put("version", DemoBase.version);                  //M
        // Default value: UTF-8
        contentData.put("encoding", DemoBase.encoding);           //M
        // Value: 01 (RSA)
        contentData.put("signMethod", SDKConfig.getConfig().getSignMethod()); //M
        // Value: 01
        contentData.put("txnType", "01");                              //M
        // 01: Purchase, to differentiate the front-end purchase or back-end purchase through transaction request URL
        // 02: MOTO
        //05: Purchase with authentication (Applied to Product type 000301)
        contentData.put("txnSubType", "01");                           //Transaction sub-type 01-purchase
        //000301: Merchant-hosted
        //000000: ExpressPay
        //000902: Token payment
        //001001: MOTO
        contentData.put("bizType", "000201");         //M
        // 0: Merchant direct access
        // 1: Acquirer Access
        // 2: Platform merchant access
        contentData.put("accessType", "0");// M
        // 07: Internet
        // 08: Mobile
        contentData.put("channelType", "07");// M

        // Merchant ID
        contentData.put("merId", merId);                              //M
        // Merchant order No
        contentData.put("orderId", orderId);                           //M client order number, consisting of 8-40 alphanumeric characters, no “-” or “_” is allowed, but custom rules are allowed
        // Date and time when merchant sends transaction
        contentData.put("txnTime", txnTime);                           //M order delivery time: It must be in format of YYYYMMDDhhmmss. Be sure to use the current time. Otherwise, an error of invalid txnTime will be reported.
        // Default value is 156.
        contentData.put("currencyCode", "156");                           //M transaction currency (for domestic clients, it is usually 156, which indicates RMB)
        // The unit of transaction amount is cent.
        contentData.put("txnAmt", txnAmt);                               //M transaction amount: in cents, without any decimal point.

        //Consumption: The transaction element card number and authentication code depend on the service configuration (by default, an SMS authentication code is required).
        //Map<String,String> customerInfoMap = new HashMap<String,String>();
        //customerInfoMap.put("smsCode", "111111");			    	//SMS authentication code: You will not actually receive an SMS in the test environment. Therefore, always fill in 111111 here.

        ////////////If the client has enabled the right [encrypt sensitive information by the client], you need to encrypt accNo, pin, phoneNo, cvn2, and expired (if these fields will be sent later) for encryption of sensitive information.
        //String accNo = AcpService.encryptData("6216261000000000018", DemoBase.encoding);  //A test card number is used here because it is in test environment. In normal environment, please use a real card number instead.
        //contentData.put("accNo", accNo);
        //contentData.put("encryptCertId",AcpService.getEncryptCertId());       //certId of the encryption certificate, which is configured under the acpsdk.encryptCert.path property of the acp_sdk.properties file.
        //String customerInfoStr = AcpService.getCustomerInfoWithEncrypt(customerInfoMap,"6216261000000000018",DemoBase.encoding);

        //contentData.put("customerInfo", customerInfoStr);//M

//        contentData.put("payTimeout", "");// O

        contentData.put("backUrl", DemoBase.backUrl);

        contentData.put("frontUrl", DemoBase.frontUrl);

        contentData.put("acqInsCode", "01040826");

        contentData.put("merCatCode", "");
        contentData.put("merName", "");
        contentData.put("merAbbr", "");

//        contentData.put("orderDesc",orderDesc);
        logger.info("-----------------------对报文进行签名-----------------------");

        /**All request parameters have been set. Now, sign the request parameters and generate an html form. Then, write the form to the browser and jump to and open the UnionPay page.**/
        Map<String, String> submitFromData = AcpService.sign(contentData, DemoBase.encoding);  //In a message, the values of certId and signature are obtained from the signData method and are assigned with values automatically. Therefore, you just need to ensure that the certificate is correctly configured.

        logger.info("-----------------------开始登记unpy_tran表-----------------------");

        //登记unpy_tran表
        JSONObject unpy_tran = JSONObject.parseObject(JSON.toJSONString(submitFromData));

        unpy_tran.put("fcbpdt", fcbpdt);
        unpy_tran.put("fcbpsq", fcbpsq);
        unpy_tran.put("orderDesc", orderDesc);

        i = securePayMapper.regUnionPayTran(unpy_tran);

        if (i != 1) {

            logger.info("-----------------------登记unpy_tran表失败-----------------------");
            return "登记unpy_tran表失败";
        }

        logger.info("-----------------------登记unpy_tran表成功-----------------------");

        String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();  //Obtain the requested foreground UnionPay address: acpsdk.frontTransUrl in the corresponding property file acp_sdk.properties

        logger.info("-----------------------请求银联，等待重定向-----------------------");
        securePayUtil.printMap(submitFromData);
        String html = AcpService.createAutoFormHtml(requestFrontUrl, submitFromData, DemoBase.encoding);   //Generate an html form which can jump to UnionPay page automatically

        logger.info("-----------------------银联返回页面为：-----------------------\r\n" + html);

        LogUtil.writeLog("Print the request HTML, which is a request message and the basis for problem joint debugging and troubleshooting" + html);
        //Write the generated html to the browser to automatically jump to and open the UnionPay payment page. Here, do not modify the names and values of the form items in the html after calling signData or before writing the html to the browser. Such modification may cause failure of the signature authentication.

        return html;
    }
}
