package com.duboribu.ecommerce.batch.controller;

import com.duboribu.ecommerce.auth.domain.DefaultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/batch")
@RequiredArgsConstructor
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job bookCrawlJob;

    @GetMapping("/crawl-books")
    public ResponseEntity<DefaultResponse<String>> runCrawlJob(
            @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .addString("page", String.valueOf(page))
                    .toJobParameters();

            jobLauncher.run(bookCrawlJob, params);
            
            log.info("교보문고 크롤링 배치 실행 시작");
            return new ResponseEntity<>(new DefaultResponse<>("배치 실행 시작"), HttpStatus.OK);

        } catch (Exception e) {
            log.error("배치 실행 실패", e);
            return new ResponseEntity<>(new DefaultResponse<>("배치 실행 실패: " + e.getMessage(), 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

