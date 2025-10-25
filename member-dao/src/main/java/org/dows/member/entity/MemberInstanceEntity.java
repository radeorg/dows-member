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

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(value = "member_instance", onUpdate = AutoFillDataListener.class, onInsert = AutoFillDataListener.class)
@Schema(name = "会员实例表")
public class MemberInstanceEntity extends BaseEntity<MemberInstanceEntity> {

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long memberInstanceId;

    @Column(value = "account_instance_id", comment = "账号实例ID")
    private Long accountInstanceId;

    @Column(value = "member_interests_id", comment = "会员权益ID")
    private Long memberInterestsId;

    @Column(value = "member_type", comment = "会员等级")
    private String memberType;

    @Column(value = "effective_date", comment = "会员生效日期")
    private LocalDateTime effectiveDate;

    @Column(value = "expiry_date", comment = "会员过期日期")
    private LocalDateTime expiryDate;

    @Column(value = "app_id", comment = "应用ID")
    private String appId;

    @Column(value = "operator_id", comment = "操作者ID")
    private Long operatorId;

    @Column(value = "ver", comment = "乐观锁", onUpdateValue = "ver+1")
    private Integer ver;

    @Column(value = "deleted", comment = "逻辑删除", isLogicDelete = true)
    private Integer deleted;

    @Column(value = "ts", comment = "操作时间")
    private LocalDateTime ts;

    @Column(value = "ut", comment = "更新时间")
    private LocalDateTime ut;
}