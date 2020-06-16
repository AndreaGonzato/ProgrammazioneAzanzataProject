package it.units.project;

import it.units.project.expression.NumericalExpression;
import it.units.project.server.LineProcessingServer;

import java.io.IOException;

public class Main {

  public static void main(String[] args) {

    // TEST
    NumericalExpression ec = new NumericalExpression("(28-((4+1)^2))");
    System.out.println("result: " + ec.calculate());

    Processor processor = new Processor();

    LineProcessingServer server = new LineProcessingServer(10000, "BYE", processor.getFunction(), 1);
    try {
      server.start();
    } catch (IOException e) {
      e.printStackTrace(); // TO DO
    }
  }
}