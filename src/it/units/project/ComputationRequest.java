package it.units.project;


import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComputationRequest implements Request {
  private String request;
  private ComputationRequestType type;

  public ComputationRequest(String request) {
    this.request = request;
  }

  public String solve() throws IOException {
    String regex = "(MAX_)|(MIN_)|(AVG_)|(COUNT_)";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(request);
    int endOffset = 0;
    String computationKind = "";
    while (matcher.find()) {
      if (matcher.start() == 0) {
        computationKind = matcher.group();
        endOffset = matcher.end();
      }
    }
    if (endOffset == 0) {
      throw new IOException("Computation Request is not proper format, no Computation Kind is found at the beginning of the request" + System.lineSeparator());
    }

    switch (computationKind) {
      case "MIN_": {
        type = ComputationRequestType.MIN;
        break;
      }
      case "MAX_": {
        type = ComputationRequestType.MAX;
        break;
      }
      case "AVG_": {
        type = ComputationRequestType.AVG;
        break;
      }
      case "COUNT_": {
        type = ComputationRequestType.COUNT;
        break;
      }
    }

    System.out.println("type: " + type);
    return request.toUpperCase();
  }
}
