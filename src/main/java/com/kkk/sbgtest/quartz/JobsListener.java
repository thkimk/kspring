package com.kkk.sbgtest.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

public class JobsListener implements JobListener {
    @Override
    public String getName() { return "globalJbo"; }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        System.out.println("## JobsListener.java [] ");
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {

    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {

    }
}
