package org.dows.member.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dows.rade.crud.AutoFillDataListener;
import org.dows.rade.crud.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "会员充值表")
@Table(value = "member_charge", onUpdate = AutoFillDataListener.class, onInsert = AutoFillDataListener.class)
public class MemberChargeEntity extends BaseEntity<MemberChargeEntity> {

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long memberChargeId;

    @Column(value = "account_instance_id", comment = "账号实例ID")
    private Long accountInstanceId;

    @Column(value = "member_instance_id", comment = "会员实例ID")
    private Long memberInstanceId;

    @Column(value = "member_interests_id", comment = "会员权益ID")
    private Long memberInterestsId;

    @Column(value = "pay_no", comment = "支付单号")
    private String payNo;

    @Column(value = "amount", comment = "充值金额（红冲/冲正为负数）")
    private BigDecimal amount;

    @Column(value = "channel", comment = "支付通道(wechat/alipay/bank)")
    private String channel;

    @Column(value = "state", comment = "充值状态(0:pending,1:completed,2:failed,3:refunded)")
    private String state;

    @Column(value = "charge_time", comment = "实际充值时间")
    private Date chargeTime;

    @Column(value = "transaction_id", comment = "第三方支付交易号")
    private String transactionId;

    @Column(value = "charge_type", comment = "充值类型")
    private String chargeType;

    @Column(value = "note", comment = "充值备注")
    private String note;

    @Column(value = "app_id", comment = "应用ID")
    private String appId;

    @Column(value = "operatorId", comment = "操作者ID")
    private Long operatorId;

    @Column(value = "ver", comment = "乐观锁版本号", onUpdateValue = "ver+1")
    private Long ver;

    @Column(value = "deleted", comment = "逻辑删除(0:未删除,1:删除)", isLogicDelete = true)
    private Integer deleted;

    @Column(value = "ts", comment = "操作时间")
    private Date ts;

    @Column(value = "ut", comment = "更新时间")
    private Date ut;
}