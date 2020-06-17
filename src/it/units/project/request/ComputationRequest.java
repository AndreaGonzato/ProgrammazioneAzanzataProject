package it.units.project.request;


import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComputationRequest implements Request {
  private final String request;
  private ComputationKind computationKind;
  private ValuesKind valuesKind;
  private List<Variable> variablesList;

  private static final String GENERAL_DESCRIPTION_FOR_EXCEPTION = "Computation Request is not in a proper format,";

  public ComputationRequest(String request) {
    this.request = request;
  }

  public String solve() throws ProtocolException {
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

    // TEST
    System.out.println("computationKind: " + computationKind); // TEST
    System.out.println("valuesKind: " + valuesKind.toString()); // TEST
    /*
    for (int i = 0; i < variablesList.size(); i++) {
      System.out.println(variablesList.get(i).name);
      System.out.println(variablesList.get(i).lower);
      System.out.println(variablesList.get(i).step);
      System.out.println(variablesList.get(i).upper);
    }
     */


    return request.toUpperCase();
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
              "Computation Kind is not defined at the beginning of the request");
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
    variablesList = new ArrayList<Variable>();
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
      variablesList.add(new Variable(variableName, low, step, upper));
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
