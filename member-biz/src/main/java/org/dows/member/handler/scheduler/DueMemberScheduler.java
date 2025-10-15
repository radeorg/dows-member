package org.dows.member.handler.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.member.handler.user.UserMemberInstanceBiz;
import org.dows.member.handler.user.UserMemberInstanceHandler;
import org.dows.member.response.MemberInstanceGetResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.Objects;

/**
 * 会员到期自动降级为免费会员线程
 */
@RequiredArgsConstructor
@Configuration
@EnableScheduling
@Slf4j
public class DueMemberScheduler {

    private final UserMemberInstanceHandler userMemberInstanceHandler;
    private final UserMemberInstanceBiz userMemberInstanceBiz;
    private final ThreadPoolTaskExecutor dueMemberInstanceTaskExecutor;

    // 每天00:00:00执行（CRON表达式）
    @Scheduled(cron = "0 */10 * * * ?")
    public void dueTask() {
        log.info("DueMemberScheduler定时任务执行开始: {}", System.currentTimeMillis());

        dueMemberInstanceTaskExecutor.execute(() -> {
            try {
                List<MemberInstanceGetResponse> list = userMemberInstanceHandler.listDueMemberInstance();
                if (Objects.nonNull(list)) {
                    list.forEach(instance -> {
                        userMemberInstanceBiz.due(instance.getMemberInstanceId());
                    });
                }
            } catch (Exception e) {
                log.error("处理到期会员异常", e);
            }
        });

        log.info("DueMemberScheduler定时任务执行结束: {}", System.currentTimeMillis());
    }
}
