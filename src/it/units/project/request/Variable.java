package it.units.project.request;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Variable {

  private String name;
  private double lower;
  private double step;
  private double upper;


  public Variable(String name, double lower, double step, double upper) {
    this.name = name;
    this.lower = lower;
    this.step = step;
    this.upper = upper;
  }


}
