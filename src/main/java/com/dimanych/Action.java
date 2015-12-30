package com.dimanych;

import com.dimanych.entity.Job;
import com.dimanych.entity.JobType;
import org.apache.commons.collections4.ListUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p></p>
 *
 * @author Dmitriy Grigoriev
 * @since 1.7.5
 */
public class Action {

  public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";
  public static final String SESSION_ID = "session_id";
  public static final String CSS_SELECTOR = "#jsJobResults article";
  private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
  public static final String EMPTY = "";


  /**
   * Parsing jobs by cookie session_id
   */
  public void getJobs() {
    try {
      Document doc = Jsoup.connect("https://www.upwork.com/find-work-home/?topic=1965391")
        .userAgent(USER_AGENT)
        .cookie(SESSION_ID, "452e3d54b4d95650faa445f0cbeb9e31")
        .get();

      Elements newsHeadlines = doc.select(CSS_SELECTOR);
      List<Job> jobs = new ArrayList<>();
      ListUtils.emptyIfNull(newsHeadlines)
        .forEach(element -> fillJobList(jobs, element));

      //if socket timeout, try again
      System.out.println();
    } catch (SocketTimeoutException e) {
      getJobs();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  private void fillJob(Element element, Job job) {
    job.setId(element.attr("data-id"));
    job.setTitle(getByClass(element, "oVisitedLink"));
    try {
      job.setUrl(new URL(
        new URL("https://www.upwork.com"), getByClassAndAttr(element, "oVisitedLink", "href")));
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    job.setDescription(
      "".equals(getByClass(element, "jsFull")) ? getByClass(element, "oDescription") : getByClass(element, "jsFull"));
    job.setType(JobType.get(getByClass(element, "jsType")));
    job.setBudget(getByClass(element, "jsBudget"));
    job.setPublishTime(getPublishTimeFromJson(element.attr("data-relevance")));
    job.setLevel(getById(element, "jsTier"));
    job.setDuration(getByClass(element, "jsDuration"));
    job.setPayIndicator(getByClassAndAttr(element, "oSpendIcon", "title"));
    job.setStarsInfo(getByClassAndAttr(element, "oStarsContainer", "data-content"));
  }
  /**
   * Fill job and add to list
   *
   * @param jobs job list
   * @param element html element
   */
  private void fillJobList(List<Job> jobs, Element element) {
    Job job = new Job();
    fillJob(element, job);
    jobs.add(job);
  }


  private Calendar getPublishTimeFromJson(String jsonObj) {
    if (Objects.nonNull(jsonObj)) {
      String json = String.valueOf(new JSONObject(jsonObj).get("publish_time"));
      Calendar calendar = Calendar.getInstance();
      try {
        calendar.setTime(new SimpleDateFormat(DATE_PATTERN).parse(json));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return calendar;
    }
    return null;
  }

  public String getById(Element element, String id) {
    return Optional.ofNullable(element.getElementById(id))
      .map(Element::text)
      .map(String::trim)
      .orElse(EMPTY);
  }

  public String getByClass(Element element, String class_name) {
    return Optional.ofNullable(element.getElementsByClass(class_name))
      .map(Elements::text)
      .map(String::trim)
      .orElse(EMPTY);
  }

  public String getByClassAndAttr(Element element, String class_name, String attr) {
    return Optional.ofNullable(element.getElementsByClass(class_name))
      .map(item -> item.attr(attr))
      .map(String::trim)
      .orElse(EMPTY);
  }
}
