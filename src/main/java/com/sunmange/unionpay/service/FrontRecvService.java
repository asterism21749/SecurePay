package com.sunmange.unionpay.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

public interface FrontRecvService {

    String recvPost(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException;
}
