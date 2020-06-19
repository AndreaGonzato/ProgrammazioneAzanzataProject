package it.units.project;

import it.units.project.expression.*;
import it.units.project.request.ComputationRequest;
import it.units.project.server.LineProcessingServer;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

  public static void main(String[] args) {

    //TEST
    ComputationRequest computationRequest = new ComputationRequest("MAX_GRID;x:1:1:3,y:2:2:6;(x+2)");
    try {
      computationRequest.solve();
    } catch (ProtocolException e) {
      System.err.printf("IO error: %s%n", e);
    }




    System.out.println("superato test e sto continuando nel Main()");


    LineProcessingServer server = new LineProcessingServer(10000, "BYE", 1);
    try {
      server.start();
    } catch (IOException e) {
      e.printStackTrace(); // TO DO
    }
  }
}