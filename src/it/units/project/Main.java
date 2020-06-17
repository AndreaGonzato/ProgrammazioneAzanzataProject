package it.units.project;

import com.google.common.collect.Sets;
import it.units.project.expression.NumericalExpression;
import it.units.project.request.Variable;
import it.units.project.server.LineProcessingServer;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

  public static void main(String[] args) {

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