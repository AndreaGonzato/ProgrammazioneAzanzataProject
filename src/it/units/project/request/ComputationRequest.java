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
  private List<Expression> expressions;
  private Set<List<Double>> tuples;


  private static final String GENERAL_DESCRIPTION_FOR_EXCEPTION = "Computation Request is not in a proper format,";

  public ComputationRequest(String request) {
    this.request = request;
  }

  public String solve() throws ProtocolException {
    parse(); // parse request and assigns fields: computationKind, valuesKind, variables and expressions

    tuples = getTuples(); // assigns tuples


    //TEST
    System.out.println("Tuples: ");
    for (List<Double> test : tuples) {
      System.out.println(test);
    }


    System.out.println(request.toUpperCase()); // TEST
    return request.toUpperCase();
  }

  private Set<List<Double>> getTuples() throws ProtocolException {
    if (valuesKind.equals(ValuesKind.GRID)) {
      // GRID
      /*
      create a List that contains the values Sets of all the variables:
      for example if:
        variable1.getValues() == [1, 2] && variable2.getValues() == [3, 4]
        (variables.add(variable1) ; variables.add(variable2);) && variables.size() == 2;
      then: variablesValues will be: [[1, 2], [3, 4]]
       */
      List<Set<Double>> variablesValues = new ArrayList<>();
      for (Variable variable : variables) {
        // Guava method to convert a double[] in a List<Double>
        List<Double> variableValues = Doubles.asList(variable.getValues());
        // convert a List<Double> in a HashSet<Double> and store this Set as element of a List: variablesValues
        variablesValues.add(new HashSet<>(variableValues));
      }

      // Guava method that return the cartesian product
      return Sets.cartesianProduct(variablesValues);

    } else {
      // LIST
      // checks that the cardinality of values of the variables is consistent
      int variablesValuesCardinality = 0;
      for (Variable variable : variables) {
        if (variablesValuesCardinality == 0) {
          variablesValuesCardinality = variable.getValues().length;
        }
        if (variablesValuesCardinality != 0 && variable.getValues().length != variablesValuesCardinality) {
          throw new ProtocolException("The cardinality of values of the variables must be the same");
        }
      }

      /*
      create a List that contains the values List of all the variables:
      for example if:
        variable1.getValues() == [1, 2, 3] && variable2.getValues() == [4, 5, 6]
        (variables.add(variable1) ; variables.add(variable2);) && variables.size() == 2;
      then variablesValues will be: [[1, 2, 3], [4, 5, 6]]
       */
      List<List<Double>> variablesValues = new ArrayList<>();
      for (Variable variable : variables) {
        // Guava method to convert a double[] in a List<Double>
        List<Double> variableValues = Doubles.asList(variable.getValues());
        variablesValues.add(variableValues);
      }

      Set<List<Double>> result = new LinkedHashSet<>();

      for (int i = 0; i < variablesValuesCardinality; i++) {
        List<Double> list = new ArrayList<>();
        for (int j = 0; j < variables.size(); j++) {
          list.add(variablesValues.get(j).get(i));
        }
        result.add(list);
      }
      return result;
    }
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
    offset = parseVariables(offset); // fill variables Set
    if (request.charAt(offset) == ';') {
      offset++;
    } else {
      throw new ProtocolException(String.format(
              "Semicolon is not present at index: %d in the request", offset));
    }
    offset = parseExpressions(offset); // fill expression List

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

  private int parseExpressions(int offset){
    System.out.println("scrivi codice parseExpressions");
    return 0;
  }

}
