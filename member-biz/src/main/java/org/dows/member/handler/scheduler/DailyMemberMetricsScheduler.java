package org.dows.member.handler.scheduler;

import com.mybatisflex.core.paginate.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.member.form.PageQuery;
import org.dows.member.handler.admin.AdminMemberInstanceHandler;
import org.dows.member.handler.user.UserMemberMetricsBiz;
import org.dows.member.request.admin.AdminMemberInstanceQueryRequest;
import org.dows.member.response.MemberInstanceGetResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 生成每日会员度量数据线程
 */
@RequiredArgsConstructor
@Configuration
@EnableScheduling
@Slf4j
public class DailyMemberMetricsScheduler {

    private static final int PAGE_SIZE = 100; // 5线程*10条/线程
    private static final int EXECUTE_NUM = 20; // 每个线程执行的条数
    private final UserMemberMetricsBiz userMemberMetricsBiz;
    private final AdminMemberInstanceHandler adminMemberInstanceHandler;
    private final ThreadPoolTaskExecutor dailyMemberMetricsTaskExecutor;

    // 每天00:00:00执行（CRON表达式）
    @Scheduled(cron = "0 */1 * * * ?")
    public void dailyTask() {
        try {
            int currentPage = 1; // 重置页码为1，确保每次定时任务都从第一页开始处理
            while (true) {
                AdminMemberInstanceQueryRequest request = buildRequest();
                PageQuery pageQuery = new PageQuery();
                pageQuery.setPageSize(PAGE_SIZE);
                pageQuery.setPageNum(currentPage);

                Page<MemberInstanceGetResponse> page = adminMemberInstanceHandler.query(pageQuery, request);
                if (page.getRecords().isEmpty()) {
                    break; // 没有更多数据，退出循环
                }

                processBatch(page.getRecords());

                if (!page.hasNext()) {
                    break; // 没有下一页，退出循环
                }
                currentPage++;
            }
        } catch (Exception e) {
            log.error("简历解析调度异常", e);
        }
    }
    private AdminMemberInstanceQueryRequest buildRequest() {
        return new AdminMemberInstanceQueryRequest();
    }

    private void processBatch(List<MemberInstanceGetResponse> records)
            throws InterruptedException {
        CountDownLatch latch = new CountDownLatch((int) Math.ceil((double) records.size() / EXECUTE_NUM));

        for (int i = 0; i < records.size(); i += EXECUTE_NUM) {
            int end = Math.min(i + EXECUTE_NUM, records.size());
            List<MemberInstanceGetResponse> tempRecords = records.subList(i, end);
            dailyMemberMetricsTaskExecutor.execute(() -> {
                try {
                    for (MemberInstanceGetResponse tempRecord : tempRecords) {
                        userMemberMetricsBiz.saveDailyMemberMetrics(tempRecord.getMemberInstanceId());
                    }
                } catch (Exception e) {
                    log.error("处理简历文件数字化异常", e);
                }finally {
                    latch.countDown();
                }
            });
        }
        latch.await(); // 等待所有任务完成
    }
}
