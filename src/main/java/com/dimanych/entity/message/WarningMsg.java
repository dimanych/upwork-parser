package com.dimanych.entity.message;


import com.dimanych.util.Params;

/**
 * <p>Сообщения-предупреждения</p>
 *
 * @author Dmitriy Grigoriev
 * @since 1.0
 */
public class WarningMsg extends Message {

  public WarningMsg(String msg) {
    super(msg, AlertType.WARNING);
    setTitle(Params.MSG_WARNING);
    showAndWait();
  }
}
