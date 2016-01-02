package com.dimanych.controller;

import com.dimanych.Action;
import com.dimanych.Parser;
import com.dimanych.entity.Job;
import com.dimanych.entity.message.WarningMsg;
import com.dimanych.util.Params;
import com.dimanych.util.Util;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

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
  @FXML
  private Label jobTitle;
  @FXML
  private WebView jobDesc;
  @FXML
  private Label jobBudget;
  @FXML
  private Label jobDuration;
  @FXML
  private Label jobLevel;
  @FXML
  private Label jobPayIndicator;
  @FXML
  private Label jobType;
  @FXML
  private Label jobPublishTime;
  @FXML
  private Label jobStarsInfo;
  @FXML
  private Hyperlink jobUrl;
  @FXML
  private Label parsingStatus;

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

  public Label getJobDuration() {
    return jobDuration;
  }

  public void setJobDuration(Label jobDuration) {
    this.jobDuration = jobDuration;
  }

  public Label getJobBudget() {
    return jobBudget;
  }

  public void setJobBudget(Label jobBudget) {
    this.jobBudget = jobBudget;
  }

  public WebView getJobDesc() {
    return jobDesc;
  }

  public void setJobDesc(WebView jobDesc) {
    this.jobDesc = jobDesc;
  }

  public Label getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(Label jobTitle) {
    this.jobTitle = jobTitle;
  }

  public TableColumn<Job, String> getJobTitleId() {
    return jobTitleId;
  }

  public void setJobTitleId(TableColumn<Job, String> jobTitleId) {
    this.jobTitleId = jobTitleId;
  }

  public TableColumn<Job, String> getJobTitleColumn() {
    return jobTitleColumn;
  }

  public void setJobTitleColumn(TableColumn<Job, String> jobTitleColumn) {
    this.jobTitleColumn = jobTitleColumn;
  }

  public TableColumn<Job, String> getJobDateColumn() {
    return jobDateColumn;
  }

  public void setJobDateColumn(TableColumn<Job, String> jobDateColumn) {
    this.jobDateColumn = jobDateColumn;
  }

  public Label getJobLevel() {
    return jobLevel;
  }

  public void setJobLevel(Label jobLevel) {
    this.jobLevel = jobLevel;
  }

  public Label getJobPayIndicator() {
    return jobPayIndicator;
  }

  public void setJobPayIndicator(Label jobPayIndicator) {
    this.jobPayIndicator = jobPayIndicator;
  }

  public Label getJobType() {
    return jobType;
  }

  public void setJobType(Label jobType) {
    this.jobType = jobType;
  }

  public Label getJobPublishTime() {
    return jobPublishTime;
  }

  public void setJobPublishTime(Label jobPublishTime) {
    this.jobPublishTime = jobPublishTime;
  }

  public Label getJobStarsInfo() {
    return jobStarsInfo;
  }

  public void setJobStarsInfo(Label jobStarsInfo) {
    this.jobStarsInfo = jobStarsInfo;
  }

  public Hyperlink getJobUrl() {
    return jobUrl;
  }

  public void setJobUrl(Hyperlink jobUrl) {
    this.jobUrl = jobUrl;
  }

  public Label getParsingStatus() {
    return parsingStatus;
  }

  public void setParsingStatus(Label parsingStatus) {
    this.parsingStatus = parsingStatus;
  }

  private InitSceneService actions;

  @FXML
  public void initialize() {
    load();
  }

  public void load() {
    Task action = new Action();

    action.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
      ObservableList<Job> jobs = FXCollections.observableArrayList((List<Job>) action.getValue());
      jobTitleColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getTitle()));
      jobDateColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(Util.getDate(cell.getValue().getPublishTime())));

      jobList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Job>() {
        @Override
        public void changed(ObservableValue<? extends Job> observable, Job oldValue, Job newValue) {
          if (Objects.isNull(newValue)) {
            return;
          }
          jobTitle.setText(newValue.getTitle());
          jobDesc.getEngine().loadContent(newValue.getDescription());
          jobBudget.setText(newValue.getBudget());
          jobDuration.setText(newValue.getDuration());
          jobLevel.setText(newValue.getLevel());
          jobPayIndicator.setText(newValue.getPayIndicator());
          jobPublishTime.setText(Util.getDate(newValue.getPublishTime()));
          jobStarsInfo.setText(newValue.getStarsInfo());
          jobType.setText(newValue.getType().toString());
          jobUrl.setText("https://upwork.com" + newValue.getUrl().getPath());
          jobUrl.setOnMouseClicked(event1 -> {
            getApp().getHostServices().showDocument(jobUrl.getText());
          });
        }
      });
      jobList.setItems(jobs);
      if (CollectionUtils.isEmpty(jobs)) {
        parsingStatus.setText("Nothing loaded");
      } else {
        parsingStatus.setText("Loaded");
      }
    });
    new Thread(action).start();
    parsingStatus.setText("Loading from upwork...");
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
