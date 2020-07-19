package com.sunmange.unionpay.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sunmange.unionpay.mapper.SecurePayMapper;
import com.sunmange.unionpay.util.constants.Constants;
import com.sunmange.unionpay.util.constants.ErrorEnum;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

@Component
public class SecurePayUtil {

    private static final Logger logger = LoggerFactory.getLogger(SecurePayUtil.class);

    @Autowired
    SecurePayMapper securePayMapper;

    //流水号加1后返回，流水号长度为12
    private static final String STR_FORMAT = "000000000000";

    /**
     * 获取订单时间
     *
     * @return
     */
    public String getTxnTime() {

        String txnTime = "";

//        YYYYMMDDhhmmss
        String format = "YYYYMMddHHmmss";
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        txnTime = dateFormat.format(now);

        return txnTime;
    }

    /**
     * 获取前置日期
     *
     * @return
     */
    public String getFcbpdt() {

        String fcbpdt = "";

//        YYYYMMDDhhmmss
        String format = "YYYYMMdd";
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        fcbpdt = dateFormat.format(now);

        return fcbpdt;
    }

    /**
     * 获取前置流水
     *
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String getFcbsq(String fcbpdt) {

        String fcbpsq = securePayMapper.queryMaxFcbpsq(fcbpdt);

        //默认从0开始
        if (fcbpsq == null || StringUtils.isEmpty(fcbpsq)) {
            fcbpsq = "0";
        }

        //流水号+1
        fcbpsq = String.valueOf(Integer.parseInt(fcbpsq) + 1);

        //长度小于12，左补零
        if (fcbpsq.length() < 12) {
            Integer fcbpsq_int = Integer.parseInt(fcbpsq);
            DecimalFormat df = new DecimalFormat(STR_FORMAT);
            fcbpsq = df.format(fcbpsq_int);
        }

        //长度大于12，截取后12位
        if (fcbpsq.length() > 12) {
            fcbpsq.substring(fcbpsq.length() - 12);
        }

        return fcbpsq;
    }

    /**
     * 返回一个返回码为100的json
     */
    public JSONObject successJson(JSONObject inData) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("respcd", Constants.SUCCESS_CODE);
        resultJson.put("resptx", Constants.SUCCESS_MSG);
        resultJson.put("orderId", inData.getString("orderId"));
        return resultJson;
    }

    /**
     * 返回错误信息JSON
     */
    public JSONObject errorJson(ErrorEnum errorEnum) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("respcd", errorEnum.getErrorCode());
        resultJson.put("resptx", errorEnum.getErrorMsg());
        return resultJson;
    }

    /**
     * 返回错误信息JSON
     */
    public JSONObject errorJson(String respcd, String resptx) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("respcd", respcd);
        resultJson.put("resptx", resptx);
        resultJson.put("info", new JSONObject());
        return resultJson;
    }


    public void printMap(Map map) {

        JSONObject json = JSONObject.parseObject(JSON.toJSONString(map));

        logger.info("-----------------------开始 打印Map-----------------------\r\n");

        logger.info(json.toJSONString());

        logger.info("-----------------------结束 打印Map-----------------------\r\n");


    }

    /**
     * 金额转换，金额*100
     *
     * @param tranam
     * @return
     */
    public String dealTranAmount(String tranam) {


        DecimalFormat df1 = new DecimalFormat("0");
        String txnAmt_str = df1.format(Double.parseDouble(tranam) * 100);

        return txnAmt_str;
    }


    /**
     * 将request参数值转为json
     */
    public JSONObject request2Json(HttpServletRequest request) {
        JSONObject requestJson = new JSONObject();
        Enumeration paramNames = request.getParameterNames();
//        JSONObject result = new JSONObject();;
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] pv = request.getParameterValues(paramName);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pv.length; i++) {
                if (pv[i].length() > 0) {
                    if (i > 0) {
                        sb.append(",");
                    }
                    sb.append(pv[i]);
                }
            }
//            result =  JSONObject.parseObject(String.valueOf(sb));
            requestJson.put(paramName, sb.toString());
        }
        return requestJson;
    }

}
