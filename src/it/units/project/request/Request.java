package it.units.project.request;

import com.google.protobuf.ServiceException;

import java.io.IOException;
import java.net.ProtocolException;
import java.text.ParseException;

public interface Request {
  String solve() throws ProtocolException, ParseException, ServiceException;
}
