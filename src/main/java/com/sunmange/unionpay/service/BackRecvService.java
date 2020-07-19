package com.sunmange.unionpay.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface BackRecvService {

    void recvBackRecvResponse(HttpServletRequest request, HttpServletResponse response);
}
