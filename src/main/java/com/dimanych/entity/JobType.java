package com.dimanych.entity;

import org.apache.commons.collections4.ListUtils;

import java.util.Arrays;

/**
 * <p>Timed type of work</p>
 *
 * @author Dmitriy Grigoriev
 * @since 1.0
 */
public enum JobType {
  FIXED("Fixed Price"),
  HOURLY("Hourly");

  private String caption;

  JobType(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return caption;
  }

  public static JobType get(String caption) {
    return ListUtils.emptyIfNull(
      Arrays.asList(values()))
      .stream()
      .filter(item -> item.getCaption().equals(caption))
      .findAny()
      .orElse(null);
  }
}
