package it.units.project.request;

import java.io.IOException;
import java.net.ProtocolException;

public interface Request {
  String solve() throws ProtocolException;
}
