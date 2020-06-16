package it.units.project;

import it.units.project.expression.NumericalExpression;

import java.util.function.Function;

public enum ComputationRequestType {
  MIN((expressions) -> {
    Double min;
    if (expressions.length > 0) {
      min = expressions[0].calculate();
    } else {
      return null;
    }
    for (int i = 1; i < expressions.length; i++) {
      if (expressions[i].calculate() < min) {
        min = expressions[i].calculate();
      }
    }
    return min;
  }),

  MAX((expressions) -> {
    Double max;
    if (expressions.length > 0){
      max = expressions[0].calculate();
    }else {
      return null;
    }
    for(int i=1 ; i<expressions.length ; i++){
      if(expressions[i].calculate() > max){
        max = expressions[i].calculate();
      }
    }
    return max;
  }),

  AVG((expressions) -> {
    if (expressions.length < 1){
      return null;
    }
    double sum = 0;
    for (NumericalExpression numericalExpression : expressions ) {
      sum += numericalExpression.calculate();
    }
    return sum/expressions.length;
  }),

  COUNT((expressions ) ->{
    return Double.valueOf(expressions.length);
  });

  private final Function<NumericalExpression[], Double> function;

  ComputationRequestType(Function<NumericalExpression[], Double> function) {
    this.function = function;
  }

  public Function<NumericalExpression[], Double> getFunction() {
    return function;
  }
}
