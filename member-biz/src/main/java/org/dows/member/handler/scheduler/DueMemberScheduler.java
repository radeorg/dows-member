package org.dows.member.handler.scheduler;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.member.handler.user.UserMemberInstanceBiz;
import org.dows.member.handler.user.UserMemberInstanceHandler;
import org.dows.member.response.MemberInstanceGetResponse;
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

    private final UserMemberInstanceHandler userMemberInstanceHandler;
    private final UserMemberInstanceBiz userMemberInstanceBiz;
    private final ThreadPoolTaskScheduler dueMemberTaskScheduler;

    /**
     * 初始化时注册定时任务
     */
    @PostConstruct
    public void init() {
        dueMemberTaskScheduler.schedule(this::processDueMembers, new CronTrigger("0 */10 * * * ?"));
        log.info("DueMemberScheduler定时任务注册成功，执行频率：每隔10分钟");
    }

    private void processDueMembers() {
        try {
            List<MemberInstanceGetResponse> members = userMemberInstanceHandler.listDueMemberInstance();
            if (members.isEmpty()) return;

            long count = members.stream()
                    .peek(m -> {
                        try { userMemberInstanceBiz.due(m.getMemberInstanceId()); }
                        catch (Exception e) { log.error("处理失败：{}", m.getMemberInstanceId(), e); }
                    })
                    .count();
            log.info("处理完成：{}条记录", count);
        } catch (Exception e) {
            log.error("处理异常", e);
        }
    }
}
