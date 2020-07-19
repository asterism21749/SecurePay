package com.sunmange.unionpay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sunmange.unionpay.acp.sdk.AcpService;
import com.sunmange.unionpay.acp.sdk.LogUtil;
import com.sunmange.unionpay.acp.sdk.SDKConstants;
import com.sunmange.unionpay.acp.sdk.SDKUtil;
import com.sunmange.unionpay.controller.FrontRcvController;
import com.sunmange.unionpay.mapper.SecurePayMapper;
import com.sunmange.unionpay.service.FrontRecvService;
import com.sunmange.unionpay.util.SecurePayUtil;
import com.sunmange.unionpay.util.constants.Constants;
import com.sunmange.unionpay.util.constants.ErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.sunmange.unionpay.acp.demo.FrontRcvResponse.getAllRequestParam;

@Component
public class FrontRecvServiceImpl implements FrontRecvService {

    private static final Logger logger = LoggerFactory.getLogger(FrontRcvController.class);

    @Autowired
    SecurePayUtil securePayUtils;

    @Autowired
    SecurePayMapper securePayMapper;

    @Override
    public String recvPost(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {

        logger.info("-----------------------开始进行frontRcvResponse业务处理-----------------------");

        StringBuffer page = new StringBuffer();

        logger.info("-----------------------获取第三方接口地址-----------------------");

        String subsys = "unionpay";
        String chanel = "SecurePay";
        JSONObject params = securePayMapper.getUnionPayParams(subsys, chanel);

        String thrdip = params.getString("thrdip");

        logger.info("-----------------------初始化返回页面-----------------------");

        page.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head><body><form id=\"pay_form\" action=\"" + thrdip + "\" method=\"post\">");

        logger.info("-----------------------初始化返回信息-----------------------");

        String respcd = Constants.SUCCESS_CODE;
        String resptx = Constants.SUCCESS_MSG;
        String orderId = "";
        String txnTime = "";
        String txnAmt = "";

        LogUtil.writeLog("FrontRcvResponse: The foreground receives a message and returns “Start”.");

        String encoding = req.getParameter(SDKConstants.param_encoding);
        LogUtil.writeLog("in the returned message encoding=[" + encoding + "]");

        Map<String, String> respParam = getAllRequestParam(req);

        // Print the request message
        LogUtil.printRequestLog(respParam);

        Map<String, String> valideData = null;
        if (null != respParam && !respParam.isEmpty()) {
            Iterator<Map.Entry<String, String>> it = respParam.entrySet()
                    .iterator();
            valideData = new HashMap<String, String>(respParam.size());
            while (it.hasNext()) {
                Map.Entry<String, String> e = it.next();
                String key = (String) e.getKey();
                String value = (String) e.getValue();
                valideData.put(key, value);
            }
        }
        if (!AcpService.validate(valideData, encoding)) {
            LogUtil.writeLog("Result of signature authentication [Fail].");
//            验签失败
            logger.info("-----------------------银联返回报文 验签失败-----------------------");

            respcd = ErrorEnum.E_2013.getErrorCode();
            resptx = ErrorEnum.E_2013.getErrorMsg();

        } else {
            logger.info("-----------------------银联返回报文 验签成功-----------------------");
            orderId = valideData.get("orderId");

            txnTime = valideData.get("txnTime");

            txnAmt = valideData.get("txnAmt");

            LogUtil.writeLog("Result of signature authentication [Succeed].");
            System.out.println(valideData.get("orderId")); //Other fields can be obtained similarly.

            //To obtain a transaction with a token number, you need to parse the tokenPayData domain.
            String customerInfo = valideData.get("customerInfo");
            if (null != customerInfo) {
                Map<String, String> customerInfoMap = AcpService.parseCustomerInfo(customerInfo, "UTF-8");
            }

//			String accNo = valideData.get("accNo");
//			//If the returned card number is a ciphertext one, you can use the method below for decryption. This step can be neglected if the returned card number is not a ciphertext one.
//			if(null!= accNo){
//				accNo = AcpService.decryptData(accNo, "UTF-8");
//				page.append("<br>Plain text accNo: "+accNo);
//			}

            String tokenPayData = valideData.get("tokenPayData");
            if (null != tokenPayData) {
                Map<String, String> tokenPayDataMap = SDKUtil.parseQString(tokenPayData.substring(1, tokenPayData.length() - 1));
                String token = tokenPayDataMap.get("token");//Obtain
            }

            String respCode = valideData.get("respCode");
            String respMsg = valideData.get("respMsg");

            //After determining that respCode=00 or A6, you are recommended to query the interface and update the database after confirming that the transaction is successful for a transaction with capital involved.
            if (!respCode.equals("00")) {
                logger.info("-----------------------银联返回 交易失败-----------------------");

                respcd = "00" + respCode;
                resptx = respMsg;
            }

        }

        LogUtil.writeLog("FrontRcvResponse: The foreground receives a message and returns “End”.");

        logger.info("-----------------------更新unpy_tran表-----------------------");

        //登记unpy_tran表
        JSONObject unpy_tran = JSONObject.parseObject(JSON.toJSONString(valideData));

        int i = securePayMapper.uptUnionPayTran(unpy_tran);

        if (i != 1) {
            logger.info("-----------------------更新unpy_tran表 失败-----------------------");
            respcd = ErrorEnum.E_1013.getErrorCode();
            resptx = ErrorEnum.E_1013.getErrorMsg();
        }

        page.append("<input type=\"hidden\" name=\"respcd\" id=\"respcd\" value=\"" + respcd + "\"/>");
        page.append("<input type=\"hidden\" name=\"resptx\" id=\"resptx\" value=\"" + resptx + "\"/>");
        page.append("<input type=\"hidden\" name=\"orderId\" id=\"orderId\" value=\"" + orderId + "\"/>");
        page.append("<input type=\"hidden\" name=\"txnTime\" id=\"txnTime\" value=\"" + txnTime + "\"/>");
        page.append("<input type=\"hidden\" name=\"txnAmt\" id=\"txnAmt\" value=\"" + txnAmt + "\"/>");

        page.append("</form></body><script type=\"text/javascript\">document.all.pay_form.submit();</script></html>");

        JSONObject outData = securePayUtils.successJson(unpy_tran);
        outData.put("rpdata", page.toString());
        i = securePayMapper.uptRequest(outData);

        if (i != 1) {
            logger.info("-----------------------更新unpy_logs表 失败-----------------------");
        }

        logger.info("-----------------------返回页面为-----------------------\r\n" + page.toString());

        return page.toString();
    }
}
