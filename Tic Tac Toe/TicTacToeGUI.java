import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TicTacToeGUI
{
    private final String SERVER_IP    = "127.0.0.1";
    private final int    SERVER_PORT  = 43123;

    private final String FREE_ICON     = "resources/greenFree.png";
    private final String PLAYER_ICON   = "resources/blueCircle.png";
    private final String COMPUTER_ICON = "resources/redCross.png";
    private final String NEW_GAME = "resources/newGame.png";
    private final String CLOSE_GAME = "resources/closeGame.png";
    private final String ICON = "resources/icon.png";

    private int winCount, lossCount, drawCount;

    //Open a file and load the player's statistics
    public synchronized void statsReader() throws IOException {
        File f = new File("stats.txt");
        if(f.isFile() && !f.isDirectory()) {

            File file = new File("stats.txt");

            Scanner readStats = new Scanner(file);
            String stats = readStats.nextLine();

            //Statistics are divided by ";", so it has to be split
            String [] divideStats = stats.split(";");
            winCount = Integer.parseInt(divideStats[0]);
            drawCount = Integer.parseInt(divideStats[1]);
            lossCount = Integer.parseInt(divideStats[2]);
        }
        else{
            PrintWriter writer = new PrintWriter("stats.txt", "UTF-8");
            winCount = 0;
            drawCount = 0;
            lossCount = 0;
            writer.print(winCount+";"+drawCount+";"+lossCount);
            writer.close();
        }
    }

    private JFrame frame;
    private Container content;
    private JPanel buttonPanel, optionsPanel;
    private JButton[] buttons;
    
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    // Handles a click on an enabled grid button.
    private ActionListener gridClickListener = new ActionListener() {
        // Transmit the user's desired grid to the server.
        public void actionPerformed(ActionEvent actionEvent) {
            String buttonNumber = actionEvent.getActionCommand();
            System.out.println("Sending to server: " + buttonNumber);
            out.println(buttonNumber + "\n");

            // Process any commands sent by the server (which should be a gridStatus string).
            processServerCommands();
        }
    };

    // Handles a click on the new game/close game buttons.
    private ActionListener optionsClickListener = new ActionListener() {
        public void actionPerformed(ActionEvent actionEvent) {
            String buttonCommand = actionEvent.getActionCommand();

            // If a player would want to close the game
            if(buttonCommand.equals("close")) {
                try {
                    PrintWriter saveStats = new PrintWriter("stats.txt");
                    saveStats.println(winCount + ";" + drawCount + ";" + lossCount);
                    saveStats.close();

                    out.println("#CG\n");
                    out.close();
                    in.close();
                    socket.close();
                    Sound.sound.stop();
                }
                catch(IOException e) {
                    System.err.println("Error disconnecting from the server.");
                }
                System.exit(0);
            }

            //Start a new game
            if(buttonCommand.equals("new")) {
                showGrid();
                out.println("#NG\n");
                processServerCommands();
            }
        }
    };

    //Set up a server connection, run the game for a player
    public void run() throws InterruptedException {
        // Connect to the TicTacToe server.
        connectServer();

        // Load the game's GUI.
        showGUI();

        // Allow the user to play the game.
        processServerCommands();
    }

    //Create a connection with a server, input and output streams to communicate with a server
    private void connectServer() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch(UnknownHostException e) {
            System.err.println("The specified server host could not be found.");
            System.exit(-1);
        }
        catch(IOException e) {
            System.err.println("An I/O error has occurred. Please ascertain the server is running.");
            System.exit(-1);
        }

        System.out.println("The GUI has connected to the server.");
    }

    // "#T" -- game is a tie
    // "#P" -- the player won the game
    // "#O" -- the opponent won the game
    // => expects "#CG" to end the game or "#NG" to create a new game
    private void processServerCommands() {
        try {
            System.out.println("Processing the server's command...");

            String serverCommand;
            serverCommand = in.readLine();
            System.err.println("Message from server: " + serverCommand);

            // Process any gridStatus strings.
            if(serverCommand.charAt(0) != '#')
                updateGrid(serverCommand);

            // The game has finished, load a player's statistics
            else {
                showOptions();

                // Alert the user to his or her result.
                String title, text;
                if(serverCommand.equals("#T")) {
                    drawCount++;
                    title = "DRAW!";
                    text = "This round is a draw.";
                }
                else if(serverCommand.equals("#P")) {
                    winCount++;
                    title = "VICTORY!";
                    text = "Player has won this round.";}
                else {
                    lossCount++;
                    title = "DEFEAT!";
                    text = "Computer has won this round.";
                }

                text += "\nStatistics:" +
                        "\n• wins: " + winCount +
                        "\n• draws: " + drawCount +
                        "\n• losses: " + lossCount;

                JOptionPane.showMessageDialog(null, text, title, JOptionPane.INFORMATION_MESSAGE);
            }

            //System.out.println("==> Control has returned to the user.");
        }
        catch(IOException e) {
            System.err.println("Error reading commands from the server.");
        }
    }

    //Loads GUI (grids)
    private void showGUI() throws InterruptedException {
        showGrid();
        frame.setVisible(true);
    }

    //Screen, which will appeared after finished a game
    private void showOptions() {
        content.remove(buttonPanel);
        content.add(optionsPanel);
        optionsPanel.setVisible(true);
        optionsPanel.updateUI();
    }

    //Screen with Tic Tac Toe game
    private void showGrid() {
        content.remove(optionsPanel);
        content.add(buttonPanel);
        buttonPanel.setVisible(true);
        buttonPanel.updateUI();
    }

    //Shows images of player, computer or free button
    //Free: "-"
    //Player: "1"
    //Computer: "2"
    private void updateGrid(String gridState) {
        for(int i = 0; i < 9; i++) {
            JButton button = buttons[i];
            char state = gridState.charAt(i);

            if(state == '-') {
                button.setEnabled(true);
            }
                
            //The button is occupied (checks whether it is a player or computer)
            else {
                String icon;
                if(state == '1') icon = PLAYER_ICON;
                else icon = COMPUTER_ICON;

                button.setDisabledIcon(new ImageIcon(getClass().getResource(icon)));
                button.setEnabled(false);
            }
        }       
    }


    public TicTacToeGUI() throws IOException {
        statsReader();
        Runnable server = new Server();
        Thread serverThread = new Thread(server);
        serverThread.start();

        Dimension normal = new Dimension(400, 400);
        Dimension minimum = new Dimension(200, 200);
        Dimension maximum = Toolkit.getDefaultToolkit().getScreenSize();

        // The main frame of the game
        frame = new JFrame("Białek Tomasz - Tic Tac Toe");
        frame.setIconImage(ImageIO.read(getClass().getResource(ICON)));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Maximum, minimum and output size of frame
        frame.setSize(normal);
        frame.setMinimumSize(minimum);
        frame.setMaximumSize(maximum);


        //Grid for Tic Tac Toe game - JPanel holds these grids with buttons
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 3));
        buttons = new JButton[9];
        for(int i = 0; i < buttons.length; i++) {
            JButton button = new JButton(new ImageIcon(getClass().getResource(FREE_ICON)));
            button.setActionCommand(i + "");
            button.addActionListener(gridClickListener);

            buttons[i] = button;
            buttonPanel.add(button);
        }

        //Grid for options - JPanel holds these grids with buttons
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(2, 1));

        //Button of "new game"
        JButton newGameButton = new JButton(new ImageIcon(getClass().getResource(NEW_GAME)));
        newGameButton.setActionCommand("new");
        newGameButton.addActionListener(optionsClickListener);

        //Button of "close game"
        JButton closeGameButton = new JButton(new ImageIcon(getClass().getResource(CLOSE_GAME)));
        closeGameButton.setActionCommand("close");
        closeGameButton.addActionListener(optionsClickListener);

        optionsPanel.add(newGameButton);
        optionsPanel.add(closeGameButton);
        optionsPanel.setVisible(false);


        //Prepare the content panel of the frame.
        content = frame.getContentPane();
    }

}
