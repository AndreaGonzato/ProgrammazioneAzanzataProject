package it.units.project.request;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Variable {

  String name;
  double lower;
  double step;
  double upper;


  public Variable(String variableDefinition){
    // x0:-1:0.1:1
    System.out.println("TEST: "+variableDefinition);

  }


}
