package com.dimanych;

import com.dimanych.controller.FxController;
import javafx.application.Application;
import javafx.stage.Stage;
import com.dimanych.util.Params;

/**
 * <p>com.dimanych.Parser jobs from upwork, using browser cookies</p>
 *
 * @author Dmitriy Grigoriev
 * @since 1.0
 */
public class Parser extends Application {

  private Stage stage;


  @Override
  public void start(Stage primaryStage) throws Exception {
    getInstance().initStage(primaryStage);
    FxController controller = new FxController();
    controller.changeScene(Params.START_FXML);
    primaryStage.show();

  }

  private void initStage(Stage stage) {
    this.stage = stage;
  }

  public Stage getStage() {
    return stage;
  }


  public static class ParserHolder {
    public static final Parser MAIN_APP_INSTANCE = new Parser();
  }

  public static Parser getInstance() {
    return ParserHolder.MAIN_APP_INSTANCE;
  }
}
