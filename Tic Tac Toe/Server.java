import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    static int PORT = 43123;
    private volatile boolean running = true;

    @Override
    public void run() {
        while (running) {
            // Monitor connections to PORT (if it is available).
            try {
                ServerSocket welcomeSocket = new ServerSocket(PORT);
                System.out.println("The server is now running on port " + PORT + "...");


                // When welcomeSocket is contacted, it returns a socket to handle communication
                // with the client.
                Socket connectionSocket = welcomeSocket.accept();
                System.out.println("A user has connected from " + connectionSocket.getInetAddress());

                // Establish the client's input stream.
                BufferedReader clientInput = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

                // Establish the server's output stream.
                DataOutputStream serverOutput = new DataOutputStream(connectionSocket.getOutputStream());

                // Create a server-game based version of TicTacToe.
                ServerGame game = new ServerGame(clientInput, serverOutput);

                try {
                    game.start();
                } catch (CloneNotSupportedException e) {
                    System.err.println("The gamed failed to start.");
                    System.exit(-1);
                } finally {
                    connectionSocket.close();
                    running = false;
                }
            }

            catch (IOException e) {
                //System.out.println("Game finished");
                System.err.println(e);
                running = false;
            }
        }
    }
}
