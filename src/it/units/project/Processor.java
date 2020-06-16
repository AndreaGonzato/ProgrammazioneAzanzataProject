package it.units.project;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Processor {

  private Function<String,String> function;

  public Processor(){
    function = ((s) -> {
      try {
        TimeUnit.SECONDS.sleep(5);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return s.toUpperCase();
    });
  }

  public Function<String, String> getFunction() {
    return function;
  }
}

