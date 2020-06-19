package it.units.project.expression;

import it.units.project.request.ComputationKind;

import java.net.ProtocolException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Expression {
  private final String definition;
  private Set<Variable> variables;
  private Set<List<Double>> tuples;
  private ComputationKind computationKind;

  private Node root;

  public Expression(String definition, Set<Variable> variables, Set<List<Double>> tuples, ComputationKind computationKind) throws ProtocolException {
    this.definition = definition;
    this.variables = variables;
    this.tuples = tuples;
    this.computationKind = computationKind;
    Parser parser = new Parser(definition);

    try {
      root = parser.parse();
    } catch (IllegalArgumentException e) {
      throw new ProtocolException("This expression: '" + definition + "' does not respect the protocol");
    }

  }

  public double evaluate() {

    NumericalExpression[] numericalExpressions = new NumericalExpression[tuples.size()];

    Iterator<List<Double>> iterator = tuples.iterator();
    for (int i = 0; i < tuples.size(); i++) {
      String expression = definition;
      List<Double> values = iterator.next();
      int j =0;
      // replace all the reference to variables with it current value in the iterator
      for (Variable variale : variables) {
        expression = expression.replaceAll(variale.getName(), Double.toString(values.get(j)));
        j++;
      }
      numericalExpressions[i] = new NumericalExpression(expression);
    }
    System.out.println("TEST: "+computationKind);
    double result = computationKind.getFunction().apply(numericalExpressions);
    System.out.println("RESULT: " + result);

    return result;
  }
}
