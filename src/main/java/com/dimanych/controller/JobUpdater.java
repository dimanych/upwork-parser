package com.dimanych.controller;

import com.dimanych.Action;
import com.dimanych.entity.Job;
import com.dimanych.entity.JobType;
import com.dimanych.util.Util;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Objects;

/**
 * <p></p>
 *
 * @author Dmitriy Grigoriev
 */
public class JobUpdater implements Runnable {
  private FxController controller;  
 
  public JobUpdater(FxController controller) {
    this.controller = controller;
  }

    @Override
  public void run() {
    Action action = new Action();

    action.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
      ObservableList<Job> jobs = FXCollections.observableArrayList((List<Job>) action.getValue());

      controller.getJobList().setItems(getNewJobs(jobs, controller.getJobList().getItems()));

      controller.getJobTitleColumn().setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getTitle()));
      controller.getJobDateColumn().setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(Util.getDate(cell.getValue().getPublishTime())));

      controller.getJobList().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Job>() {
        @Override
        public void changed(ObservableValue<? extends Job> observable, Job oldJob, Job newJob) {
          if (Objects.isNull(newJob)) {
            return;
          }
          controller.getJobTitle().setText(newJob.getTitle());
          controller.getJobDesc().getEngine().loadContent(newJob.getDescription());
          controller.getJobBudget().setText(newJob.getBudget());
          controller.getJobDuration().setText(newJob.getDuration());
          controller.getJobLevel().setText(newJob.getLevel());
          controller.getJobPublishTime().setText(Util.getDate(newJob.getPublishTime()));
          controller.getJobStarsInfo().setText(newJob.getStarsInfo());
          controller.getJobType().setText(newJob.getType().toString());
          controller.getJobUrl().setText(String.valueOf(newJob.getUrl()));
          controller.getJobUrl().setOnMouseClicked(event1 ->
            controller.getApp().getHostServices().showDocument(controller.getJobUrl().getText()));
          controller.getJobCustomer().setText(newJob.getPayIndicator());

          if (!controller.getLiteMode().isSelected()) {
            controller.getCustomerInfo().setVisible(true);
            Document doc = action.connect(String.valueOf(newJob.getUrl()));
            int sel = newJob.getType().equals(JobType.HOURLY) ? 1 : 2;
            Element customer = doc.select(Action.CUSTOMER_CSS_SELECTOR).get(sel);
              controller.getCustomerInfo().getEngine().loadContent(customer.html());
            Element jobDescr = doc.select("div.air-card").first();
              controller.getJobDesc().getEngine().loadContent(jobDescr.html());
          }

        }
      });

      if (CollectionUtils.isEmpty(jobs)) {
        controller.getParsingStatus().setText("Nothing loaded");
      } else {
          controller.getParsingStatus().setText("Loaded");
      }
    });
    Platform.runLater(() -> {
        controller.getParsingStatus().setText("Loading from upwork...");
    });
    new Thread(action).start();
  }

  private ObservableList<Job> getNewJobs(ObservableList<Job> recievedJobs, ObservableList<Job> oldJobs) {
    if (CollectionUtils.isNotEmpty(oldJobs) && oldJobs.size() > 100) {
      oldJobs.remove(50, 99);
    }

    ListUtils.emptyIfNull(recievedJobs).stream()
      .filter(item -> notExist(item, oldJobs))
      .forEach(item -> oldJobs.add(0, item));
    return oldJobs;
  }

  private boolean notExist(Job job, ObservableList<Job> jobs) {
    return ListUtils.emptyIfNull(jobs).stream()
      .noneMatch(item -> item.getId().equals(job.getId()));
  }
}
