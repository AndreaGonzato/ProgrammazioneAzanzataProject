package it.units.project;

import it.units.project.expression.ExpressionCalculator;
import it.units.project.server.ExecutorLineProcessingServer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Main {

    public static void main(String[] args) {

        // TEST
        ExpressionCalculator ec = new ExpressionCalculator("(28-((4+1)^2))");
        System.out.println("result: "+ ec.calculate());

        Function<String, String> processingFunction = ((s) -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return s.toUpperCase();
        });

        ExecutorLineProcessingServer server = new ExecutorLineProcessingServer(10000, "BYE", processingFunction, 1);
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace(); // TO DO
        }
    }
}