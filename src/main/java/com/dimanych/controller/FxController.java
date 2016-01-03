package com.dimanych.controller;

import com.dimanych.Action;
import com.dimanych.Parser;
import com.dimanych.entity.Job;
import com.dimanych.entity.JobType;
import com.dimanych.util.Params;
import com.dimanych.util.Util;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <p></p>
 *
 * @author Dmitriy Grigoriev
 * @since 1.0
 */
public class FxController extends AnchorPane {

  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

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
  private Label jobType;
  @FXML
  private Label jobPublishTime;
  @FXML
  private Label jobStarsInfo;
  @FXML
  private Hyperlink jobUrl;
  @FXML
  private Label parsingStatus;
  @FXML
  private WebView customerInfo;
  @FXML
  private Text jobCustomer;
  @FXML
  private CheckBox liteMode;

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

  public WebView getCustomerInfo() {
    return customerInfo;
  }

  public void setCustomerInfo(WebView customerInfo) {
    this.customerInfo = customerInfo;
  }

  public Text getJobCustomer() {
    return jobCustomer;
  }

  public void setJobCustomer(Text jobCustomer) {
    this.jobCustomer = jobCustomer;
  }

  public CheckBox getLiteMode() {
    return liteMode;
  }

  public void setLiteMode(CheckBox liteMode) {
    this.liteMode = liteMode;
  }

  private InitSceneService actions;

  @FXML
  public void initialize() {
    runScheduler();
  }

  public void runScheduler() {
    scheduler.scheduleAtFixedRate(new JobUpdater(), 0, 1, TimeUnit.MINUTES);
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

  private class JobUpdater implements Runnable {

    @Override
    public void run() {
      Action action = new Action();

      action.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
        ObservableList<Job> jobs = FXCollections.observableArrayList((List<Job>) action.getValue());
        jobTitleColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getTitle()));
        jobDateColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(Util.getDate(cell.getValue().getPublishTime())));

        jobList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Job>() {
          @Override
          public void changed(ObservableValue<? extends Job> observable, Job oldJob, Job newJob) {
            if (Objects.isNull(newJob)) {
              return;
            }
            jobTitle.setText(newJob.getTitle());
            jobDesc.getEngine().loadContent(newJob.getDescription());
            jobBudget.setText(newJob.getBudget());
            jobDuration.setText(newJob.getDuration());
            jobLevel.setText(newJob.getLevel());
            jobPublishTime.setText(Util.getDate(newJob.getPublishTime()));
            jobStarsInfo.setText(newJob.getStarsInfo());
            jobType.setText(newJob.getType().toString());
            jobUrl.setText(String.valueOf(newJob.getUrl()));
            jobUrl.setOnMouseClicked(event1 ->
                    getApp().getHostServices().showDocument(jobUrl.getText()));
            jobCustomer.setText(newJob.getPayIndicator());

            if (!liteMode.isSelected()) {
              customerInfo.setVisible(true);
              Document doc = action.connect(String.valueOf(newJob.getUrl()));
              int sel = newJob.getType().equals(JobType.HOURLY) ? 1 : 2;
              Element customer = doc.select(Action.CUSTOMER_CSS_SELECTOR).get(sel);
              customerInfo.getEngine().loadContent(customer.html());
              Element jobDescr = doc.select("div.air-card").first();
              jobDesc.getEngine().loadContent(jobDescr.html());
            }

          }
        });
        jobList.setItems(jobs);
        if (CollectionUtils.isEmpty(jobs)) {
          parsingStatus.setText("Nothing loaded");
        } else {
          parsingStatus.setText("Loaded");
        }
      });
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          parsingStatus.setText("Loading from upwork...");
        }
      });
      new Thread(action).start();
    }
  }

}
