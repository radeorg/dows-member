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
@Table("member_metrics")
@Schema(name = "MemberMetricsEntity")
public class MemberMetricsEntity {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long memberMetricsId;

    @Column(value = "member_instance_id", comment = "会员实例ID")
    private Long memberInstanceId;

    @Column(value = "account_instance_id", comment = "账号实例ID")
    private Long accountInstanceId;

    @Column(value = "member_interests_id", comment = "会员权益ID")
    private Long memberInterestsId;

    @Column(value = "match_count", comment = "可使用匹配简历总数")
    private Integer matchCount;

    @Column(value = "used_match_count", comment = "已使用匹配简历数")
    private Integer usedMatchCount;

    @Column(value = "invite_count", comment = "可使用邀约总数")
    private Integer inviteCount;

    @Column(value = "used_invite_count", comment = "已使用邀约数")
    private Integer usedInviteCount;

    @Column(value = "creation_jd_count", comment = "可使创建JD总数")
    private Integer creationJdCount;

    @Column(value = "used_creation_jd_count", comment = "已创建JD数")
    private Integer usedCreationJdCount;

    @Column(value = "single_upload_count", comment = "单次上传简历上限")
    private Integer singleUploadCount;

    @Column(value = "app_id", comment = "应用ID")
    private String appId;

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