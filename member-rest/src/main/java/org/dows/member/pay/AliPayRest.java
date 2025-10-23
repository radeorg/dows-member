package org.dows.member.pay;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.member.api.pay.AliPayApi;
import org.dows.member.enums.PayChannelEnum;
import org.dows.member.exception.MemberException;
import org.dows.member.handler.pay.AliPayBiz;
import org.dows.member.handler.pay.PaymentBiz;
import org.dows.member.request.pay.PayQrCodeRequest;
import org.dows.member.response.pay.AliPayStatusResponse;
import org.dows.member.response.pay.PayQrCodeResponse;
import org.dows.rade.aac.AacContext;
import org.dows.rade.aac.AacUser;
import org.dows.rade.context.AppContext;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Tag(name = "支付宝支付", description = "支付宝支付")
@Slf4j
@RestController
@RequiredArgsConstructor
public class AliPayRest implements AliPayApi {

    private final AacContext aacContext;
    private final PaymentBiz paymentBiz;
    private final AliPayBiz aliPayBiz;

    @Override
    public PayQrCodeResponse aliPayQrCode(PayQrCodeRequest request) {
        request.setAccountInstanceId(getAccountId());
        request.setAppId(AppContext.getAppId());
        request.setPayChannel(PayChannelEnum.ALI.getCode());

        return paymentBiz.createNativePayment(request);
    }

    @Override
    public String aliPayNotify(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, values) -> params.put(key, values[0]));

        return paymentBiz.aliPayNotify(params);
    }

    @Override
    public AliPayStatusResponse aliPayStatus(String outTradeNo) {
        return aliPayBiz.aliPayStatus(outTradeNo);
    }

    private Long getAccountId() {
        AacUser aacUser = aacContext.getAacUser();
        if (Objects.isNull(aacUser) || Objects.isNull(aacUser.getAccountId())) {
            throw new MemberException("登录账号不存在");
        }
        return aacUser.getAccountId();
    }
}