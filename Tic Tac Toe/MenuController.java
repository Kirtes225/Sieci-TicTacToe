import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


public class MenuController extends Application {

    @FXML
    private Button singleplayer;

    @FXML
    private Button statistics;

    public static void main(String[] args) {
        Sound.sound.loop();
        launch(args);
    }

    public void singleplayerButton() throws IOException, InterruptedException {
        TicTacToeGUI ticTacToe = new TicTacToeGUI();
        ticTacToe.run();
        singleplayer.setDisable(true);
    }

    public void multiplayerButton() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("IPandPort.fxml"));
        Parent root = null;
        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Set IP and port");
        stage.setResizable(false);

        stage.show();
    }

    public synchronized void statisticsButton() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int winCount, drawCount, lossCount;
                String title = null, text = null;
                File file = new File("stats.txt");
                if (file.isFile() && !file.isDirectory()) {

                    //File file = new File("stats.txt");

                    Scanner readStats = null;
                    try {
                        readStats = new Scanner(file);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                    String stats = readStats.nextLine();

                    //Statistics are divided by ";", so it has to be split
                    String[] divideStats = stats.split(";");
                    winCount = Integer.parseInt(divideStats[0]);
                    drawCount = Integer.parseInt(divideStats[1]);
                    lossCount = Integer.parseInt(divideStats[2]);
                } else {
                    winCount = 0;
                    drawCount = 0;
                    lossCount = 0;
                }

                title = "Your stats";
                text = "\nStatistics:" +
                        "\n• wins: " + winCount +
                        "\n• draws: " + drawCount +
                        "\n• losses: " + lossCount;

                JOptionPane.showMessageDialog(null, text, title, JOptionPane.INFORMATION_MESSAGE);
            }
        }).start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Białek Tomasz - Tic Tac Toe");
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });

    }
}
