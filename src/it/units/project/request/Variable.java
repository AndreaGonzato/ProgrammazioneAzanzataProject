package it.units.project.request;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class Variable {

  private String name;
  private double lower;
  private double step;
  private double upper;
  private Set<Double> values;


  public Variable(String name, double lower, double step, double upper) {
    if (step <= 0) {
      throw new IllegalArgumentException("A step of a variable definition need to be positive");
    }
    this.name = name;
    this.lower = lower;
    this.step = step;
    this.upper = upper;
    int initialCapacity = Math.max(0, (int)((upper-lower)/step)+1);
    values = new LinkedHashSet<>(initialCapacity);
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

  public Set<Double> getValues() {
    return values;
  }
}
