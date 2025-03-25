package org.orderhub.sc.ScheduledOrder.batch.config;

import lombok.RequiredArgsConstructor;
import org.orderhub.sc.ScheduledOrder.batch.ScheduledOrderItemProcessor;
import org.orderhub.sc.ScheduledOrder.batch.ScheduledOrderItemReader;
import org.orderhub.sc.ScheduledOrder.batch.ScheduledOrderItemWriter;
import org.orderhub.sc.ScheduledOrder.domain.ScheduledOrder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ScheduledOrderJobConfig {

    private final ScheduledOrderItemReader reader;
    private final ScheduledOrderItemProcessor processor;
    private final ScheduledOrderItemWriter writer;

    @Bean
    public Job scheduledOrderProcessingJob(JobRepository jobRepository, Step scheduledOrderStep) {
        return new JobBuilder("scheduledOrderProcessingJob", jobRepository)
                .start(scheduledOrderStep)
                .build();
    }

    @Bean
    public Step scheduledOrderStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("scheduledOrderStep", jobRepository)
                .<ScheduledOrder, ScheduledOrder>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
