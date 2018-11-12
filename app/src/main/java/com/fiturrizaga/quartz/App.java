package com.fiturrizaga.quartz;

import javax.sql.DataSource;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }

    @Autowired
    public DataSource dataSource;

    @Bean
    public JobDetail sampleJobDetail() {
        return JobBuilder.newJob(SampleJob.class).withIdentity("sampleJob")
            .usingJobData("name", "World").storeDurably().build();
    }

    @Bean
    public Trigger sampleJobTrigger() {

        return TriggerBuilder.newTrigger()
            .forJob(sampleJobDetail())
            .withIdentity("sampleTrigger")
            .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
            .build();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactory() {
        System.out.println(dataSource.getClass());
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        bean.setJobDetails(sampleJobDetail());
        bean.setTriggers(sampleJobTrigger());
        bean.setDataSource(dataSource);
        return bean;
    }


}
