package it.units.project.response;

import java.util.Locale;

public class Response {
  private static final double MAX_NUMBER_NOT_EXPONENTIAL = 999999.999999;
  private static final double MIN_NUMBER_NOT_EXPONENTIAL = 0.000010;


  public static String generateOkResponse(double computationTime, double result) {
    String resultString;
    if ((Math.abs(result) > MAX_NUMBER_NOT_EXPONENTIAL && !Double.isInfinite(result)) || (Math.abs(result) < MIN_NUMBER_NOT_EXPONENTIAL && !(result == 0))) {
      resultString = String.format(Locale.ROOT, "%.4E", result);
    } else {
      resultString = String.format(Locale.ROOT, "%.6f", result);
    }
    return String.format(Locale.ROOT, "OK;%.3f;%s", computationTime, resultString);
  }


  public static String generateErrorResponse(String errorDescription) {
    return String.format("ERR;%s", errorDescription);
  }

}
