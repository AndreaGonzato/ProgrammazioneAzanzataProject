package it.units.project;

import java.io.IOException;

public class ComputationRequest implements Request {
  private String request;
  private ComputationRequestType type;

  private final int MINIMUM_REQUEST_LENGTH = 4;

  public ComputationRequest(String request) {
    this.request = request;
  }

  public String solve() throws IOException {
    if (request.length() < MINIMUM_REQUEST_LENGTH) {
      throw new IOException("Computation Request is not proper format, it has not the minimum chars"+System.lineSeparator());
    }

    int endIndex = 4; // end index for substring
    switch (request.substring(0, endIndex)) {
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
      case "COUN": {
        //  COUNT_
        endIndex = 6;
        if (request.length() < endIndex) {
          throw new IOException("Computation Request is not proper format, it has not the minimum chars"+System.lineSeparator());
        }
        if (request.substring(0, endIndex).equals("COUNT_")) {
          type = ComputationRequestType.COUNT;
          break;
        }
      }
      default:
        throw new IOException("Computation Request is not proper format, unrecognized Computation Kind: " + request.substring(0, endIndex) +System.lineSeparator());
    }
    System.out.println("type: " + type);
    return request.toUpperCase();
  }
}
