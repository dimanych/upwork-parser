package com.dimanych.controller;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * <p></p>
 *
 * @author Dmitriy Grigoriev
 * @since 1.0
 */
public class ColumnEventHandler implements EventHandler<MouseEvent> {

  @Override
  public void handle(MouseEvent event) {
    System.out.println(event);
  }
}
