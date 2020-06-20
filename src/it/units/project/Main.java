package it.units.project;


import it.units.project.exception.ServiceException;
import it.units.project.request.ComputationRequest;
import it.units.project.server.LineProcessingServer;

import java.io.IOException;
import java.net.ProtocolException;
import java.text.ParseException;

public class Main {

  public static void main(String[] args) {

    //TEST
    ComputationRequest computationRequest = new ComputationRequest("MIN_GRID;x:-1:0.1:1,y:-10:1:20;(1+4)");
    try {
      computationRequest.solve();
    }catch (ParseException e){
      System.err.printf("ParseException: %s at index: %d%n", e.getMessage(), e.getErrorOffset());
    }
    catch (ProtocolException | ServiceException e) {
      System.err.printf("%s: %s%n", e.getClass().getSimpleName(), e.getMessage());
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