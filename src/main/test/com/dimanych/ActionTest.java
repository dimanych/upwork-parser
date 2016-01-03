package com.dimanych;

import com.dimanych.entity.Job;
import com.dimanych.entity.JobType;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * <p></p>
 *
 * @author Dmitriy Grigoriev
 */
public class ActionTest {

    public List<Job> getJobsTest() {
        List<Job> jobs = new ArrayList<>();
        for (int i=0; i<10; i++) {
            Job job = new Job();
            job.setId("id"+i);
            job.setBudget("budget"+i);
            StringBuffer sbDesc = new StringBuffer("begin____________\n");
            for (int j=0; j<200; j++) {
                sbDesc.append("description "+j+i);
            }
            job.setDescription(sbDesc.toString());
            job.setDuration("duration"+i);
            job.setLevel("level"+i);
            job.setPayIndicator("payIndicator"+i);
            job.setPublishTime(Calendar.getInstance());
            job.setStarsInfo("starsInfo"+i);
            job.setType(i % 2 == 0 ? JobType.FIXED : JobType.HOURLY);
            try {
                job.setUrl(new URL("http://upwork/"+i));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            job.setTitle("Title"+i);
            jobs.add(job);
        }
        return jobs;
    }

}
