package com.duboribu.ecommerce.batch.config;

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

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BookCrawlBatchConfig {

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
    public Step bookCrawlStep(ItemReader<CrawledBookDto> reader,
                              ItemProcessor<CrawledBookDto, Book> processor,
                              ItemWriter<Book> writer) {
        return new StepBuilder("bookCrawlStep", jobRepository)
                .<CrawledBookDto, Book>chunk(1, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(10)
                .build();
    }
}
