package it.units.project.expression;

import java.util.Collections;
import java.util.Objects;

public class Variable extends Node {

  private final String name;
  private double lower;
  private double step;
  private double upper;
  private double[] values;


  public Variable(String name) {
    super(Collections.emptyList());
    this.name = name;
  }


  public Variable(String name, double lower, double step, double upper) {
    super(Collections.emptyList());
    if (step <= 0) {
      throw new IllegalArgumentException("A step of a variable definition need to be positive");
    }
    this.name = name;
    this.lower = lower;
    this.step = step;
    this.upper = upper;

    // set the array length
    int k = 0;
    double currentStep = lower;
    while (currentStep <= upper) {
      k++;
      currentStep = lower + (k * step);
    }
    values = new double[k];

    /*
     Observation: next comment line is not fully equivalent to the last section code,
     because in some cases could happen that length < k due to java approximation
    */
    // int length = Math.max(0, (int) ((upper - lower) / step)+1);

    k = 0;
    currentStep = lower;
    while (currentStep <= upper) {
      values[k] = currentStep;
      // increase values
      k++;
      currentStep = lower + (k * step);
    }
    // observation: do not exclude the possibility that values.length == 0
    // this happens when: this.lower > this.upper
  }

  public double[] getValues() {
    return values;
  }


  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Variable variable = (Variable) o;
    return Objects.equals(name, variable.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), name);
  }

  @Override
  public String toString() {
    return name;
  }
}
