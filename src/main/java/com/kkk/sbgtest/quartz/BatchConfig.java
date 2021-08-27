package com.kkk.sbgtest.quartz;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/* Quartz configuration */
//@Configuration
//@EnableEncryptableProperties
public class BatchConfig {
    private final String TRIGGER_GROUP_NAME = "AIRMAP_GROUP";

    @Bean
    public JobDetailFactoryBean dustJobDetail() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(Job1.class);
        jobDetailFactory.setDescription("Collect tbl_weather_dust_density Data");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

//    @Bean
    public CronTriggerFactoryBean dustTrigger(JobDetail dustJobDetail) {
        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
        trigger.setGroup(TRIGGER_GROUP_NAME);
        trigger.setCronExpression("0 * * * * ?");
        trigger.setJobDetail(dustJobDetail);

        return trigger;
    }

    @Bean
    public JobDetailFactoryBean secondJobDetail() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(Job2.class);
        jobDetailFactory.setDescription("Collect tbl_weather_dust_density Data");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

//    @Bean
    public SimpleTriggerFactoryBean secondTrigger(JobDetail secondJobDetail) {
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setGroup(TRIGGER_GROUP_NAME);
        trigger.setRepeatInterval(10*1000);
        trigger.setRepeatCount(10);
        trigger.setJobDetail(secondJobDetail);

        return trigger;
    }

//    @Bean(name = "quartzDatasource")
//    @ConfigurationProperties(prefix = "spring.quartz-ds.datasource")
//    public DataSource quartzDataSource() {
//        return DataSourceBuilder.create().build();
//    }

    @Bean
    public SchedulerFactoryBean scheduler(Trigger[] triggers) throws Exception {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setOverwriteExistingJobs(true);
//        schedulerFactory.setDataSource(quartzDataSource());
//        schedulerFactory.setConfigLocation(new ClassPathResource("quartz/quartz.properties"));
//        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setTriggers(triggers);
        schedulerFactory.setSchedulerName("AIRMAP-scheduler");

        return schedulerFactory;
    }
}