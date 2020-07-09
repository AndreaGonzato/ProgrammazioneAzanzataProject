package it.units.project.request;

import it.units.project.response.Response;
import it.units.project.server.SynchronizedServerData;

import java.util.Objects;


public class StatRequest implements Request {
  private final String request;


  public StatRequest(String request) {
    this.request = request;
  }


  public String solve() {
    final long startTime = System.currentTimeMillis();
    double result = 0;
    switch (request) {
      case "STAT_REQS":
        result = SynchronizedServerData.getResponsesNumber();
        break;
      case "STAT_AVG_TIME":
        result = SynchronizedServerData.getAverageResponseTime();
        break;
      case "STAT_MAX_TIME":
        result = SynchronizedServerData.getMaximumResponseTime();
        break;
      default:
        return Response.generateErrorResponse("(ProtocolException) Invalid Stat Request");
    }
    double processTime = ((double) System.currentTimeMillis() - startTime) / 1000;
    SynchronizedServerData.addResponseTime(processTime);
    return Response.generateOkResponse(processTime, result);
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StatRequest that = (StatRequest) o;
    return Objects.equals(request, that.request);
  }


  @Override
  public int hashCode() {
    return Objects.hash(request);
  }

}
