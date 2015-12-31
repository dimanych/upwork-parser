package com.dimanych.entity.message;

import javafx.beans.NamedArg;
import javafx.scene.control.Alert;

/**
 * <p>Сообщения, выводимые пользователю</p>
 *
 * @author Dmitriy Grigoriev
 * @since 1.0
 */
public class Message extends Alert {

  public Message(@NamedArg("alertType") AlertType alertType) {
    super(alertType);
  }

  public Message(String msg, @NamedArg("alertType") AlertType alertType) {
    this(alertType);
    setHeaderText(null);
    setContentText(msg);
  }
}
