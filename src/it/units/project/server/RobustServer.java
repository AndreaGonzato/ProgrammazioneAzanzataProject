package it.units.project.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class RobustServer {
  private final int port;
  private final String quitCommand;
  private final ExecutorService executorService;


  public RobustServer(int port, String quitCommand, int concurrentClients) {
    this.port = port;
    this.quitCommand = quitCommand;
    executorService = Executors.newFixedThreadPool(concurrentClients);
  }


  public void start() throws IOException {
    try (ServerSocket serverSocket = new ServerSocket(port)) {
      while (true) {
        try {
          Socket socket = serverSocket.accept();
          RobustClientHandler clientHandler = new RobustClientHandler(socket, quitCommand, executorService);
          clientHandler.start();
        } catch (IOException e) {
          System.err.printf("Cannot accept connection due to %s", e);
        }
      }
    } finally {
      executorService.shutdown();
    }
  }


  public String getQuitCommand() {
    return quitCommand;
  }


  public ExecutorService getExecutorService() {
    return executorService;
  }

}