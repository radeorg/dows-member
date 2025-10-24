package org.dows.member.handler.pay;

import org.dows.member.request.pay.PayQrCodeRequest;
import org.dows.member.response.pay.AliPayStatusResponse;
import org.dows.member.response.pay.PayQrCodeResponse;

import java.util.Map;

/**
 * 充值记录（充值订单）
 */
public interface PaymentBiz {

    /**
     * 创建支付信息
     * @return 支付二维码链接
     */
    PayQrCodeResponse createNativePayment(PayQrCodeRequest request);

    /**
     * 支付宝回调
     */
    String aliPayNotify(Map<String, String> params);

    /**
     * 查询支付宝支付状态
     */
    AliPayStatusResponse aliPayStatus(String outTradeNo);
}
