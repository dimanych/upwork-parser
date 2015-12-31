package com.dimanych.entity.message;


import com.dimanych.util.Params;

/**
 * <p>Сообщения для некой информации</p>
 *
 * @author Dmitriy Grigoriev
 * @since 1.0
 */
public class InfoMsg extends Message {

  public InfoMsg(String msg) {
    super(msg, AlertType.INFORMATION);
    setTitle(Params.MSG_INFO);
    showAndWait();
  }
}
