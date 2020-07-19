package com.sunmange.unionpay.mapper;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface SecurePayMapper {


    //获取发送银联参数
    @Select("select param1 merId,param2 thrdip,param3 acqInsCode from unpy_para where subsys = #{subsys} and chanel = #{chanel}")
    JSONObject getUnionPayParams(@Param("subsys") String subsys, @Param("chanel") String chanel);

    //插入unpy_log表记录
    @Insert("insert into `sunmange`.`unpy_logs`(`fcbpdt`, `fcbpsq`,`rqdata`,`orderId`) values (#{fcbpdt},#{fcbpsq},#{rqdata},#{orderId})")
    int regRequest(JSONObject jsonObject);

    //插入unpy_tran表记录
    @Insert("INSERT INTO `sunmange`.`unpy_tran` (`fcbpdt`,`fcbpsq`,`version`,`encoding`,`certId`,`signature`,`signMethod`,`txnType`,`txnSubType`,`bizType`,`accessType`,`channelType`,`frontUrl`,`backUrl`,`merId`,`merCatCode`,`merName`,`merAbbr`,`orderId`,`txnTime`,`accNo`,`txnAmt`,`currencyCode`,`customerInfo`,`frontFailUrl`,`orderDesc`) VALUES (#{fcbpdt}, #{fcbpsq},  #{version}, #{encoding}, #{certId}, #{signature}, #{signMethod}, #{txnType}, #{txnSubType}, #{bizType}, #{accessType}, #{channelType}, #{frontUrl}, #{backUrl},  #{merId}, #{merCatCode}, #{merName}, #{merAbbr}, #{orderId}, #{txnTime}, #{accNo}, #{txnAmt},#{currencyCode}, #{customerInfo}, #{frontFailUrl},  #{orderDesc})")
    int regUnionPayTran(JSONObject jsonObject);

    //更新unpy_log记录
    @Update("update `sunmange`.`unpy_logs` set rpdata = #{rpdata} , respcd = #{respcd} ,resptx = #{resptx} where orderId = #{orderId}")
    int uptRequest(JSONObject jsonObject);

    //更新unpy_tran记录
    @Update("update `sunmange`.`unpy_tran` set respCode = #{respCode} , respMsg = #{respMsg}, settleDate = #{settleDate}, settleCurrencyCode = #{settleCurrencyCode} , settleAmt = #{settleAmt} , traceNo = #{traceNo} ,queryId = #{queryId}, traceTime = #{traceTime} where orderId = #{ orderId}")
    int uptUnionPayTran(JSONObject jsonObject);

    //修改交易状态
    @Update("update `sunmange`.`unpy_tran` set transt = #{trading_status} where orderId = #{orderId}")
    int uptTranst(JSONObject jsonObject);

    //获取当日最大流水号
    @Select("select fcbpsq from unpy_logs where fcbpdt = #{fcbpdt} order by fcbpsq desc limit 1 for update")
    String queryMaxFcbpsq(String fcbpdt);
}
