import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerGame
{
    private TicTacToe game;
    private Scanner scanner;
    private BufferedReader input;
    private DataOutputStream output;

    ServerGame(BufferedReader input, DataOutputStream output) {
        this.input = input;
        this.output = output;
    }

    public void start() throws InputMismatchException, CloneNotSupportedException, IOException {
        scanner = new Scanner(input);

        game = new TicTacToe();
        game.chooseFirstPlayer();


        while(!game.isOver()) {
            if(game.getFirstTurn() == game.getCOMPUTER_TURN()) {
                doComputerTurn();

                if(!game.isOver()) {
                    doPlayerTurn();
                }
            }

            else {
                doPlayerTurn();

                if(!game.isOver()) {
                    doComputerTurn();
                }
            }
        }

        switch(game.result()) {
            case 1:
                System.out.println("The player has won the game.");
                output.writeBytes("#P\n");
                break;
            case 2:
                System.out.println("The opponent has won the game.");
                output.writeBytes("#O\n");
                break;
            case 3:
                System.out.println("The game is a draw.");
                output.writeBytes("#T\n");
                break;
        }
        System.out.println("Determining if user would like to play another game...");

        String decision = "";

        while(!decision.equals("#NG") && !decision.equals("#CG")) {
            decision = input.readLine();
        }

        if(decision.equals("#NG")) {
            System.out.println("The user would like to play another game.");
            this.start();
        }

        else {
            System.out.println("The user would NOT like to play another game.");
            input.close();
            output.close();
        }
    }

    public void doPlayerTurn() throws IOException {
        game.setWhoseTurn(game.getPLAYER_TURN());

        int userMove = -1;

        while(!game.legalMove(userMove)) {
           output.writeBytes(game.drawBoard());

            try {
                userMove = scanner.nextInt();
            }

            catch(InputMismatchException e) {
                userMove = -1;
                scanner = new Scanner(input);
            }

            catch(NoSuchElementException e) {
                System.err.println("The game was terminated by the user.");
            }
        }

        game.placePiece(game.getPLAYER_TURN(), userMove);
    }

    public void doComputerTurn() throws CloneNotSupportedException {
        game.setWhoseTurn(game.getCOMPUTER_TURN());
        game.computerMove();
    }

}
