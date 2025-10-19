package org.dows.member.handler.pay.impl;

import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import com.wechat.pay.java.service.payments.nativepay.model.QueryOrderByOutTradeNoRequest;
import com.wechat.pay.java.service.weixinpayscanandride.model.TradeState;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.dows.member.config.WxPayConfig;
import org.dows.member.handler.pay.WechatPayHandler;
import org.dows.member.handler.util.SignatureUtils;
import org.dows.member.handler.util.XmlUtils;
import org.dows.member.response.WechatPayQrCodeResponse;
import org.dows.member.response.WechatPayStatusResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WechatPayHandlerImpl implements WechatPayHandler {
    private final NativePayService nativePayService;

    @Override
    public WechatPayQrCodeResponse wechatPayQrCode(String outTradeNo, BigDecimal totalAmount, String description) {
        WechatPayQrCodeResponse qrCodeResponse = new WechatPayQrCodeResponse();
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(totalAmount.multiply(new BigDecimal("100")).intValue()); // 单位：分
        request.setAmount(amount);
        request.setAppid("wxa9d9651ae**** **");
  //      request.setMchid(WxPayConfig.merchantId);
        request.setDescription(description);
        request.setNotifyUrl("https://******/pay/notify");
        request.setOutTradeNo(outTradeNo);
        qrCodeResponse.setOutTradeNo(outTradeNo);
        try {
            PrepayResponse response = nativePayService.prepay(request);
            qrCodeResponse.setQrCode(response.getCodeUrl()); // 返回支付二维码链接
        } catch (Exception e) {
            e.printStackTrace();
        }
        return qrCodeResponse;
    }

    @Override
    public String wechatPayNotify(HttpServletRequest request) {
        // 1. 解析XML请求
        Map<String, String> params = null;
        try {
            params = XmlUtils.parseXml(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 2. 验证签名
        if (!SignatureUtils.verifySignature(params)) {
            return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        }

        // 3. 更新订单状态
        String orderNo = params.get("out_trade_no");


        // 4. 返回SUCCESS
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>";
    }

    @Override
    public WechatPayStatusResponse wechatPayStatus(String outTradeNo) {
        WechatPayStatusResponse wechatPayStatusResponse = new WechatPayStatusResponse();

        // 1. 先查本地数据库
        //  Order order = orderMapper.selectByOutTradeNo(outTradeNo);
//        if (order == null) {
//            throw new BusinessException("订单不存在");
//        }

        // 2. 如果本地状态已明确（成功/关闭），直接返回
//        if ("PAID".equals(order.getStatus()) || "CLOSED".equals(order.getStatus())) {
//            return buildOrderStatusVO(order);
//        }

        // 3. 本地状态未明确，调用微信支付查单接口确认
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setOutTradeNo(outTradeNo);
        request.setMchid(WxPayConfig.merchantId);
        Transaction transaction = nativePayService.queryOrderByOutTradeNo(request);
        String stateDesc = transaction != null ? transaction.getTradeStateDesc() : "订单不存在";
        wechatPayStatusResponse.setTradeStateDesc(stateDesc);
        if (transaction != null && TradeState.SUCCESS.equals(transaction.getTradeState())) {
            // 支付成功，处理后续业务（如更新订单状态、发货等）
            System.out.println("支付成功，微信订单号：" + transaction.getTransactionId());
            wechatPayStatusResponse.setSuccessTime(transaction.getSuccessTime());
            // 4. 更新本地订单状态

        } else {

            // 支付未成功，根据状态做进一步处理
            System.out.println("支付未成功：" + stateDesc);


        }
        return wechatPayStatusResponse;

    }

}