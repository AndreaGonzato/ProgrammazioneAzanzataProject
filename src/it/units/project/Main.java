package it.units.project;

import it.units.project.request.ComputationRequest;
import it.units.project.server.LineProcessingServer;

import java.io.IOException;
import java.net.ProtocolException;

public class Main {

  public static void main(String[] args) {

    //TEST
    ComputationRequest computationRequest = new ComputationRequest("MAX_LIST;x:1:1:3;(x+5)");
    try {
      computationRequest.solve();
    } catch (ProtocolException e) {
      System.err.printf("IO error: %s%n", e);
    }




    System.out.println("SUPERATO TEST e sto continuando nel Main()");


    LineProcessingServer server = new LineProcessingServer(10000, "BYE", 1);
    try {
      server.start();
    } catch (IOException e) {
      e.printStackTrace(); // TO DO
    }
  }
}