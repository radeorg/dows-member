package org.dows.member.request.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Data
@Schema(description = "创建支付二维码请求示例")
public class CreateNativePayQrCodeRequest {

    @Schema(description = "账号ID")
    private Long accountInstanceId;

    @NotBlank(message = "会员权益ID不能为空")
    @Schema(description = "会员权益ID")
    private Long memberInterestsId;

    @Schema(description = "应用ID")
    private String appId;

    @NotEmpty(message = "支付渠道不能为空")
    @Schema(description = "支付渠道（Wechat-微信，Ali-支付宝）")
    private String payChannel;

    @Schema(description = "描述")
    private  String description;

    @Schema(description = "客户端IP")
    private String clientIp;

    @Schema(description = "类型（RENEWAL续费，UP_GRADE升级）")
    private String chargeType;
}
