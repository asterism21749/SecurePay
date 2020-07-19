package com.sunmange.unionpay.controller;

import com.sunmange.unionpay.service.impl.FrontRecvServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author binhy
 * @desc
 * @date 2019/9/18
 */
@Controller
public class FrontRcvController {

    private static final Logger logger = LoggerFactory.getLogger(FrontRcvController.class);


    @Autowired
    FrontRecvServiceImpl frontRecvService;

    @RequestMapping(value = "/frontRcvResponse")
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        logger.info("-----------------------接收frontRcvResponse请求-----------------------");

        resp.getWriter().write(frontRecvService.recvPost(req, resp));

        logger.info("-----------------------已转发至第三方接口-----------------------");

    }
}
