package com.example.jobscopenotworkingsimpleasyncexecutor;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;

import static com.example.jobscopenotworkingsimpleasyncexecutor.BatchConfiguration.SIMPLE_ASYNC_EXECUTOR;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by michal on 29.01.20.
 */

@SpringBatchTest
@SpringBootTest
@ActiveProfiles(SIMPLE_ASYNC_EXECUTOR)
@TestConfiguration
public class BatchConfigurationSimpleAsyncMultiThreadedTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;


    // The following test will fail, as, because of using simple asyc task executor job scope is not available in step
    @Test
    void shouldStartJob() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo(ExitStatus.COMPLETED.getExitCode());
    }
}