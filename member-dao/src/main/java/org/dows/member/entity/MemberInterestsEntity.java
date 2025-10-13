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
@Table(value = "member_interests", onUpdate = AutoFillDataListener.class, onInsert = AutoFillDataListener.class)
@Schema(name = "会员权益表")
public class MemberInterestsEntity extends BaseEntity<MemberInterestsEntity> {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long memberInterestsId;

    @Column(value = "member_type", comment = "会员等级")
    private String memberType;

    @Column(value = "amount", comment = "会员价格")
    private Double amount;

    @Column(value = "expiry_day", comment = "会员有效期")
    private Integer expiryDay;

    @Column(value = "single_upload_count", comment = "单次上传简历上限")
    private Integer singleUploadCount;

    @Column(value = "daily_match_count", comment = "每日匹配简历上限")
    private Integer dailyMatchCount;

    @Column(value = "creation_jd_count", comment = "JD创建总数上限")
    private Integer creationJdCount;

    @Column(value = "active_interview_count", comment = "同时面试邀约上限")
    private Integer activeInterviewCount;

    @Column(value = "email_push_enabled", comment = "邮件推送功能开关")
    private Integer emailPushEnabled;

    @Column(value = "use_date", comment = "使用时间段")
    private String useDate;

    @Column(value = "holiday_exclude", comment = "是否排除节假日")
    private Integer holidayExclude;

    @Column(value = "matching_priority", comment = "匹配优先级")
    private Integer matchingPriority;

    @Column(value = "disabled", comment = "是否禁用")
    private Integer disabled;

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