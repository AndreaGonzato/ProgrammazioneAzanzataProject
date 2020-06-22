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
    ComputationRequest computationRequest = new ComputationRequest("MAX_LIST;x:0:1:4;((0-1)^0.25)");
    String result = computationRequest.solve();
    System.out.println(result);
    System.out.println("SUPERATO TEST e sto continuando nel Main()");


    int port = 10000;
    if (args.length > 0){
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