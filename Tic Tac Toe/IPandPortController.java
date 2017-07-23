import MULTIPLAYER.TicTacToeClient;
import MULTIPLAYER.TicTacToeServer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;


public class IPandPortController implements Initializable{

    @FXML
    private TextField serverPortTextField;

    @FXML
    private TextField clientIPTextField;

    @FXML
    private TextField clientPortTextField;

    @FXML
    private Button createServerButton;

    @FXML
    private Button connectServerButton;

    @FXML
    private Label serverInfo;

    @FXML
    private Label errorInfo;

    @FXML
    private Label IPLabel;


    public synchronized void createServer() throws UnknownHostException {
        String regex = "[0-9]+";
        int port;

        if(!serverPortTextField.getText().matches(regex)) {
            errorInfo.setText("Port is a number!");
        }
        else{
            port = Integer.parseInt(serverPortTextField.getText());

            if (port < 1 || port > 65535 || Character.isLetter(serverPortTextField.getText().charAt(0))) {
                errorInfo.setText("Wrong port number! Server couldn't have been created.");
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TicTacToeServer ticTacToeServer = new TicTacToeServer(port);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                serverInfo.setText("Server created! Port: " + port);
                errorInfo.setText("");
            }
        }
    }

    public synchronized void connectServer() {
        TicTacToeClient ticTacToeClient = new TicTacToeClient();

        String IPAdress;
        int port;
        IPAdress = clientIPTextField.getText();
        port = Integer.parseInt(clientPortTextField.getText());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ticTacToeClient.runClient(IPAdress, port);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            InetAddress IP= InetAddress.getLocalHost();
            IPLabel.setText("Your IPv4 Address: " + IP.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
