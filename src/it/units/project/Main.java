package it.units.project;

import it.units.project.request.ComputationRequest;
import it.units.project.server.LineProcessingServer;

import java.io.IOException;


public class Main {


  public static void main(String[] args) {

    //TEST
    ComputationRequest computationRequest = new ComputationRequest("MIN_LIST;x:-100:0.01:100;(x*x)");
    String result = computationRequest.solve();
    System.out.println(result);
    System.out.println("SUPERATO TEST e sto continuando nel Main()");


    int port = 10000;
    if (args.length > 0) {
      port = Integer.parseInt(args[0]);
    }
    int processors = Runtime.getRuntime().availableProcessors();

    LineProcessingServer server = new LineProcessingServer(port, "BYE", processors);
    try {
      server.start();
    } catch (IOException e) {
      System.err.printf("IOException: %s", e);
    }
  }

}