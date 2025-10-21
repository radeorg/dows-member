package org.dows.member.request.user;

import com.mybatisflex.annotation.Column;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "会员充值表请求")
public class UserMemberChargeListRequest {

    @Column(value = "state", comment = "充值状态(0:pending,1:completed,2:failed,3:refunded)")
    private String state;
}
