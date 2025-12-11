package com.duboribu.ecommerce.batch.config;

import com.duboribu.ecommerce.batch.dto.CrawledBookDto;
import com.duboribu.ecommerce.batch.processor.BookItemProcessor;
import com.duboribu.ecommerce.batch.reader.KyoboBookItemReader;
import com.duboribu.ecommerce.batch.writer.BookItemWriter;
import com.duboribu.ecommerce.entity.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BookCrawlBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final KyoboBookItemReader reader;
    private final BookItemProcessor processor;
    private final BookItemWriter writer;

    @Bean
    public Job bookCrawlJob() {
        return new JobBuilder("bookCrawlJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(bookCrawlStep())
                .build();
    }

    @Bean
    public Step bookCrawlStep() {
        return new StepBuilder("bookCrawlStep", jobRepository)
                .<CrawledBookDto, Book>chunk(5, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(10)
                .build();
    }
}

