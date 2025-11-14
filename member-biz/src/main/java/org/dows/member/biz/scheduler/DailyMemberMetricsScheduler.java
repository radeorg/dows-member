package org.dows.member.biz.scheduler;

import com.mybatisflex.core.paginate.Page;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.member.form.PageQuery;
import org.dows.member.biz.admin.AdminMemberInstanceHandler;
import org.dows.member.biz.user.UserMemberMetricsBiz;
import org.dows.member.request.admin.AdminMemberInstanceQueryRequest;
import org.dows.member.response.MemberInstanceGetResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 生成每日会员度量数据线程
 */
@RequiredArgsConstructor
@Configuration
@Slf4j
public class DailyMemberMetricsScheduler {

    @Value("${dows.member.scheduler.daily-metrics.cron:0 0 0 * * ?}")
    private String dailyMetrics;

    private static final int PAGE_SIZE = 100; // 每页大小
    private static final int BATCH_SIZE = 20; // 每个线程处理的条数
    private final UserMemberMetricsBiz userMemberMetricsBiz;
    private final AdminMemberInstanceHandler adminMemberInstanceHandler;
    private final ThreadPoolTaskScheduler dailyMemberMetricsTaskScheduler;

    // 初始化时注册定时任务（替代@Scheduled注解）
    @PostConstruct
    public void init() {
        dailyMemberMetricsTaskScheduler.schedule(this::processAllData, new CronTrigger(dailyMetrics));
        log.info("DailyMemberMetricsScheduler定时任务注册成功，执行频率：每日凌晨");
    }

    private void processAllData() {
        log.info("开始生成每日会员度量数据");

        int pageNum = 1;
        while (true) {
            PageQuery pageQuery = new PageQuery();
            pageQuery.setPageSize(PAGE_SIZE);
            pageQuery.setPageNum(pageNum);
            Page<MemberInstanceGetResponse> page = adminMemberInstanceHandler.query(pageQuery, new AdminMemberInstanceQueryRequest());

            if (page.getRecords().isEmpty()) break;

            // 并行处理当前页数据
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (int i = 0; i < page.getRecords().size(); i += BATCH_SIZE) {
                int end = Math.min(i + BATCH_SIZE, page.getRecords().size());
                List<MemberInstanceGetResponse> batch = page.getRecords().subList(i, end);

                futures.add(CompletableFuture.runAsync(() ->
                        batch.forEach(record -> {
                            try {
                                userMemberMetricsBiz.saveDailyMemberMetrics(record.getMemberInstanceId());
                            } catch (Exception e) {
                                log.error("处理会员度量数据失败，会员ID: {}，错误信息: {}",
                                        record.getMemberInstanceId(), e.getMessage(), e);
                            }
                        }), dailyMemberMetricsTaskScheduler
                ));
            }

            // 等待当前页所有批次完成
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            if (!page.hasNext()) break;
            pageNum++;
        }

        log.info("每日会员度量数据生成完成");
    }
}
