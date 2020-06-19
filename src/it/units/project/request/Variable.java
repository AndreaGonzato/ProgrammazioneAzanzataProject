package it.units.project.request;

import com.google.common.base.Strings;

import java.util.Objects;

public class Variable {

  private String name;
  private double lower;
  private double step;
  private double upper;
  private double[] values;


  public Variable(String name, double lower, double step, double upper) {
    if (step <= 0) {
      throw new IllegalArgumentException("A step of a variable definition need to be positive");
    }
    this.name = name;
    this.lower = lower;
    this.step = step;
    this.upper = upper;
    int length = Math.max(0, (int) ((upper - lower) / step) + 1);
    values = new double[length];

    int k = 0;
    double currentStep = lower;
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


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Variable variable = (Variable) o;
    return name.equals(variable.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
