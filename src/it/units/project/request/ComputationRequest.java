package it.units.project.request;


import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComputationRequest implements Request {
  private final String request;
  private ComputationKind computationKind;
  private ValuesKind valuesKind;

  private int cursor = 0;

  public ComputationRequest(String request) {
    this.request = request;
  }

  public String solve() throws IOException {
    int offset = parseComputationKind(); // assign field type
    offset = parseValuesKind(offset);
    // TO DO VAR DEFINITION


    System.out.println("type: " + computationKind); // TEST
    System.out.println("valuesKind: "+ valuesKind.toString()); // TEST
    return request.toUpperCase();
  }

  private int parseComputationKind() throws IOException {
    String regex = "(MAX_)|(MIN_)|(AVG_)|(COUNT_)";
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
      throw new IOException("Computation Request is not in a proper format, no Computation Kind is found at the beginning of the request" + System.lineSeparator());
    }

    switch (type) {
      case "MIN_":
        computationKind = ComputationKind.MIN;
        break;
      case "MAX_":
        computationKind = ComputationKind.MAX;
        break;
      case "AVG_":
        computationKind = ComputationKind.AVG;
        break;
      case "COUNT_":
        computationKind = ComputationKind.COUNT;
        break;
    }

    return offset;
  }

  private int parseValuesKind(int offset) throws IOException {
    int originalOffset = offset;
    String regex = "(GRID;)|(LIST;)";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(request);
    String type = "";
    if (matcher.find()) {
      if (matcher.start() == offset) {
        type = matcher.group();
        offset = matcher.end();
      }
    }
    if ( offset == originalOffset) {
      throw new IOException("Computation Request is not in a proper format, no Values Kind defined" + System.lineSeparator());
    }

    switch (type){
      case "GRID;":
        valuesKind = ValuesKind.GRID;
        break;
      case "LIST;":
        valuesKind = ValuesKind.LIST;
        break;
    }

    return offset;
  }

}
