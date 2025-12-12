package com.duboribu.ecommerce.batch.controller;

import com.duboribu.ecommerce.auth.domain.DefaultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 배치 수동 실행 컨트롤러
 * - 관리자가 수동으로 배치를 실행할 때 사용
 * 새로운 Job을 추가하려면:
 * // 1. job 패키지에 XxxJobConfig.java 생성
 * // 2. 생성자에 @Qualifier("xxxJob") Job xxxJob 추가
 * // 3. 아래처럼 엔드포인트 추가
 */
@Slf4j
@RestController
@RequestMapping("/admin/batch")
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job bookCrawlJob;

    public BatchController(JobLauncher jobLauncher,
                           @Qualifier("bookCrawlJob") Job bookCrawlJob) {
        this.jobLauncher = jobLauncher;
        this.bookCrawlJob = bookCrawlJob;
    }

    /**
     * 책 크롤링 배치 수동 실행
     * GET /admin/batch/book-crawl?page=1
     */
    @GetMapping("/book-crawl")
    public ResponseEntity<DefaultResponse<Map<String, Object>>> runBookCrawlJob(
            @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        return executeJob(bookCrawlJob, page);
    }

    /**
     * Job 실행 공통 메서드
     */
    private ResponseEntity<DefaultResponse<Map<String, Object>>> executeJob(Job job, int page) {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .addString("page", String.valueOf(page))
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(job, params);
            
            Map<String, Object> result = new HashMap<>();
            result.put("jobName", job.getName());
            result.put("status", execution.getStatus().toString());
            result.put("jobId", execution.getJobId());
            
            log.info("[수동실행] {} 배치 시작 - JobId: {}", job.getName(), execution.getJobId());
            return new ResponseEntity<>(new DefaultResponse<>(result), HttpStatus.OK);

        } catch (Exception e) {
            log.error("[수동실행] {} 배치 실패: {}", job.getName(), e.getMessage());
            
            Map<String, Object> error = new HashMap<>();
            error.put("jobName", job.getName());
            error.put("error", e.getMessage());
            
            return new ResponseEntity<>(new DefaultResponse<>("배치 실행 실패", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
