package org.dows.member.handler.pay;

import com.alipay.api.AlipayApiException;
import org.dows.member.request.pay.AliPayQrCodeRequest;
import org.dows.member.response.pay.AliPayStatusResponse;
import org.dows.member.response.pay.PayQrCodeResponse;

import java.util.Map;

public interface AliPayBiz {

    /**
     * 创建支付宝支付订单接口，返回支付二维码连接
     */
    PayQrCodeResponse aliPayQrCode(AliPayQrCodeRequest request);

    /**
     * 验签
     */
    boolean verifyNotify(Map<String, String> params);

    /**
     * 查询支付宝支付订单状态：
     * WAIT_BUYER_PAY(待付款)
     * TRADE_SUCCESS(支付成功)
     * TRADE_CLOSED(未付款交易超时关闭，或支付完成后全额退款)
     * TRADE_FINISHED(交易结束，不可退款)
     */
    AliPayStatusResponse aliPayStatus(String outTradeNo);

    /**
     * 取消支付
     */
    void cancelPay(String outTradeNo) throws AlipayApiException;
}