package it.units.project.server;

import it.units.project.ComputationRequest;
import it.units.project.Request;
import it.units.project.StatRequest;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LineProcessingServer {
  private final int port;
  private final String quitCommand;
  private final ExecutorService executorComputationRequest;

  public LineProcessingServer(int port, String quitCommand, int concurrentClients) {
    this.port = port;
    this.quitCommand = quitCommand;
    executorComputationRequest = Executors.newFixedThreadPool(concurrentClients);
  }

  public void start() throws IOException {
    try (ServerSocket serverSocket = new ServerSocket(port)) {
      while (true) {
        try {
          final Socket socket = serverSocket.accept();

          // create a new Thread that handle the connection
          new Thread(() -> {
            try (socket) {
              BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
              BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
              while (true) {
                String command = br.readLine();
                if (command == null) {
                  System.err.println("Client abruptly closed connection");
                  break;
                }
                if (command.equals(quitCommand)) {
                  break;
                }
                if (command.length() >= 5 && command.substring(0, 5).equals("STAT_")) {
                  // stat request
                  Request request = new StatRequest(command);
                  bw.write(request.solve() + System.lineSeparator());
                  bw.flush();
                } else {
                  // computation request
                  executorComputationRequest.submit(() -> {
                    try {
                      Request request = new ComputationRequest(command);
                      bw.write(request.solve() + System.lineSeparator());
                      bw.flush();
                    } catch (IOException e) {
                      System.err.printf("IO error: %s", e);
                    }
                  });
                }
              }
            } catch (IOException e) {
              System.err.printf("IO error: %s", e);
            }
          }).start(); // start the new Thread

        } catch (IOException e) {
          System.err.printf("Cannot accept connection due to %s", e);
        }
      }
    } finally {
      executorComputationRequest.shutdown();
    }
  }


}