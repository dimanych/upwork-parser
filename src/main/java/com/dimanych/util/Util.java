package com.dimanych.util;

import com.dimanych.Parser;

import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * <p></p>
 *
 * @author Dmitriy Grigoriev
 * @since 1.0
 */
public class Util {

  public static final DateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");
  public static final DateFormat dateFormatToday = new SimpleDateFormat("HH:mm");

  /**
   * Вытягиваем абсолютную ссылку из относительной
   *
   * @param path
   * @return
   */
  public static String pathFromResource(String path) {
    return resource(path).getPath();
  }

  /**
   * Вытягиваем URL на ресурс
   *
   * @param path относительная ссылка
   * @return
   */
  public static URL resource(String path) {
    return Parser.class.getResource(path);
  }

  /**
   * Вытягиваем ресурс как поток по ссылке
   *
   * @param path
   * @return
   */
  public static InputStream resourceAsStream(String path) {
    return Parser.class.getResourceAsStream(path);
  }

  /**
   * Ищем ссылку на компонент по id
   *
   * @param id
   * @return
   */
  public static Object getComponent(String id) {
    return Parser.getInstance().getStage().getScene().lookup(id);
  }

  /**
   * <p>Checks if two calendars represent the same day ignoring time.</p>
   * @param cal1  the first calendar, not altered, not null
   * @param cal2  the second calendar, not altered, not null
   * @return true if they represent the same day
   * @throws IllegalArgumentException if either calendar is <code>null</code>
   */
  public static boolean isSameDay(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) {
      throw new IllegalArgumentException("The dates must not be null");
    }
    return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
  }

  /**
   * <p>Checks if a calendar date is today.</p>
   * @param cal  the calendar, not altered, not null
   * @return true if cal date is today
   * @throws IllegalArgumentException if the calendar is <code>null</code>
   */
  public static boolean isToday(Calendar cal) {
    return isSameDay(cal, Calendar.getInstance());
  }

  public static String getDate(Calendar cal) {
    cal.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
    if (isToday(cal)) {
      return dateFormatToday.format(cal.getTime());
    }
    return dateFormat.format(cal.getTime());
  }
}
