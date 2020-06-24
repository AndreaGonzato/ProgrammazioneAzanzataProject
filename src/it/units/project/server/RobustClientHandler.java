package it.units.project.server;

import it.units.project.request.ComputationRequest;
import it.units.project.request.Request;
import it.units.project.request.StatRequest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;


public class RobustClientHandler extends Thread {
  private final Socket socket;
  private final String quitCommand;
  private final ExecutorService executorService;


  public RobustClientHandler(Socket socket, String quitCommand, ExecutorService executorService) {
    this.socket = socket;
    this.quitCommand = quitCommand;
    this.executorService = executorService;
  }


  public void run() {
    try (socket) {
      BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      System.out.println("Established a new connection with client: " + socket.getInetAddress().getHostAddress());
      while (true) {
        String command = br.readLine();
        if (command == null) {
          System.err.println("Client "+socket.getInetAddress().getHostAddress()+" closed connection abruptly");
          break;
        }
        if (command.equals(quitCommand)) {
          System.out.println("Closing connection with client: " + socket.getInetAddress().getHostAddress());
          break;
        }
        if (command.length() >= 5 && command.substring(0, 5).equals("STAT_")) {
          // stat request
          Request request = new StatRequest(command);
          bw.write(request.solve() + System.lineSeparator());
          bw.flush();
        } else {
          // computation request
          executorService.submit(() -> {
            try {
              Request request = new ComputationRequest(command);
              bw.write(request.solve() + System.lineSeparator());
              bw.flush();
            } catch (IOException e) {
              System.err.printf("IOException: %s%n", e.getMessage());
            }
          });
        }
      }
    } catch (IOException e) {
      System.err.printf("IOException: %s", e);
    }
  }

}
