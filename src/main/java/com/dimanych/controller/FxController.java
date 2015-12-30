package com.dimanych.controller;

import com.dimanych.Parser;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import util.Params;
import util.Util;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p></p>
 *
 * @author Dmitriy Grigoriev
 * @since 1.7.5
 */
public class FxController extends AnchorPane {

  private InitSceneService actions;


  public FxController() {
    setActions(new InitSceneParamImpl());
  }

  /**
   * Возвращает инстанс приложения
   *
   * @return
   */
  public Parser getApp() {
    return Parser.getInstance();
  }

  /**
   * Выводим текущую сцену
   *
   * @return
   */
  public Stage getStage() {
    return getApp().getStage();
  }
  /**
   * Меняем сцену
   *
   * @param fxml ссылка на fxml-файл
   * @throws Exception
   */
  public void changeScene(String fxml) {
    FXMLLoader loader = new FXMLLoader();
    InputStream in = Util.resourceAsStream(fxml);
    loader.setBuilderFactory(new JavaFXBuilderFactory());
    loader.setLocation(Util.resource(fxml));
    Pane page = null;
    try {
      page = (Pane) loader.load(in);
    }
    catch (IOException ex) {
      ex.getStackTrace();
    }
    finally {
      try {
        in.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    Scene scene = new Scene(page);
    getStage().setTitle(Params.APP_CAPTION);
    getStage().setScene(scene);
    FxController controller = loader.getController();
    InitSceneService localActions = controller.getActions();
    if (localActions != null) {
      localActions.init(controller);
    }
  }

  /**
   * Дополнительные действия при изменении и запуске сцены
   *
   * @return
   */
  public InitSceneService getActions() {
    return actions;
  }

  public void setActions(InitSceneService actions) {
    this.actions = actions;
  }

  public void run() {
    System.out.println("test");
  }
}
