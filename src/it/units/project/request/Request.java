package it.units.project.request;


import it.units.project.exception.ServiceException;

import java.net.ProtocolException;
import java.text.ParseException;

public interface Request {
  String solve() throws ProtocolException, ParseException, ServiceException;
}
