package it.units.project;

import it.units.project.server.RobustServer;

import java.io.IOException;


public class Main {


  public static void main(String[] args) {
    int port = 10000;
    if (args.length > 0) {
      port = Integer.parseInt(args[0]);
    }
    int processors = Runtime.getRuntime().availableProcessors();

    RobustServer server = new RobustServer(port, "BYE", processors);
    try {
      server.start();
    } catch (IOException e) {
      System.err.printf("IOException: %s", e);
    }
  }

}