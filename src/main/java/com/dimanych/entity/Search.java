package com.dimanych.entity;

import java.util.List;

/**
 * <p></p>
 *
 * @author Dmitriy Grigoriev
 * @since 1.0
 */
public class Search extends BaseEntity {
  private List<Job> jobs;

  public List<Job> getJobs() {
    return jobs;
  }

  public void setJobs(List<Job> jobs) {
    this.jobs = jobs;
  }
}
