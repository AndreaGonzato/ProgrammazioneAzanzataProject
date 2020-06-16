package it.units.project.request;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComputationRequest implements Request {
  private final String request;
  private ComputationKind computationKind;
  private ValuesKind valuesKind;
  private List<Variable> variablesList;

  private static String generalDescriptionForException = "Computation Request is not in a proper format,";


  public ComputationRequest(String request) {
    this.request = request;
  }

  public String solve() throws IOException {
    int offset = parseComputationKind(); // assign field computationKind
    if (request.charAt(offset) == '_'){
      offset++;
    }else {
      throw new IOException(String.format(
              "%s char '_' is not present at index: %d in request: %s%n",
              generalDescriptionForException, offset, request));
    }
    offset = parseValuesKind(offset); // assign field valuesKind
    if(request.charAt(offset) == ';' ){
      offset++;
    }else {
      throw new IOException(String.format(
              "%s char ';' is not present at index: %d in request: %s%n",
              generalDescriptionForException, offset, request));
    }
    offset = parseVariables(offset); // fill variablesList
    if (request.charAt(offset) == ';') {
      offset++;
    } else {
      throw new IOException(String.format(
              "%s char ';' is not present at index: %d in request: %s%n",
              generalDescriptionForException, offset, request));
    }


    System.out.println("type: " + computationKind); // TEST
    System.out.println("valuesKind: " + valuesKind.toString()); // TEST
    return request.toUpperCase();
  }

  private int parseComputationKind() throws IOException {
    String regex = "(MAX)|(MIN)|(AVG)|(COUNT)";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(request);
    int offset = 0;
    String type = "";
    if (matcher.find()) {
      if (matcher.start() == 0) {
        type = matcher.group();
        offset = matcher.end();
      }
    }
    if (offset == 0) {
      throw new IOException(String.format(
              "%s Computation Kind is not defined at the beginning of the request: %s%n",
              generalDescriptionForException, request));
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

  private int parseValuesKind(int offset) throws IOException {
    int originalOffset = offset;
    String regex = "(GRID)|(LIST)";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(request);
    String type = "";
    if (matcher.find()) {
      if (matcher.start() == offset) {
        type = matcher.group();
        offset = matcher.end();
      }
    }
    if (offset == originalOffset) {
      throw new IOException(String.format(
              "%s Values Kind are not defined in request: %s%n", generalDescriptionForException, request));
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

  private int parseVariables(int offset) throws IOException {
    variablesList = new ArrayList<Variable>();
    int originalOffset = offset;
    String regex = "[a-z][a-z0-9]*:[0-9]+(\\.[0-9]+)?:[0-9]+(\\.[0-9]+)?:[0-9]+(\\.[0-9]+)?";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(request);
    String variableDefinition = null;
    while (matcher.find()) {
      if (matcher.start() == offset) {
        variableDefinition = matcher.group();
        variablesList.add(new Variable(variableDefinition));
        offset = matcher.end();
        if (request.charAt(offset) == ',') {
          offset++;
        }
      }
    }
    if (originalOffset == offset){
      throw new IOException(String.format(
              "%s a variable definition need to be specified in request: %s%n",
              generalDescriptionForException, request));
    }
    return offset;
  }

}
