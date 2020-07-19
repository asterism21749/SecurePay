package com.sunmange.unionpay.controller;

import com.sunmange.unionpay.service.BackRecvService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

@Controller
public class BackRcvController {

    private static final Logger logger = LoggerFactory.getLogger(FrontRcvController.class);

    @Autowired
    BackRecvService backRecvService;

    @RequestMapping(value = "/backRcvResponse")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        logger.info("-----------------------接收backRcvResponse请求-----------------------");

        backRecvService.recvBackRecvResponse(req, resp);

        //Return the status code http 200 to the UnionPay server
        resp.getWriter().print("ok");

        logger.info("-----------------------结束backRcvResponse请求-----------------------");
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        this.doPost(req, resp);
    }

    /**
     * Obtain all the information in the requested parameters
     *
     * @param request
     * @return
     */
    public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
                //When sending messages, do not send the content below if the value of the field is null: What should be done next is deleting this field if you determine that its value is null when obtaining all parameter data.
                //System.out.println("Line 247 of the ServletUtil class  key=="+en+"      and value ==="+value of temp data );
                if (null == res.get(en) || "".equals(res.get(en))) {
                    res.remove(en);
                }
            }
        }
        return res;
    }
}
