package it.units.project.expression;

/*
This link explain and define what is a numerical expression:
https://mathtricks.zohosites.com/Numerical-Expression.html
This class models and calculate numerical expression
 */
public class NumericalExpression {

  private final Node root; // binary tree representing the expression

  public NumericalExpression(String stringToProcess) {
    Parser parser = new Parser(stringToProcess);
    root = parser.parse(true);
  }

  public double calculate() {
    return calculateRecursively(root);
  }

  private double calculateRecursively(Node node) {
    if (node instanceof Operator) {
      double[] array = new double[2];
      array[0] = calculateRecursively(node.getChildren().get(0));
      array[1] = calculateRecursively(node.getChildren().get(1));

      return ((Operator) node).getType().getFunction().apply(array);
    }
    if (node instanceof Constant) {
      return ((Constant) node).getValue();
    }
    // the execution must not get here at run time
    throw new IllegalArgumentException("Unexpected Node: " + node + " during calculation");
  }
}
