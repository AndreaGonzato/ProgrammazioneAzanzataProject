package it.units.project;

public class StatRequest implements Request {

  private String request;

  public StatRequest(String request){
    this.request = request;
  }

  @Override
  public String solve() {
    return request.toUpperCase();
  }
}
