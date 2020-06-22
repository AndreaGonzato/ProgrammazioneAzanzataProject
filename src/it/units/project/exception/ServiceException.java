package it.units.project.exception;

// Throw this exception when want to block RPC methods when a failure occurs.
public class ServiceException extends Exception {
  public ServiceException(String message) {
    super(message);
  }
}
