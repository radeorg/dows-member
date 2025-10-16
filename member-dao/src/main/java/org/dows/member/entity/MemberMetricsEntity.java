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

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(value = "member_metrics", onUpdate = AutoFillDataListener.class, onInsert = AutoFillDataListener.class)
@Schema(name = "会员度量表")
public class MemberMetricsEntity extends BaseEntity<MemberMetricsEntity> {

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long memberMetricsId;

    @Column(value = "member_instance_id", comment = "会员实例ID")
    private Long memberInstanceId;

    @Column(value = "account_instance_id", comment = "账号实例ID")
    private Long accountInstanceId;

    @Column(value = "member_interests_id", comment = "会员权益ID")
    private Long memberInterestsId;

    @Column(value = "daily_match_count", comment = "每日可使用匹配简历上限")
    private Integer dailyMatchCount;

    @Column(value = "used_match_count", comment = "已使用匹配简历数")
    private Integer usedMatchCount;

    @Column(value = "active_interview_count", comment = "可使用同时面试邀约上限")
    private Integer activeInterviewCount;

    @Column(value = "used_interview_count", comment = "已使用邀约数")
    private Integer usedInterviewCount;

    @Column(value = "creation_jd_count", comment = "可使创建JD总数")
    private Integer creationJdCount;

    @Column(value = "used_creation_jd_count", comment = "已创建JD数")
    private Integer usedCreationJdCount;

    @Column(value = "single_upload_count", comment = "单次上传简历上限")
    private Integer singleUploadCount;

    @Column(value = "email_push_enabled", comment = "邮件推送功能开关，0关闭，1打开")
    private Integer emailPushEnabled;

    @Column(value = "use_date", comment = "使用时间段")
    private String useDate;

    @Column(value = "holiday_exclude", comment = "是否排除节假日")
    private Integer holidayExclude;

    @Column(value = "app_id", comment = "应用ID")
    private String appId;

    @Column(value = "operator_id", comment = "操作者ID")
    private Long operatorId;

    @Column(value = "ver", comment = "乐观锁", onUpdateValue = "ver+1")
    private Integer ver;

    @Column(value = "deleted", comment = "逻辑删除", isLogicDelete = true)
    private Integer deleted;

    @Column(value = "ts", comment = "操作时间")
    private Date ts;

    @Column(value = "ut", comment = "更新时间")
    private Date ut;
}