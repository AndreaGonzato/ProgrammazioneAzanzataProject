package it.units.project;

import it.units.project.expression.NumericalExpression;
import it.units.project.request.Variable;
import it.units.project.server.LineProcessingServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Main {

  public static void main(String[] args) {

    // TEST
    NumericalExpression ec = new NumericalExpression("(28-((4+1)^2))");
    System.out.println("result: " + ec.calculate());

    // TEST
    Variable v = new Variable("x2", 1, 0.001, 100);
    System.out.println("TEST size: "+ v.tuple.size());



    LineProcessingServer server = new LineProcessingServer(10000, "BYE", 1);
    try {
      server.start();
    } catch (IOException e) {
      e.printStackTrace(); // TO DO
    }
  }
}