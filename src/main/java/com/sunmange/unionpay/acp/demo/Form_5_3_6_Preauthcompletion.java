package com.sunmange.unionpay.acp.demo;

import com.sunmange.unionpay.acp.sdk.AcpService;
import com.sunmange.unionpay.acp.sdk.LogUtil;
import com.sunmange.unionpay.acp.sdk.SDKConfig;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Pre-authorization completion transaction
 */
public class Form_5_3_6_Preauthcompletion extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        /**
         * Initialize the parameters related to requesting UnionPay access address as well as obtaining certificate files and certificate paths to the SDKConfig class.
         * Be sure to load such parameters each time you run them in manner of java main.
         * In case of Web application development, this method can be written into cache by means of monitoring, and does not need to appear here.
         */
        //Here, the method for loading a property file has been moved to web/AutoLoadServlet.java.
        //SDKConfig.getConfig().loadPropertiesFromSrc(); //Load the acp_sdk.properties file from classpath
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String origQryId = req.getParameter("origQryId");
        String txnAmt = req.getParameter("txnAmt");
        String merId = req.getParameter("merId");

        Map<String, String> data = new HashMap<String, String>();

        /***For an all-channel UnionPay system, all parameters can be left unchanged except the one encoding, which you need to set as required.***/
        data.put("version", DemoBase.version);            //M version number
        data.put("encoding", DemoBase.encoding);          //M character set code: Both UTF-8 and GBK can be used.
        data.put("signMethod", SDKConfig.getConfig().getSignMethod()); //M signature method
        data.put("txnType", "03");                        //M transaction type 03-pre-authorization completed
        data.put("txnSubType", "00");                     //M transaction subtype  00 by default
        //000301: Merchant-hosted
        //	000000: ExpressPay
        //	000902: Token payment
        //	001001: MOTO
        data.put("bizType", "000201");                    //M business type
        //07: Internet
        //08: Mobile
        data.put("channelType", "07");                      //M
        //0: Merchant direct access
        //1: Acquirer access
        //2: Platform merchant access
        data.put("accessType", "0");                         //M access type: Always fill in 0 here and make no modification in case of client access.

        data.put("merId", merId);             //M client number: Please modify it to the client number you have applied or the test client number 777 you have registered at open.unionpay.com.

        data.put("orderId", DemoBase.getOrderId());       //M client order number, consisting of 8-40 alphanumeric characters, no “-” or “_” is allowed, but custom rules are allowed. It is an order number regenerated, different from the one of the original purchase transaction.

        data.put("txnTime", DemoBase.getCurrentTime());   //M order delivery time: It must be in format of YYYYMMDDhhmmss. Be sure to use the current time. Otherwise, an error of invalid txnTime will be reported.

        data.put("txnAmt", txnAmt);                       //M [deal amount]: The amount should be 0-115% of the pre-authorized amount.

        data.put("currencyCode", "156");                  //M transaction currency (for domestic clients, it is usually 156, which indicates RMB)
        //data.put("reqReserved", "Transparent transmit information");                 //It is a domain reserved by the requester and you can enable it if you want to use it. Transparent transmit field (can be used to track client's custom parameters): you can use this field to query the background notifications and transaction state of this transaction, and such information will be returned as it was in the reconciliation file. The client can upload such information as required and the length of the information to be uploaded should be in range of 1-1024 bytes. The occurrence of symbols like &, =, {}, and [] may cause failure to parsing response messages from the query interface. Therefore, you are recommended to transmit only alphanumeric characters and use | for separation. Or, you can conduct a base64 encoding at the outermost layer (a “=” appears after a base64 encoding can be neglected because it will not cause parse failure).
        data.put("backUrl", DemoBase.backUrl);            //M background notification address. For details about background notification parameters, refer to Help Center > Download > Product Interface Specifications > Interface Specifications for Gateway Payment Products > Purchase Cancellation Transactions > Client Notification at open.unionpay.com. The other descriptions are the same as client notifications of purchase transactions.


        /***To debug a transaction so that it succeeds, you must modify the fields below.***/
        data.put("origQryId", origQryId);              //M [Original transaction serial number]: queryId returned by the original purchase transaction, which can be obtained from the background notification interface or transaction state query interface for purchase transaction.

        /**All request parameters have been set successfully. Now, let's sign the requested parameters, and send http.post requests and receive synchronous response messages.**/

        Map<String, String> reqData = AcpService.sign(data, DemoBase.encoding);//In a message, the values of certId and signature are obtained from the signData method and are assigned with values automatically. Therefore, you just need to ensure that the certificate is correctly configured.
        String url = SDKConfig.getConfig().getBackRequestUrl();//At the url of the transaction request, you can read the acpsdk.backTransUrl in the corresponding property file acp_sdk.properties from the configuration file.

        Map<String, String> rspData = AcpService.post(reqData, url, DemoBase.encoding);//Send request messages and receive synchronous responses (the default connection timeout is 30s and the timeout for reading the returned result is 30s); Here, after calling signData, do not make any modification to the value of any key in submitFromData before calling submitUrl. Any such modification may cause failure to signature authentication.
        /**To proceed the response codes, you need to compile a program based on your business logics. The logics for response code processing below are for reference only.------------->**/
        //For response code specifications, refer to Part 5 “Appendix” in Specifications for Platform Access Interfaces- by selecting Help Center > Download > Product Interface Specifications at open.unionpay.com.
        if (!rspData.isEmpty()) {
            if (AcpService.validate(rspData, DemoBase.encoding)) {
                LogUtil.writeLog("Signature authentication succeeds");
                String respCode = rspData.get("respCode");
                if ("00".equals(respCode)) {
                    //Transaction accepted (this does not mean that the transaction is successful). When the transaction is in this state, you can choose to wait for the background notification to determine whether the transaction is successful, or actively initiate a transaction query to determine the transaction state.
                    //TODO
                } else if ("03".equals(respCode) ||
                        "04".equals(respCode) ||
                        "05".equals(respCode)) {
                    //Also, you can initiate a transaction state query later to determine the transaction state.
                    //TODO
                } else {
                    //Other response codes are failure. Please find the cause.
                    //TODO
                }
            } else {
                LogUtil.writeErrorLog("Signature authentication fails");
                //TODO Find the reason why the signature authentication fails
            }
        } else {
            //The returned http state code is incorrect.
            LogUtil.writeErrorLog("No returned message is obtained or the returned http state code is not 200.");
        }
        String reqMessage = DemoBase.genHtmlResult(reqData);
        String rspMessage = DemoBase.genHtmlResult(rspData);
        resp.getWriter().write("</br>Request message:<br/>" + reqMessage + "<br/>" + "Response message:</br>" + rspMessage + "");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // TODO Auto-generated method stub

        doPost(req, resp);
    }
}
