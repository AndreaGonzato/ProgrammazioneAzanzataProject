package it.units.project.request;

import it.units.project.response.Response;
import it.units.project.server.ServerData;


public class StatRequest implements Request {

  private String request;

  public StatRequest(String request) {
    this.request = request;
  }

  @Override
  public String solve() {
    final long startTime = System.currentTimeMillis();
    double result = 0;
    switch (request){
      case "STAT_REQS":
        result = ServerData.getResponsesNumber();
        break;
      case "STAT_AVG_TIME":
        result = ServerData.getAverageResponseTime();
        break;
      case "STAT_MAX_TIME":
        result = ServerData.getMaximumResponseTime();
        break;
      default:
        return Response.generateErrorResponse("(ProtocolException) Invalid Stat Request");
    }
    double processTime = ((double) System.currentTimeMillis() - startTime) / 1000;
    ServerData.addResponseTime(processTime);
    return Response.generateOkResponse(processTime, result);
  }
}
