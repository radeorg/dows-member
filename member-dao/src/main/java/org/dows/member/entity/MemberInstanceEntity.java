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
@Table("member_instance")
@Schema(name = "MemberInstanceEntity")
public class MemberInstanceEntity {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long memberInstanceId;

    @Column(value = "account_instance_id", comment = "账号实例ID")
    private Long accountInstanceId;

    @Column(value = "member_interests_id", comment = "会员权益ID")
    private Long memberInterestsId;

    @Column(value = "membership_type", comment = "会员等级")
    private String membershipType;

    @Column(value = "membership_effective_date", comment = "会员生效日期")
    private OffsetDateTime membershipEffectiveDate;

    @Column(value = "membership_expiry_date", comment = "会员过期日期")
    private OffsetDateTime membershipExpiryDate;
}