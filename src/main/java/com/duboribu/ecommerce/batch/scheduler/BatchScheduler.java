package com.duboribu.ecommerce.batch.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 배치 스케줄러
 * - 특정 시간에 자동으로 배치 Job 실행
 */
@Slf4j
@Component
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job bookCrawlJob;

    public BatchScheduler(JobLauncher jobLauncher, 
                          @Qualifier("bookCrawlJob") Job bookCrawlJob) {
        this.jobLauncher = jobLauncher;
        this.bookCrawlJob = bookCrawlJob;
    }
    /**
     * Job 실행 공통 메서드
     */
    private void executeJob(Job job, String page) {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .addString("page", page)
                    .toJobParameters();

            jobLauncher.run(job, params);
            log.info("[스케줄러] {} 배치 실행 완료", job.getName());

        } catch (Exception e) {
            log.error("[스케줄러] {} 배치 실행 실패: {}", job.getName(), e.getMessage());
        }
    }
    /**
     * 책 크롤링 배치 - 매일 12시 실행
     */
    @Scheduled(cron = "0 0 12 * * *")
    public void runBookCrawlJob() {
        log.info("========== [스케줄러] 책 크롤링 배치 시작 ==========");
        executeJob(bookCrawlJob, "1");
    }

}
