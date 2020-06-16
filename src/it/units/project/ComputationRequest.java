package it.units.project;

public class ComputationRequest implements Request {
  private String request;

  public ComputationRequest(String request){
    this.request = request;
  }

  public String solve(){
    return request.toUpperCase();
  }
}
