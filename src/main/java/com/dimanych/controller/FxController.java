package com.dimanych.controller;

import com.dimanych.Action;
import com.dimanych.Parser;
import com.dimanych.entity.Job;
import com.dimanych.entity.message.WarningMsg;
import com.dimanych.util.Params;
import com.dimanych.util.Util;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <p></p>
 *
 * @author Dmitriy Grigoriev
 * @since 1.0
 */
public class FxController extends AnchorPane {

  @FXML
  private ListView rssList;
  @FXML
  private TableView<Job> jobList;
  @FXML
  private TableColumn<Job, String> jobTitleId;
  @FXML
  private TableColumn<Job, String> jobTitleColumn;
  @FXML
  private TableColumn<Job, String> jobDateColumn;

  public ListView getRssList() {
    return rssList;
  }

  public void setRssList(ListView rssList) {
    this.rssList = rssList;
  }

  public TableView getJobList() {
    return jobList;
  }

  public void setJobList(TableView jobList) {
    this.jobList = jobList;
  }

  private InitSceneService actions;

  @FXML
  public void initialize() {
    Task action = new Action();
    action.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
      ObservableList<Job> jobs = FXCollections.observableArrayList((List<Job>) action.getValue());
      jobTitleColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getTitle()));
      jobDateColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(Util.getDate(cell.getValue().getPublishTime())));

      jobList.setOnMouseClicked(new ColumnEventHandler());
      jobList.setItems(jobs);
    });
    new Thread(action).start();
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
    InitSceneService localActions = this.getActions();
    if (localActions != null) {
      localActions.init(this);
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
    new WarningMsg("Ololo");
  }
}
