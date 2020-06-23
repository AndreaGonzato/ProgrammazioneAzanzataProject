package it.units.project.expression;

import it.units.project.exception.ServiceException;
import it.units.project.request.ComputationKind;

import java.net.ProtocolException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Expression {
  private final String definition;
  private final Set<Variable> variables;
  private final Set<List<Double>> tuples;
  private final ComputationKind computationKind;


  public Expression(String definition, Set<Variable> variables, Set<List<Double>> tuples, ComputationKind computationKind) throws ProtocolException, ServiceException {
    this.definition = definition;
    this.variables = variables;
    this.tuples = tuples;
    this.computationKind = computationKind;
    Parser parser = new Parser(definition);
    try {
      parser.parse(false);
    } catch (IllegalArgumentException e) {
      throw new ProtocolException("This expression: '" + definition + "' does not respect the protocol");
    }
    if (!allVariablesAreDefined() && !computationKind.equals(ComputationKind.COUNT)) {
      throw new ServiceException("This expression: '" + definition + "' does not have all the variables correctly defined");
    }

  }

  public double evaluate() {

    NumericalExpression[] numericalExpressions = new NumericalExpression[Math.max(1, tuples.size())];

    Iterator<List<Double>> iterator = tuples.iterator();
    for (int i = 0; i < tuples.size(); i++) {
      String expression = definition;
      List<Double> values = iterator.next();
      int j = 0;
      // replace all the reference to variables with it current value in the iterator
      for (Variable variale : variables) {
        expression = expression.replaceAll(variale.getName(), Double.toString(values.get(j)));
        j++;
      }
      numericalExpressions[i] = new NumericalExpression(expression);
    }

    if (tuples.size() == 0) {
      numericalExpressions[0] = new NumericalExpression(definition);
      if (computationKind.equals(ComputationKind.COUNT)) {
        return 0;
      }
    }
    return computationKind.getFunction().apply(numericalExpressions);

  }


  boolean allVariablesAreDefined() {
    String string = definition;
    for (Variable variable : variables) {
      string = string.replaceAll(variable.getName(), "");
    }
    String[] stringsToRemove = {"[0-9]+(\\.[0-9]+)?", "\\+", "-", "\\*", "/", "\\^", "\\(", "\\)"};
    for (String s : stringsToRemove) {
      string = string.replaceAll(s, "");
    }
    return string.length() == 0;
  }

}
