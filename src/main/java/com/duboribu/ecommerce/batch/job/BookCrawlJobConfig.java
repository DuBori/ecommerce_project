package com.duboribu.ecommerce.batch.job;

import com.duboribu.ecommerce.batch.dto.CrawledBookDto;
import com.duboribu.ecommerce.entity.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 책 크롤링 배치 Job 설정
 * - Job 이름: bookCrawlJob
 * - 알라딘에서 카테고리별 베스트셀러 크롤링
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BookCrawlJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job bookCrawlJob(Step bookCrawlStep) {
        return new JobBuilder("bookCrawlJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(bookCrawlStep)
                .build();
    }

    @Bean
    public Step bookCrawlStep(ItemReader<CrawledBookDto> bookItemReader,
                              ItemProcessor<CrawledBookDto, Book> bookItemProcessor,
                              ItemWriter<Book> bookItemWriter) {
        return new StepBuilder("bookCrawlStep", jobRepository)
                .<CrawledBookDto, Book>chunk(5, transactionManager)
                .reader(bookItemReader)
                .processor(bookItemProcessor)
                .writer(bookItemWriter)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(10)
                .build();
    }
}

