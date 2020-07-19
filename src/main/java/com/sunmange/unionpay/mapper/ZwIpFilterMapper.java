package com.sunmange.unionpay.mapper;

import com.sunmange.unionpay.model.ZwIpFilter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ZwIpFilterMapper {

    @Select("select * from unpy_ipft where ip = #{ip} and mark = #{mark} and module = #{module}")
    List<ZwIpFilter> filter(ZwIpFilter zwIpFilter);
}

