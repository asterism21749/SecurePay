package com.sunmange.unionpay.interceptor;

import com.sunmange.unionpay.mapper.ZwIpFilterMapper;
import com.sunmange.unionpay.model.ZwIpFilter;
import com.sunmange.unionpay.util.IPUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class IPInterceptor implements HandlerInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(IPInterceptor.class.getName());


    @Autowired
    private ZwIpFilterMapper ipFilterMapper;

    private ZwIpFilter ipFilter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //过滤ip,若用户在白名单内，则放行
        String ipAddress = IPUtils.getRealIP(request);
        LOG.info("USER IP ADDRESS IS =>" + ipAddress);
        if (!StringUtils.isNotBlank(ipAddress))
            return false;
        ipFilter = new ZwIpFilter();
        ipFilter.setModule("unpy");//模块
        ipFilter.setIp(ipAddress);//ip地址
        ipFilter.setMark(0);//白名单
        List<ZwIpFilter> ips = ipFilterMapper.filter(ipFilter);
        if (ips.isEmpty()) {
            response.getWriter().append("<h1 style=\"text-align:center;\">Not allowed!</h1>");
            return false;
        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private static final Logger logger = LoggerFactory.getLogger(IPInterceptor.class);

}
