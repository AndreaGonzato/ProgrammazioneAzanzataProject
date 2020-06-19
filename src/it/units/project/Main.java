package it.units.project;

import com.google.common.collect.Sets;
import it.units.project.expression.NumericalExpression;
import it.units.project.request.ComputationRequest;
import it.units.project.request.Variable;
import it.units.project.server.LineProcessingServer;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Main {

  public static void main(String[] args) {



    //TEST
    ComputationRequest computationRequest = new ComputationRequest("MAX_GRID;x1:1:1:3,x2:4:1:6,x2:7:1:8;hggg");
    try {
      computationRequest.solve();
    } catch (ProtocolException e) {
      e.printStackTrace();
    }

    // TEST
    NumericalExpression ec = new NumericalExpression("(28-((4+1)^2))");
    System.out.println("result: " + ec.calculate());


    LineProcessingServer server = new LineProcessingServer(10000, "BYE", 1);
    try {
      server.start();
    } catch (IOException e) {
      e.printStackTrace(); // TO DO
    }
  }
}