package com.example.jobscopenotworkingsimpleasyncexecutor;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.example.jobscopenotworkingsimpleasyncexecutor.BatchConfiguration.SINGLE_THREADED;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by michal on 29.01.20.
 */

@SpringBatchTest
@SpringBootTest
@ActiveProfiles(SINGLE_THREADED)
public class BatchConfigurationSingleThreadedTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    void shouldStartJob() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo(ExitStatus.COMPLETED.getExitCode());
    }
}