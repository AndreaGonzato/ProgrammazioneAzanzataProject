package it.units.project.request;

import java.net.ProtocolException;

public class StatRequest implements Request {

  private String request;

  public StatRequest(String request){
    this.request = request;
  }

  @Override
  public String solve() throws ProtocolException {
    return request.toUpperCase();
  }
}
