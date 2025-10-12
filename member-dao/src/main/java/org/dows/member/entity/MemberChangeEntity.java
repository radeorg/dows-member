package org.dows.member.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Table("member_change")
@Schema(name = "MemberChangeEntity")
public class MemberChangeEntity {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long memberChangeId;

    @Column(value = "member_instance_id", comment = "会员实例ID")
    private Long memberInstanceId;

    @Column(value = "account_instance_id", comment = "账号实例ID")
    private String accountInstanceId;

    @Column(value = "app_id", comment = "应用ID")
    private String appId;

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
    private OffsetDateTime effectiveDate;

    @Column(value = "expiry_date", comment = "过期时间")
    private OffsetDateTime expiryDate;

    @Column(value = "note", comment = "变更备注")
    private String note;

    @Column(value = "operator_id", comment = "操作者ID")
    private Long operatorId;

    @Column(value = "ver", comment = "乐观锁")
    private Long ver;

    @Column(value = "deleted", comment = "逻辑删除")
    private Byte deleted;

    @Column(value = "ts", comment = "操作时间")
    private OffsetDateTime ts;

    @Column(value = "ut", comment = "更新时间")
    private OffsetDateTime ut;
}