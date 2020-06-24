package it.units.project.server;

import java.net.Socket;

public class ClientHandler extends Thread{
  private final Socket socket;
  private final LineProcessingServer server;

  public ClientHandler(Socket socket, LineProcessingServer server){
    this.socket = socket;
    this.server = server;
  }

  public void run() {

  }

}
