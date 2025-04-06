package org.orderhub.sc.scheduledorder.batch;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BatchJobSchedulerTest {

    @Mock
    JobLauncher jobLauncher;

    @Mock
    Job scheduledOrderProcessingJob;

    @InjectMocks
    BatchJobScheduler batchJobScheduler;

    @Test
    void shouldLaunchScheduledOrderProcessingJob() throws Exception {
        // When
        batchJobScheduler.runJob();

        // Then
        ArgumentCaptor<JobParameters> jobParametersCaptor = ArgumentCaptor.forClass(JobParameters.class);
        verify(jobLauncher, times(1)).run(eq(scheduledOrderProcessingJob), jobParametersCaptor.capture());

        JobParameters params = jobParametersCaptor.getValue();
        assertThat(params.getParameters()).containsKey("timestamp");
    }
}
