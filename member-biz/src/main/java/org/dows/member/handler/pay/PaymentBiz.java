package org.dows.member.handler.pay;

import jakarta.servlet.http.HttpServletRequest;
import org.dows.member.request.pay.CreateNativePayQrCodeRequest;
import org.dows.member.response.pay.PayQrCodeResponse;

/**
 * 充值记录（充值订单）
 */
public interface PaymentBiz {

    /**
     * 创建支付信息
     * @return 支付二维码链接
     */
    PayQrCodeResponse createNativePayment(CreateNativePayQrCodeRequest request);
}
