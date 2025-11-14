package org.dows.member.biz.scheduler;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.member.biz.user.UserMemberInstanceBiz;
import org.dows.member.biz.user.UserMemberInstanceHandler;
import org.dows.member.response.MemberInstanceGetResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.List;

/**
 * 会员到期调度服务
 * 每隔10分钟执行过期会员降级处理
 */
@RequiredArgsConstructor
@Configuration
@Slf4j
public class DueMemberScheduler {

    @Value("${dows.member.scheduler.due-member.cron:0 0 0 * * ?}")
    private String dueMemberCron;

    private final UserMemberInstanceHandler userMemberInstanceHandler;
    private final UserMemberInstanceBiz userMemberInstanceBiz;
    private final ThreadPoolTaskScheduler dueMemberTaskScheduler;

    /**
     * 初始化时注册定时任务
     */
    @PostConstruct
    public void init() {
        dueMemberTaskScheduler.schedule(this::processDueMembers, new CronTrigger(dueMemberCron));
        log.info("DueMemberScheduler定时任务注册成功，执行频率：每日凌晨执行");
    }

    private void processDueMembers() {
        try {
            List<MemberInstanceGetResponse> members = userMemberInstanceHandler.listDueMemberInstance();
            if (members.isEmpty()) return;

            members.forEach(m -> {
                try {
                    userMemberInstanceBiz.due(m.getMemberInstanceId());
                } catch (Exception e) {
                    log.error("处理过期会员失败，会员ID: {}，错误信息: {}",
                            m.getMemberInstanceId(), e.getMessage(), e);
                }
            });

            log.info("处理完成过期会员：{}条记录", members.size());
        } catch (Exception e) {
            log.error("过期会员处理异常", e);
        }
    }
}
