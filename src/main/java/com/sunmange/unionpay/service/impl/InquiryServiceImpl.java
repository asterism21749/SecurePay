package com.sunmange.unionpay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sunmange.unionpay.acp.demo.DemoBase;
import com.sunmange.unionpay.acp.sdk.AcpService;
import com.sunmange.unionpay.acp.sdk.LogUtil;
import com.sunmange.unionpay.acp.sdk.SDKConfig;
import com.sunmange.unionpay.mapper.SecurePayMapper;
import com.sunmange.unionpay.service.InquiryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InquiryServiceImpl implements InquiryService {

    private static final Logger logger = LoggerFactory.getLogger(InquiryServiceImpl.class);

    @Autowired
    SecurePayMapper securePayMapper;

    @Override
    public JSONObject purchaseStateInquiry(JSONObject inData) {

        logger.info("-----------------------取商户号-----------------------");
        //获取商户号
        String subsys = "unionpay";
        String chanel = "SecurePay";
        JSONObject params = securePayMapper.getUnionPayParams(subsys, chanel);
        String merId = params.getString("merId");

        String orderId = inData.getString("orderId");
        String txnTime = inData.getString("txnTime");

        Map<String, String> data = new HashMap<String, String>();

        /***For an all-channel UnionPay system, all parameters can be left unchanged except the one encoding, which you need to set as required.***/
        data.put("version", DemoBase.version);                 //M
        data.put("encoding", DemoBase.encoding);               //M
        data.put("signMethod", SDKConfig.getConfig().getSignMethod()); //M
        // Value: 00
        data.put("txnType", "00");// M
        // 00: PAN query (default)
        // 01: Mobile number query (Merchant direct access mode)
        // 02: Merchant order number query
        data.put("txnSubType", "00");// M
        // Product type
        data.put("bizType", "000201");// M 000000
        // 0: Merchant direct access
        // 1: Acquirer Access
        data.put("accessType", "0");// M

        data.put("merId", merId);                               //M client number: Please modify it to the client number you have applied or the test client number 777 you have registered at open.unionpay.com.

        /***要调通交易以下字段必须修改***/
        data.put("orderId", orderId);                 //M**** client order number: It should be modified to the order number of the transaction queried for each transaction test.
        data.put("txnTime", txnTime);                 //M**** order delivery time: It should be modified to the order delivery time of the transaction queried for each transaction test.

        /**All request parameters have been set successfully. Now, let's sign the requested parameters, and send http.post requests and receive synchronous response messages.------------->**/

        Map<String, String> reqData = AcpService.sign(data, DemoBase.encoding);           //In a message, the values of certId and signature are obtained from the signData method and are assigned with values automatically. Therefore, you just need to ensure that the certificate is correctly configured.
        String url = SDKConfig.getConfig().getSingleQueryUrl();                                  //At the url of the transaction request, you can read the acpsdk.singleQueryUrl in the corresponding property file acp_sdk.properties from the configuration file.
        Map<String, String> rspData = AcpService.post(reqData, url, DemoBase.encoding);     //Send request messages and receive synchronous responses (the default connection timeout is 30s and the timeout for reading the returned result is 30s); Here, after calling signData, do not make any modification to the value of any key in submitFromData before calling submitUrl. Any such modification may cause failure to signature authentication.

        /**To proceed the response codes, you need to compile a program based on your business logics. The logics for response code processing below are for reference only.------------->**/
        //For response code specifications, refer to Part 5 “Appendix” in Specifications for Platform Access Interfaces- by selecting Help Center > Download > Product Interface Specifications at open.unionpay.com.
        if (!rspData.isEmpty()) {
            if (AcpService.validate(rspData, DemoBase.encoding)) {
                LogUtil.writeLog("Signature authentication succeeds");
                if (("00").equals(rspData.get("respCode"))) {//If the transaction query succeeds,
                    String origRespCode = rspData.get("origRespCode");
                    if (("00").equals(origRespCode)) {
                        //Update the state of the client order if the transaction succeeds.
                        //TODO
                    } else if (("03").equals(origRespCode) ||
                            ("04").equals(origRespCode) ||
                            ("05").equals(origRespCode)) {
                        //The order is being processed or the transaction state is unknown. In this case, you need to initiate a transaction state query later. [If you cannot determine whether the transaction is successful at the end, please use the transaction state in the reconciliation file.]
                        //TODO
                    } else {
                        //Other response codes are transaction failure.
                        //TODO
                    }
                } else if (("34").equals(rspData.get("respCode"))) {
                    //The order does not exist. In this case, you can initiate a transaction state query later, or use the transaction state in the reconciliation file.

                } else {//If the transaction query fails and the response code is 10/11, you need to check whether the message is correct.
                    //TODO
                }
            } else {
                LogUtil.writeErrorLog("Signature authentication fails");
                //TODO Find the reason why the signature authentication fails
                logger.info("-----------------------验签失败-----------------------");
            }
        } else {
            //The returned http state code is incorrect.
            LogUtil.writeErrorLog("No returned message is obtained or the returned http state code is not 200.");
        }

        String reqMessage = DemoBase.genHtmlResult(reqData);
        String rspMessage = DemoBase.genHtmlResult(rspData);

        return null;
    }
}
