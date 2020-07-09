package it.units.project.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class SynchronizedServerData {
  private static final List<Double> responseTimes = new ArrayList<>();
  private static final ReadWriteLock lock = new ReentrantReadWriteLock();
  private static final Lock writeLock = lock.writeLock();
  private static final Lock readLock = lock.readLock();


  public static void addResponseTime(double time) {
    try {
      writeLock.lock();
      responseTimes.add(time);
    } finally {
      writeLock.unlock();
    }
  }


  public static int getResponsesNumber() {
    try {
      readLock.lock();
      return responseTimes.size();
    } finally {
      readLock.unlock();
    }
  }


  public static double getMaximumResponseTime() {
    try {
      readLock.lock();
      if (responseTimes.size() == 0) {
        return Double.NaN;
      }
      double maximumResponseTime = 0;
      for (Double responseTime : responseTimes) {
        if (responseTime > maximumResponseTime) {
          maximumResponseTime = responseTime;
        }
      }
      return maximumResponseTime;
    } finally {
      readLock.unlock();
    }
  }


  public static double getAverageResponseTime() {
    try {
      readLock.lock();
      double sum = 0;
      for (Double responseTime : responseTimes) {
        sum += responseTime;
      }
      return sum / responseTimes.size();
    } finally {
      readLock.unlock();
    }
  }

}
