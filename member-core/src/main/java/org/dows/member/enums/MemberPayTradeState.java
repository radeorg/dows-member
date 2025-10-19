package org.dows.member.enums;

/**
 * 微信支付交易状态枚举
 * 对应微信支付APIv3中的trade_state字段
 */
public enum MemberPayTradeState {

    /**
     * 支付成功
     * 业务处理建议：更新订单状态为"已支付"，触发后续业务流程（如发货、通知等）
     */
    SUCCESS("支付成功"),

    /**
     * 转入退款
     * 业务处理建议：处理退款相关逻辑，核对退款金额与订单金额
     */
    REFUND("转入退款"),

    /**
     * 未支付
     * 业务处理建议：继续轮询查询（建议间隔3-5秒，总时长不超过120秒）或提示用户完成支付
     */
    NOTPAY("未支付"),

    /**
     * 已关闭
     * 业务处理建议：结束轮询，提示用户订单已关闭，可引导重新下单
     */
    CLOSED("已关闭"),

    /**
     * 用户支付中
     * 业务处理建议：继续轮询查询（建议间隔2-3秒，最多轮询5次）
     */
    USERPAYING("用户支付中"),

    /**
     * 支付失败
     * 业务处理建议：记录错误日志，提示用户支付失败，可引导重新支付
     */
    PAYERROR("支付失败");

    private final String desc;

    /**
     * 构造方法
     * @param desc 交易状态业务描述
     */
    MemberPayTradeState(String desc) {
        this.desc = desc;
    }

    /**
     * 获取交易状态描述
     * @return 业务含义说明
     */
    public String getDesc() {
        return desc;
    }
}