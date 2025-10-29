package org.dows.member.pay;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.dows.member.api.pay.WechatPayApi;
import org.dows.member.biz.pay.PaymentBiz;
import org.dows.member.enums.PayChannelEnum;
import org.dows.member.exception.MemberException;
import org.dows.member.request.pay.PayQrCodeRequest;
import org.dows.member.response.pay.PayQrCodeResponse;
import org.dows.member.response.pay.WxPayStatusResponse;
import org.dows.rade.aac.AacContext;
import org.dows.rade.aac.AacUser;
import org.dows.rade.context.AppContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@Tag(name = "微信支付", description = "微信支付")
@RequiredArgsConstructor
public class WechatPayRest implements WechatPayApi {

    private final AacContext aacContext;
    private final PaymentBiz paymentBiz;

    @Override
    public PayQrCodeResponse wechatPayQrCode(PayQrCodeRequest request) {
        request.setAccountInstanceId(getAccountId());
        request.setAppId(AppContext.getAppId());
        request.setPayChannel(PayChannelEnum.WECHAT.getCode());

        return paymentBiz.createNativePayment(request);
    }

    @Override
    public ResponseEntity<String> wechatPayNotify(HttpServletRequest request) throws IOException {
        String signature = request.getHeader("Wechatpay-Signature");
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        String nonce = request.getHeader("Wechatpay-Nonce");
        String serial = request.getHeader("Wechatpay-Serial");
        String body = request.getReader().lines().collect(Collectors.joining()); // 原始请求体

        paymentBiz.wxPayNotify(signature, timestamp, nonce, serial, body);

        return ResponseEntity.ok("200");
    }

    @Override
    public WxPayStatusResponse wechatPayStatus(String outTradeNo) {
        return paymentBiz.wxPayStatus(outTradeNo);
    }

    private Long getAccountId() {
        AacUser aacUser = aacContext.getAacUser();
        if (Objects.isNull(aacUser) || Objects.isNull(aacUser.getAccountId())) {
            throw new MemberException("登录账号不存在");
        }
        return aacUser.getAccountId();
    }
}