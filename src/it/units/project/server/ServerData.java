package it.units.project.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerData {

  private static List<Double> responseTimes = Collections.synchronizedList(new ArrayList<>());

  public static synchronized void addResponseTime(double time) {
    responseTimes.add(time);
  }

  public static synchronized int getResponsesNumber() {
    return responseTimes.size();
  }

  public static synchronized double getMaximumResponseTime() {
    double maximumResponseTime = 0;
    for (Double responseTime : responseTimes) {
      if (responseTime > maximumResponseTime) {
        maximumResponseTime = responseTime;
      }
    }
    return maximumResponseTime;
  }

  public static synchronized double getAverageResponseTime() {
    double sum = 0;
    for (Double responseTime : responseTimes) {
      sum += responseTime;
    }
    return sum / responseTimes.size();
  }


}
