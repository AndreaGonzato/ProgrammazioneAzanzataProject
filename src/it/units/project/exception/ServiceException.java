package it.units.project.exception;

// Throw this exception when want to block RPC methods when a failure occurs.
// all rights of this class go to: https://www.javadoc.io/doc/com.google.protobuf/protobuf-java/3.0.2/com/google/protobuf/ServiceException.html
public class ServiceException extends Exception {
  public ServiceException(String message) {
    super(message);
  }
}
