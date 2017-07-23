package MULTIPLAYER;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TicTacToeClient {
    private JFrame frame = new JFrame("Tic Tac Toe");
    private JLabel messageLabel = new JLabel("");
    private ImageIcon icon;
    private ImageIcon opponentIcon;
    private JButton[] board = new JButton[9];
    private JButton currentSquare;
    private static int PORT;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private final String PLAYER_O   = "/resources/blueCircle.png";
    private final String PLAYER_X = "/resources/redCross.png";
    private final String FREE_ICON     = "/resources/greenFree.png";
    //private final String ICON = "/resources/icon.png";

    public TicTacToeClient(){

    }

    public TicTacToeClient(String serverAddress, int port) throws Exception {
        PORT = port;
        if(serverAddress.isEmpty() || serverAddress.equals(""))
            serverAddress = "localhost";
        socket = new Socket(serverAddress, PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        messageLabel.setBackground(Color.lightGray);
        frame.getContentPane().add(messageLabel, "South");
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));
        for (int i = 0; i < board.length; i++) {
            final int j = i;
            board[i] = new JButton();
            board[i].setIcon(new ImageIcon(getClass().getResource(FREE_ICON)));
            board[i].addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    currentSquare = board[j];
                    out.println("MOVE " + j);
                }
            });
            boardPanel.add(board[i]);
        }
        frame.getContentPane().add(boardPanel, "Center");
    }

    public void play() throws Exception {
        String response;
        try {
            response = in.readLine();
            if (response.startsWith("WELCOME")) {
                char mark = response.charAt(8);
                if(mark == 'X')
                    icon = new ImageIcon(getClass().getResource(PLAYER_X));
                else
                    icon = new ImageIcon(getClass().getResource(PLAYER_O));
                if(mark == 'X')
                    opponentIcon = new ImageIcon(getClass().getResource(PLAYER_O));
                else
                    opponentIcon = new ImageIcon(getClass().getResource(PLAYER_X));
                frame.setTitle("Białek Tomasz - Tic Tac Toe [Player " + mark + "]");
            }
            while (true) {
                response = in.readLine();
                if (response.startsWith("VALID_MOVE")) {
                    messageLabel.setText("Valid move, please wait");
                    currentSquare.setIcon(icon);
                    currentSquare.setDisabledIcon(icon);
                    currentSquare.setEnabled(false);
                }
                else if (response.startsWith("OPPONENT_MOVED")) {
                    int loc = Integer.parseInt(response.substring(15));
                    board[loc].setIcon(opponentIcon);
                    board[loc].setDisabledIcon(opponentIcon);
                    board[loc].setEnabled(false);
                    messageLabel.setText("Opponent moved, your turn");
                }
                else if (response.startsWith("VICTORY")) {
                    messageLabel.setText("You have won this round");
                    break;
                }
                else if (response.startsWith("DEFEAT")) {
                    messageLabel.setText("You have lost this round");
                    break;
                }
                else if (response.startsWith("TIE")) {
                    messageLabel.setText("This round is a draw");
                    break;
                }
                else if (response.startsWith("MESSAGE")) {
                    messageLabel.setText(response.substring(8));
                }
            }
            out.println("QUIT");
        }
        finally {
            socket.close();
        }
    }

    private boolean wantsToPlayAgain() {
        int response = JOptionPane.showConfirmDialog(frame, "Would you like to play again?",
                "Białek Tomasz - Tic Tac Toe", JOptionPane.YES_NO_OPTION);
        frame.dispose();
        return response == JOptionPane.YES_OPTION;
    }

    public void runClient(String address, int port) throws Exception {
        while (true) {
            String serverAddress = address;
            TicTacToeClient client = new TicTacToeClient(serverAddress, port);
            client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            client.frame.setSize(new Dimension(400, 400));
            client.frame.setMinimumSize(new Dimension(200, 200));
            client.frame.setVisible(true);
            client.frame.setResizable(true);
            client.play();
            if (!client.wantsToPlayAgain()) {
                break;
            }
        }
    }
}