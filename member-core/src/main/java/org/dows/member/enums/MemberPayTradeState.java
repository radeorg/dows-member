package org.dows.member.enums;

import lombok.Getter;

/**
 * 微信支付交易状态枚举
 * 对应微信支付APIv3中的trade_state字段
 */
@Getter
public enum MemberPayTradeState {

    /**
     * 支付成功
     * 业务处理建议：更新订单状态为"已支付"，触发后续业务流程（如发货、通知等）
     */
    SUCCESS("SUCCESS", "支付成功"),

    /**
     * 转入退款
     * 业务处理建议：处理退款相关逻辑，核对退款金额与订单金额
     */
    REFUND("REFUND", "转入退款"),

    /**
     * 未支付
     * 业务处理建议：继续轮询查询（建议间隔3-5秒，总时长不超过120秒）或提示用户完成支付
     */
    NOT_PAY("NOT_PAY","未支付"),

    /**
     * 已关闭
     * 业务处理建议：结束轮询，提示用户订单已关闭，可引导重新下单
     */
    CLOSED("CLOSED","已关闭"),

    /**
     * 用户支付中
     * 业务处理建议：继续轮询查询（建议间隔2-3秒，最多轮询5次）
     */
    USER_PAYING("","用户支付中"),

    /**
     * 支付失败
     * 业务处理建议：记录错误日志，提示用户支付失败，可引导重新支付
     */
    PAY_ERROR("PAYERROR","支付失败");

    private final String code;
    private final String description;

    MemberPayTradeState(String code, String description) {
        this.code = code;
        this.description = description;
    }
}