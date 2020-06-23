package it.units.project.request;

import it.units.project.expression.NumericalExpression;

import java.util.function.Function;


public enum ComputationKind {
  MIN((numericalExpressions) -> {
    Double min;
    if (numericalExpressions.length > 0) {
      min = numericalExpressions[0].calculate();
    } else {
      return null;
    }
    for (int i = 1; i < numericalExpressions.length; i++) {
      if (numericalExpressions[i].calculate() < min) {
        min = numericalExpressions[i].calculate();
      }
    }
    return min;
  }),

  MAX((numericalExpressions) -> {
    Double max;
    if (numericalExpressions.length > 0){
      max = numericalExpressions[0].calculate();
    }else {
      return null;
    }
    for(int i=1 ; i<numericalExpressions.length ; i++){
      if(numericalExpressions[i].calculate() > max){
        max = numericalExpressions[i].calculate();
      }
    }
    return max;
  }),

  AVG((numericalExpressions) -> {
    if (numericalExpressions.length < 1){
      return null;
    }
    double sum = 0;
    for (NumericalExpression numericalExpression : numericalExpressions ) {
      sum += numericalExpression.calculate();
    }
    return sum/numericalExpressions.length;
  }),

  COUNT((numericalExpressions ) ->{
    return Double.valueOf(numericalExpressions.length);
  });

  private final Function<NumericalExpression[], Double> function;

  ComputationKind(Function<NumericalExpression[], Double> function) {
    this.function = function;
  }

  public Function<NumericalExpression[], Double> getFunction() {
    return function;
  }
}
