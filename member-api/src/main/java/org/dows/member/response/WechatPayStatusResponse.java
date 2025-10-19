package org.dows.member.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.ibatis.transaction.Transaction;

@Data
@Schema(description = "微信支付订单状态查询请求")
public class WechatPayStatusResponse {

    @Schema(description = "支付状态")
    private String tradeState;

    @Schema(description = "状态描述")
    private String tradeStateDesc;

    @Schema(description = "支付成功时间")
    private String successTime;
}