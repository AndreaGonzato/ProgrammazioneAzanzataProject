package it.units.project.expression;

public class ExpressionCalculator {

  Node root;

  public ExpressionCalculator(String stringToProcess) {
    Parser parser = new Parser(stringToProcess);
    root = parser.parse();
  }

  public double calculate() {
    return calculateRecursively(root);
  }

  private double calculateRecursively(Node node) throws IllegalArgumentException{
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
