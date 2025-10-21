package org.dows.member.handler.pay.impl;

import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.dows.member.handler.config.AliPayConfig;
import org.dows.member.handler.pay.AliPayBiz;
import org.dows.member.response.AliPayQrCodeResponse;
import org.dows.member.response.AliPayStatusResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AliPayBizImpl implements AliPayBiz {
    @Override
    public AliPayQrCodeResponse aliPayQrCode(BigDecimal totalAmount, String description) {
        //订单号
        String outTradeNo="20150320010101001";
        AliPayQrCodeResponse  aliPayQrCodeResponse = new AliPayQrCodeResponse();
        //证书模式
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setBizContent("{" + "\"out_trade_no\":\"" + outTradeNo + "\"," + "\"total_amount\":\"" + totalAmount + "\"," + "\"subject\":\"" + description + "\"," + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"" + "}");
        AlipayClient alipayClient = AliPayConfig.aliPayClient();
        AlipayTradePagePayResponse response = null;
        try {
            response = alipayClient.pageExecute(request);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
        if (response.isSuccess()) {
            String form = response.getBody();  // 包含二维码的HTML表单
            //创建订单
            System.out.println("调用成功" + form);
            aliPayQrCodeResponse.setQrCode(form);
        } else {
            System.out.println("调用失败");
        }
        aliPayQrCodeResponse.setOutTradeNo(outTradeNo);
        return aliPayQrCodeResponse;

    }

    //getAlipayConfig密钥暂时没移过来 如果有支付宝公钥可以试试
    @Override
    public AliPayQrCodeResponse aliPayQrCode1(BigDecimal totalAmount, String description) {
        //订单号
        String outTradeNo="20150320010101001";
        AliPayQrCodeResponse  aliPayQrCodeResponse = new AliPayQrCodeResponse();
        // 1. 初始化预创建订单请求
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl("https://your.domain/notify"); // 异步通知地址
        // 2. 设置订单参数
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setOutTradeNo(outTradeNo); // 商户唯一订单号
        model.setTotalAmount(totalAmount.toString()); // 订单金额（元）
        model.setSubject(description); // 商品标题
        model.setTimeoutExpress("30m"); // 订单超时时间
        request.setBizModel(model);
        AlipayClient alipayClient = null;
        try {
            alipayClient = new DefaultAlipayClient(AliPayConfig.getAlipayConfig());
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
        // 3. 执行请求获取二维码链接
        AlipayTradePrecreateResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
        String qrCodeUrl =null;
        if (response.isSuccess()) {
             qrCodeUrl = response.getQrCode(); // 获取二维码链接（如：https://qr.alipay.com/xxx）
            // 4. 将链接转换为二维码图片（需借助第三方库，如ZXing）
        }
        aliPayQrCodeResponse.setQrCode(qrCodeUrl);
        aliPayQrCodeResponse.setOutTradeNo(outTradeNo);
        return aliPayQrCodeResponse;
    }

    @Override
    public String aliPayNotify(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();

        // 转换参数格式
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            params.put(name, values[0]);
        }

        // 验签
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, AliPayConfig.publicKey, "UTF-8", "RSA2");
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }

        if (signVerified) {
            // 处理支付结果（订单状态更新等）
            String outTradeNo = params.get("out_trade_no");
            String tradeStatus = params.get("trade_status");
            if ("TRADE_SUCCESS".equals(tradeStatus)) {
                // 处理成功逻辑
            }
            return "success";  // 必须返回"success"
        } else {
            return "fail";
        }
    }

    @Override
    public AliPayStatusResponse aliPayStatus(String outTradeNo) {
        AliPayStatusResponse aliPayStatusResponse = new AliPayStatusResponse();
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", outTradeNo);  // 商户订单号
        // 或使用支付宝交易号查询：bizContent.put("trade_no", "支付宝交易号");
        request.setBizContent(bizContent.toString());
        try {
            AlipayClient alipayClient = AliPayConfig.aliPayClient();
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                // 处理查询结果
                String tradeStatus = response.getTradeStatus();
                String totalAmount = response.getTotalAmount();
                // 交易状态说明：WAIT_BUYER_PAY(待付款)、TRADE_SUCCESS(支付成功)、TRADE_CLOSED(交易关闭)等FAIL
                aliPayStatusResponse.setTradeState(tradeStatus);

            } else {
                aliPayStatusResponse.setTradeState("FAIL");
                aliPayStatusResponse.setTradeStateDesc(response.getMsg());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return aliPayStatusResponse;
    }

}