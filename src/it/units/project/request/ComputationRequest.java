package it.units.project.request;

import com.google.common.collect.Sets;
import com.google.common.primitives.Doubles;

import it.units.project.exception.ServiceException;
import it.units.project.expression.Expression;
import it.units.project.expression.Variable;
import it.units.project.response.Response;
import it.units.project.server.ServerData;

import java.net.ProtocolException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ComputationRequest implements Request {
  private final String request;
  private ComputationKind computationKind;
  private ValuesKind valuesKind;
  private Set<Variable> variables;
  private Set<List<Double>> tuples;
  private List<Expression> expressions;


  public ComputationRequest(String request) {
    this.request = request;
  }

  public String solve() {
    final long startTime = System.currentTimeMillis();
    try {
      // parse request and assigns fields: computationKind, valuesKind, variables, tuples and expressions
      parse();

      double result;
      if (computationKind.equals(ComputationKind.AVG)) {
        result = expressions.get(0).evaluate();
      } else {
        if (computationKind.equals(ComputationKind.COUNT)) {
          result = tuples.size();
        } else {
          result = expressions.get(0).evaluate();
          for (int i = 1; i < expressions.size(); i++) {
            double expressionResult = expressions.get(i).evaluate();
            if (computationKind.equals(ComputationKind.MAX) && expressionResult > result) {
              result = expressionResult;
            }
            if (computationKind.equals(ComputationKind.MIN) && expressionResult < result) {
              result = expressionResult;
            }
          }
        }
      }

      double processTime = ((double) System.currentTimeMillis() - startTime) / 1000;
      ServerData.addResponseTime(processTime);
      return Response.generateOkResponse(processTime, result);
    } catch (ProtocolException | ServiceException e) {
      return Response.generateErrorResponse(String.format("(%s) %s", e.getClass().getSimpleName(), e.getMessage()));
    } catch (ParseException e) {
      return Response.generateErrorResponse(String.format("(ParseException) %s at index: %d", e.getMessage(), e.getErrorOffset()));
    }
  }


  private void parse() throws ProtocolException, ParseException, ServiceException {
    int offset = parseComputationKind(); // assign field computationKind
    if (offset < request.length() && request.charAt(offset) == '_') {
      offset++;
    } else {
      throw new ParseException("Char '_' is not present", offset);
    }
    offset = parseValuesKind(offset); // assign field valuesKind
    if (offset < request.length() && request.charAt(offset) == ';') {
      offset++;
    } else {
      throw new ParseException("Semicolon is not present", offset);
    }
    offset = parseVariables(offset); // fill variables Set
    tuples = getTuples(); // assigns tuples
    if (offset < request.length() && request.charAt(offset) == ';') {
      offset++;
    } else {
      throw new ParseException("Semicolon is not present", offset);
    }
    offset = parseExpressions(offset); // fill expression List

    if (offset != request.length()) {
      throw new ProtocolException("Delete chars from index: " + offset + " to obtain a syntactic valid request");
    }

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
    String regex = "([a-z][a-z0-9]*):((-[0-9]+)|([0-9]+)(\\.[0-9]+)?):((Infinity)|([0-9]+(\\.[0-9]+)?)):((-[0-9]+)|([0-9]+)(\\.[0-9]+)?)";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(request);
    boolean defineVariable = true; // indicate whether a variable need to be defined

    while (defineVariable && matcher.find() && matcher.start() == offset) {
      String variableName = matcher.group(1);
      double low = Double.parseDouble(matcher.group(2));
      double step = Double.parseDouble(matcher.group(6));
      double upper = Double.parseDouble(matcher.group(10));
      if (low <= upper) {
        variables.add(new Variable(variableName, low, step, upper));
      }
      offset = matcher.end();
      defineVariable = false;
      if (offset < request.length() && request.charAt(offset) == ',') {
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


  private Set<List<Double>> getTuples() throws ProtocolException {
    if (valuesKind.equals(ValuesKind.GRID)) {
      // GRID

      if (variables.size() == 0) {
        return new HashSet<>(); // empty Set
      }

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
          throw new ProtocolException("The cardinality of values of the variables it is not the same for each variable");
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


  private int parseExpressions(int offset) throws ProtocolException, ServiceException {
    expressions = new ArrayList<>();
    String regex = "([a-z][a-z0-9]*)|([0-9]+(\\.[0-9]+)?)|(\\([a-zA-Z0-9\\.\\+\\-\\*\\/\\^\\(\\)]+\\))";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(request);
    boolean defineExpression = true; // indicate whether a expression need to be define
    while (matcher.find(offset) && matcher.start() == offset) {
      expressions.add(new Expression(matcher.group(), variables, tuples, computationKind));
      offset = matcher.end();
      defineExpression = false;
      if (offset < request.length() && request.charAt(offset) == ';') {
        offset++;
        defineExpression = true;
      }
    }
    if (defineExpression | expressions.size() == 0) {
      throw new ProtocolException("Expressions are not properly defined in the request");
    }

    return offset;
  }


}
