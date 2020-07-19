package com.sunmange.unionpay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sunmange.unionpay.acp.sdk.AcpService;
import com.sunmange.unionpay.acp.sdk.LogUtil;
import com.sunmange.unionpay.acp.sdk.SDKConstants;
import com.sunmange.unionpay.acp.sdk.SDKUtil;
import com.sunmange.unionpay.controller.FrontRcvController;
import com.sunmange.unionpay.mapper.SecurePayMapper;
import com.sunmange.unionpay.service.BackRecvService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.sunmange.unionpay.acp.demo.BackRcvResponse.getAllRequestParam;

@Service
public class BackRecvServiceImpl implements BackRecvService {

    private static final Logger logger = LoggerFactory.getLogger(FrontRcvController.class);

    @Autowired
    SecurePayMapper securePayMapper;

    @Override
    public void recvBackRecvResponse(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.writeLog("BackRcvResponse starts to receive background notifications.");

        String encoding = request.getParameter(SDKConstants.param_encoding);
        // Obtain the background notification parameters sent by the UnionPay notification server.
        Map<String, String> reqParam = getAllRequestParam(request);

        LogUtil.printRequestLog(reqParam);

        Map<String, String> valideData = null;
        if (null != reqParam && !reqParam.isEmpty()) {
            Iterator<Map.Entry<String, String>> it = reqParam.entrySet().iterator();
            valideData = new HashMap<String, String>(reqParam.size());
            while (it.hasNext()) {
                Map.Entry<String, String> e = it.next();
                String key = (String) e.getKey();
                String value = (String) e.getValue();

                valideData.put(key, value);
            }
        }

        //Important! Do not modify the content of the key-value pair in reqParam before signature authentication. Otherwise, the signature authentication may fail.
        if (!AcpService.validate(valideData, encoding)) {
            LogUtil.writeLog("Result of signature authentication [Fail].");
            //The signature authentication fails and the problem should be solved.
            logger.info("-----------------------验签失败-----------------------");

        } else {

            logger.info("-----------------------验签成功-----------------------");

            LogUtil.writeLog("Result of signature authentication [Succeed].");
            //Update the state of the client order if the transaction succeeds.

            String orderId = valideData.get("orderId"); //Obtain the data of background notifications and you can obtain other fields similarly.
            String customerInfo = valideData.get("customerInfo");
            if (null != customerInfo) {
                Map<String, String> customerInfoMap = AcpService.parseCustomerInfo(customerInfo, "UTF-8");
                LogUtil.writeLog("Plain text customerInfoMap: " + customerInfoMap);
            }

            String accNo = valideData.get("accNo");
            //If an encryption certificate has been configured for sensitive information, you can use the method below for decryption. No decryption is required if the sensitive has not been encrypted.
            if (null != accNo) {
                accNo = AcpService.decryptData(accNo, "UTF-8");
                LogUtil.writeLog("Plain text accNo: " + accNo);
            }

            String tokenPayData = valideData.get("tokenPayData");
            if (null != tokenPayData) {
                Map<String, String> tokenPayDataMap = null;
                try {
                    tokenPayDataMap = SDKUtil.parseQString(tokenPayData.substring(1, tokenPayData.length() - 1));
                } catch (UnsupportedEncodingException e) {
                    logger.info("-----------------------不支持的编码方式-----------------------");
                    logger.info(e.getMessage());
                }
                String token = tokenPayDataMap.get("token");//Obtain
                LogUtil.writeLog("Plain text tokenPayDataMap: " + tokenPayDataMap);
            }

            String respCode = valideData.get("respCode");
            //After determining that respCode=00 or A6, you are recommended to query the interface and update the database after confirming that the transaction is successful for a transaction with capital involved.
            String trading_status = "0";

            if (respCode.equals("00")) {
                trading_status = "1";
                logger.info("-----------------------交易成功-----------------------");
            } else {
                trading_status = "2";
                logger.info("-----------------------交易失败-----------------------");
            }

            JSONObject unpy_tran = new JSONObject();
            unpy_tran.put("trading_status", trading_status);
            unpy_tran.put("orderId", orderId);

            int i = securePayMapper.uptTranst(unpy_tran);
            if (i != 1) {
                logger.info("-----------------------backRcvResponse更新unpy_tran表交易状态失败-----------------------");
            } else {
                logger.info("-----------------------backRcvResponse更新unpy_tran表交易状态成功-----------------------");
            }
        }
        LogUtil.writeLog("BackRcvResponse ends to receive background notifications.");
    }
}
