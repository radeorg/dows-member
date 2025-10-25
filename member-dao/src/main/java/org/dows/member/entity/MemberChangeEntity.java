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
@Schema(name = "会员等级变更表")
@Table(value = "member_change", onUpdate = AutoFillDataListener.class, onInsert = AutoFillDataListener.class)
public class MemberChangeEntity extends BaseEntity<MemberChangeEntity> {

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long memberChangeId;

    @Column(value = "member_instance_id", comment = "会员实例ID")
    private Long memberInstanceId;

    @Column(value = "account_instance_id", comment = "账号实例ID")
    private Long accountInstanceId;

    @Column(value = "previous_member_interests_id", comment = "变更前会员权益ID")
    private Long previousMemberInterestsId;

    @Column(value = "previous_member_type", comment = "变更前会员等级")
    private String previousMemberType;

    @Column(value = "new_member_interests_id", comment = "变更后会员权益ID")
    private Long newMemberInterestsId;

    @Column(value = "new_member_type", comment = "变更后会员等级")
    private String newMemberType;

    @Column(value = "change_type", comment = "变更类型")
    private String changeType;

    @Column(value = "effective_date", comment = "生效时间")
    private LocalDateTime effectiveDate;

    @Column(value = "expiry_date", comment = "过期时间")
    private LocalDateTime expiryDate;

    @Column(value = "note", comment = "变更备注")
    private String note;

    @Column(value = "new_interests_info", comment = "变更后会员权益详细信息")
    private String newInterestsInfo;

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