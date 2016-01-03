package com.dimanych.entity;

import java.net.URL;
import java.util.Calendar;
import java.util.Objects;

/**
 * <p>Upwork job com.dimanych.entity</p>
 *
 * @author Dmitriy Grigoriev
 * @since 1.0
 */
public class Job extends BaseEntity {
  private JobType type;
  private String description;
  private String budget;
  private URL url;
  private Calendar publishTime;
  private String level;
  private String duration;
  private String payIndicator;
  private String starsInfo;
  private String customer;

  public JobType getType() {
    return type;
  }

  public void setType(JobType type) {
    this.type = type;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public URL getUrl() {
    return url;
  }

  public void setUrl(URL url) {
    this.url = url;
  }

  public Calendar getPublishTime() {
    return publishTime;
  }

  public void setPublishTime(Calendar publishTime) {
    this.publishTime = publishTime;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getDuration() {
    if (Objects.nonNull(duration)) {
      return "Duration: " + duration;
    }
    return "";
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public String getPayIndicator() {
    return payIndicator;
  }

  public void setPayIndicator(String payIndicator) {
    this.payIndicator = payIndicator;
  }

  public String getStarsInfo() {
    return starsInfo;
  }

  public void setStarsInfo(String starsInfo) {
    this.starsInfo = starsInfo;
  }

  public String getBudget() {
    if (type.equals(JobType.HOURLY)) {
      return "";
    }
    return "Budget: " + budget;
  }

  public void setBudget(String budget) {
    this.budget = budget;
  }

  public String getCustomer() {
    return customer;
  }

  public void setCustomer(String customer) {
    this.customer = customer;
  }
}
