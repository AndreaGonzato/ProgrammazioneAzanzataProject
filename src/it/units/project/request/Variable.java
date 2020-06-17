package it.units.project.request;

import java.util.HashSet;
import java.util.Set;

public class Variable {

  public String name;
  public double lower;
  public double step;
  public double upper;
  public Set<Double> values;


  public Variable(String name, double lower, double step, double upper) {
    if (step < 0) {
      throw new IllegalArgumentException("A step of a variable definition need to be positive");
    }
    this.name = name;
    this.lower = lower;
    this.step = step;
    this.upper = upper;
    values = new HashSet<>();

    int k = 0;
    double currentStep = lower;
    while (currentStep <= upper) {
      values.add(currentStep);
      // increase values
      k++;
      currentStep = lower + (k * step);
    }
    // observation: do not exclude the possibility that tuple.size() == 0
    // this happens when: this.lower > this.upper
  }


}
