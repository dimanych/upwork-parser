package com.dimanych;

import com.dimanych.entity.Job;
import com.dimanych.entity.JobType;
import javafx.concurrent.Task;
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
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * <p></p>
 *
 * @author Dmitriy Grigoriev
 * @since 1.0
 */
public class Action extends Task {

  public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";
  public static final String SESSION_ID_ATTR = "session_id";
  public static final String JOBS_CSS_SELECTOR = "#jsJobResults article";
  public static final String CUSTOMER_CSS_SELECTOR = "div.col-md-3";
  private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
  public static final String EMPTY = "";
  public static final String SESSION_ID = "014b432e76250be4807bec782c25c5fd";
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  /**
   * Парсим jobs по кукисам session_id
   */
  public List<Job> getJobs(String url) {
    List<Job> jobs = new ArrayList<>();
      Document doc = connect(url);

      Elements newsHeadlines = doc.select(JOBS_CSS_SELECTOR);
      ListUtils.emptyIfNull(newsHeadlines)
        .forEach(element -> fillJobList(jobs, element));
    return jobs;
  }

  /**
   * Пытаемся соединиться с заданным url
   *
   * @param url строка соединения
   * @return
   */
  public Document connect(String url) {
    try {
      return Jsoup.connect(url)
        .userAgent(USER_AGENT)
              .cookie(SESSION_ID_ATTR, SESSION_ID)
              .timeout(5000)
              .get();
    } catch (SocketTimeoutException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new Document(EMPTY);
  }

  private void fillJob(Element element, Job job) {
//    if (element.attr("class").contains("js-similar-tile")) {
//      fillJobSearch(element, job);
//    } else {
      fillJobSaved(element, job);
//    }
  }

  private void fillJobSaved(Element element, Job job) {
    job.setId(element.attr("data-id"));
    job.setTitle(getByClass(element, "oVisitedLink"));
    try {
      job.setUrl(new URL(
        new URL("https://www.upwork.com"), getByClassAndAttr(element, "oVisitedLink", "href")));
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    job.setType(JobType.get(getByClass(element, "jsType")));
    job.setCustomer(getByClass(element, "oSpendIndicator"));
    job.setDescription(
      "".equals(getByClass(element, "jsFull")) ? getHtmlByClass(element, "oDescription") : getHtmlByClass(element, "jsFull"));
    job.setBudget(getByClass(element, "jsBudget"));
    job.setPublishTime(getPublishTimeFromJson(element.attr("data-relevance"), "publish_time"));
    job.setLevel(getById(element, "jsTier"));
    job.setDuration(getByClass(element, "jsDuration"));
    job.setPayIndicator(getByClassAndAttr(element, "oSpendIcon", "title"));
    job.setStarsInfo(getByClassAndAttr(element, "oStarsContainer", "data-content"));
  }

  private void fillJobSearch(Element element, Job job) {
    job.setId(element.attr("data-qm"));
    job.setTitle(getByClass(element, "visited"));
    try {
      job.setUrl(new URL(
        new URL("https://www.upwork.com"), getByClassAndAttr(element, "visited", "href")));
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    job.setType(JobType.get(getByClass(element, "js-type")));
    job.setDescription(getByClass(element, "description"));
    job.setBudget(getByClass(element, "js-budget"));
    job.setPublishTime(getPublishTimeFromJson(element.attr("data-relevance"), "publishTime"));
    job.setLevel(getByClass(element, "js-contractor-tier"));
    job.setDuration(getByClass(element, "js-duration"));
    job.setCustomer(getByClassAndAttr(element, "o-spend-icon", "title"));
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


  private Calendar getPublishTimeFromJson(String jsonObj, String attr) {
    if (Objects.nonNull(jsonObj)) {
      String json = String.valueOf(new JSONObject(jsonObj).get(attr));
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

  public String getHtmlByClass(Element element, String class_name) {
    return Optional.ofNullable(element.getElementsByClass(class_name))
      .map(Elements::html)
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

  @Override
  protected List<Job> call() throws Exception {
    return getJobs("https://www.upwork.com/find-work-home/?topic=1965948");
//    return getJobsTest();
  }


  public List<Job> getJobsTest() {
    List<Job> jobs = new ArrayList<>();
    Calendar cal = Calendar.getInstance();
    for (int i=0; i<2; i++) {
      Job job = new Job();
      job.setId(i+"id"+cal.getTimeInMillis());
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
      job.setTitle("Title:"+cal.getTimeInMillis());
      jobs.add(job);
    }
    return jobs;
  }
}
