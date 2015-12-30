package util;

import com.dimanych.Parser;

import java.io.InputStream;
import java.net.URL;

/**
 * <p></p>
 *
 * @author Dmitriy Grigoriev
 * @since 1.0
 */
public class Util {

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

}
