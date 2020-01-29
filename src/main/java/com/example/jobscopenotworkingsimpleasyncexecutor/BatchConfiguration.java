package com.example.jobscopenotworkingsimpleasyncexecutor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by michal on 29.01.20.
 */

@Configuration
@EnableBatchProcessing
public class BatchConfiguration  extends DefaultBatchConfigurer {

    public static final String SINGLE_THREADED = "single-threaded";
    public static final String SIMPLE_ASYNC_EXECUTOR = "simple-async-executor";
    public static final String FIXED_THREAD_POOL__EXECUTOR = "fixed-thread-pool-executor";

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;


    @Bean
    @JobScope
    ConcurrentHashMap<String, String> jobScopedMap(){
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Job job(@Qualifier("write") Step write,
                   @Qualifier("read")Step read){
        return jobBuilderFactory.get("single-threaded")
                .incrementer(new RunIdIncrementer())
                .start(write)
                .next(read)
                .build();
    }

    @Bean
    @Profile(SINGLE_THREADED)
    @Qualifier("write")
    public Step singleThreadedReadStep(){
        return stepBuilderFactory.get("read-step")
                .tasklet(putToMapTasklet())
                .build();

    }

    @Bean
    @Qualifier("read")
    @Profile(SINGLE_THREADED)
    public Step singleThreadedWriteStep(){
        return stepBuilderFactory.get("write-step")
                .tasklet(readFromMapTasklet())
                .build();

    }

    @Bean
    @Profile(SIMPLE_ASYNC_EXECUTOR)
    @Qualifier("write")
    public Step multiThreadedReadStep(){
        return stepBuilderFactory.get("read-step")
                .tasklet(putToMapTasklet())
                .build();

    }

    @Bean
    @Qualifier("read")
    @Profile(SIMPLE_ASYNC_EXECUTOR)
    public Step multiThreadedWriteStep(){
        return stepBuilderFactory.get("write-step")
                .tasklet(readFromMapTasklet())
                .taskExecutor(taskExecutor())
                .build();

    }

    @Bean
    @Profile(SIMPLE_ASYNC_EXECUTOR)
    public TaskExecutor taskExecutor(){
        return new SimpleAsyncTaskExecutor();
    }



    //


    @Bean
    @Profile(FIXED_THREAD_POOL__EXECUTOR)
    @Qualifier("write")
    public Step fixedThreadedReadStep(){
        return stepBuilderFactory.get("read-step")
                .tasklet(putToMapTasklet())
                .taskExecutor(fixedTaskExecutor())
                .build();

    }

    @Bean
    @Qualifier("read")
    @Profile(FIXED_THREAD_POOL__EXECUTOR)
    public Step fixedThreadedWriteStep(){
        return stepBuilderFactory.get("write-step")
                .tasklet(readFromMapTasklet())
                .taskExecutor(fixedTaskExecutor())
                .build();

    }

    @Bean
    @Profile(FIXED_THREAD_POOL__EXECUTOR)
    public TaskExecutor fixedTaskExecutor(){
        return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(2));
    }

    @Bean
    @StepScope
    public Tasklet putToMapTasklet(){
        ConcurrentHashMap<String, String> map = jobScopedMap();
        return (stepContribution, chunkContext) -> {
            map.putIfAbsent("some-key", "some-value");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    @StepScope
    public Tasklet readFromMapTasklet(){
        ConcurrentHashMap<String, String> map = jobScopedMap();
        return (stepContribution, chunkContext) -> {
            System.out.println(map.values());
            return RepeatStatus.FINISHED;
        };
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        // no need for datasource
    }
}
