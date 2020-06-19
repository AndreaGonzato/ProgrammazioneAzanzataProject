package it.units.project.request;


import com.google.common.collect.Sets;
import com.google.common.primitives.Doubles;

import java.net.ProtocolException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComputationRequest implements Request {
  private final String request;
  private ComputationKind computationKind;
  private ValuesKind valuesKind;
  private Set<Variable> variables;
  private Set<List<Double>> tuples;


  private static final String GENERAL_DESCRIPTION_FOR_EXCEPTION = "Computation Request is not in a proper format,";

  public ComputationRequest(String request) {
    this.request = request;
  }

  public String solve() throws ProtocolException {
    parse(); // parse all the request


    if (valuesKind.equals(ValuesKind.GRID)) {
      // GRID
      /*
      create a List that contains the values Sets of all the variables:
      for example if variables.size == 2 && variables.get(0).getValues() == [1, 2] &&  variables.get(1).getValues() == [3, 4]
      then variablesValues will be: [[1, 2], [3, 4]]
       */
      List<Set<Double>> variablesValues = new ArrayList<>();
      for (Variable variable : variables) {
        // Guava method to convert a double[] in a List<Double>
        List<Double> variableValues = Doubles.asList(variable.getValues());
        // convert a List<Double> in a HashSet<Double> and store this Set as element of a List: variablesValues
        variablesValues.add(new HashSet<>(variableValues));
      }

      // Guava method that return the cartesian product
      tuples = Sets.cartesianProduct(variablesValues);

      //TEST
      System.out.println("TEST tuple GRID:");
      for (List<Double> test : tuples) {
        System.out.println(test);
      }

    } else {
      // LIST

      // checks that the cardinality of values of the variables is consistent
      int cardinalityOfValuesOfVariables = 0;
      for (Variable variable : variables) {
        if (cardinalityOfValuesOfVariables == 0) {
          cardinalityOfValuesOfVariables = variable.getValues().length;
        }
        if (cardinalityOfValuesOfVariables != 0 && variable.getValues().length != cardinalityOfValuesOfVariables) {
          throw new ProtocolException("The cardinality of values of the variables must be the same");
        }
      }

      //TEST
      System.out.println("TEST: array delle variabili");
      for (Variable var : variables) {
        System.out.println(Arrays.toString(var.getValues()));
      }

            /*
      create a List that contains the values Sets of all the variables:
      for example if variables.size == 2 && variables.get(0).getValues() == [1, 2, 3] &&  variables.get(1).getValues() == [4, 5, 6]
      then variablesValues will be: [[1, 2, 3], [4, 5, 6]]
       */

      List<Variable> temp = new ArrayList<>(variables);
      double[][] variablesValues = new double[cardinalityOfValuesOfVariables][variables.size()];
      for (int i = 0; i < cardinalityOfValuesOfVariables; i++) {
        double[] array = new double[variables.size()];
        for (int j = 0; j < variables.size(); j++) {
          array[j] = temp.get(j).getValues()[i];
        }
        variablesValues[i] = array;
      }

      //TEST
      System.out.println("TEST: array dei valori di LIST: ");
      for (double[] var : variablesValues) {
        System.out.println(Arrays.toString(var));
      }



    }


    System.out.println(request.toUpperCase()); // TEST
    return request.toUpperCase();
  }

  private void parse() throws ProtocolException {
    int offset = parseComputationKind(); // assign field computationKind
    if (request.charAt(offset) == '_') {
      offset++;
    } else {
      throw new ProtocolException(String.format(
              "Char '_' is not present at index: %d in the request", offset));
    }
    offset = parseValuesKind(offset); // assign field valuesKind
    if (request.charAt(offset) == ';') {
      offset++;
    } else {
      throw new ProtocolException(String.format(
              "Semicolon is not present at index: %d in the request", offset));
    }
    offset = parseVariables(offset); // fill variablesList
    if (request.charAt(offset) == ';') {
      offset++;
    } else {
      throw new ProtocolException(String.format(
              "Semicolon is not present at index: %d in the request", offset));
    }
    //PARSE EXPRESSION
  }

  private int parseComputationKind() throws ProtocolException {
    int offset = 0;
    String regex = "(MAX)|(MIN)|(AVG)|(COUNT)";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(request);
    String type = null;
    if (matcher.find() && matcher.start() == 0) {
      type = matcher.group();
      offset = matcher.end();
    } else {
      throw new ProtocolException(
              "Computation Kind is not properly defined at the beginning of the request");
    }

    switch (type) {
      case "MIN":
        computationKind = ComputationKind.MIN;
        break;
      case "MAX":
        computationKind = ComputationKind.MAX;
        break;
      case "AVG":
        computationKind = ComputationKind.AVG;
        break;
      case "COUNT":
        computationKind = ComputationKind.COUNT;
        break;
    }

    return offset;
  }

  private int parseValuesKind(int offset) throws ProtocolException {
    String regex = "(GRID)|(LIST)";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(request);
    String type = null;
    if (matcher.find() && matcher.start() == offset) {
      type = matcher.group();
      offset = matcher.end();
    } else {
      throw new ProtocolException(
              "Values Kind are not properly defined in the request");
    }

    switch (type) {
      case "GRID":
        valuesKind = ValuesKind.GRID;
        break;
      case "LIST":
        valuesKind = ValuesKind.LIST;
        break;
    }

    return offset;
  }

  private int parseVariables(int offset) throws ProtocolException {
    variables = new LinkedHashSet<>();
    String regex = "([a-z][a-z0-9]*):((-[0-9]+)|([0-9]+)(\\.[0-9]+)?):([0-9]+(\\.[0-9]+)?):((-[0-9]+)|([0-9]+)(\\.[0-9]+)?)";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(request);
    String variableName = null;
    double low, step, upper;
    boolean defineVariable = true; // indicate whether a variable need to be defined

    while (defineVariable && matcher.find() && matcher.start() == offset) {
      variableName = matcher.group(1);
      low = Double.parseDouble(matcher.group(2));
      step = Double.parseDouble(matcher.group(6));
      upper = Double.parseDouble(matcher.group(8));
      if (low <= upper) {
        variables.add(new Variable(variableName, low, step, upper));
      }
      offset = matcher.end();
      defineVariable = false;
      if (request.charAt(offset) == ',') {
        offset++;
        defineVariable = true;
      }
    }
    if (defineVariable) {
      throw new ProtocolException(
              "Variable are not properly defined in the request");
    }
    return offset;
  }

}
