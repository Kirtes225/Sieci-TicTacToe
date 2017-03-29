package com.company;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class TicTacToe
{
    // Grid variables
    //    0 for an empty square
    //    1 if the square contains X
    //    2 if the square contains O
    // static int A1, A2, A3, B1, B2, B3, C1, C2, C3;

    static Scanner scanner = new Scanner(System.in);
    static TicTacToeBoard board = new TicTacToeBoard();

    public static void main(String[] args)
    {
        System.out.println("Tic Tac Toe 3x3 Console\n");
        System.out.println("Board Pattern");
        System.out.println(board.boardPattern());

        String prompt = "Please insert your first move: ";
        String humanMove = "";
        String computerMove = "";

        boolean gameIsWon = false;

        // There are a maximum of nine plays, so a for loop keeps track of
        // the number of plays. The game is over after the ninth play.
        // Each time through the loop, both the human and the computer play.
        // So i is incremented in the body of the loop after the computer plays.

        for (int i = 1; i <=9; i++)
        {
            // Human player

            humanMove = getMove(prompt);
            board.playAt(humanMove, 1);

            System.out.println();
            System.out.println(board.toString());
            System.out.println();

            if (board.isGameOver() == 1)
            {
                System.out.println("Human won!");
                gameIsWon = true;
                break;
            }

            // Computer player
            if (i < 9)
            {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                computerMove = board.getNextMove();
                System.out.println("Computer move: " + computerMove);
                board.playAt(computerMove, 2);

                System.out.println();
                System.out.println(board.toString());
                System.out.println();

                if (board.isGameOver() == 2)
                {
                    System.out.println("Computer won!");
                    gameIsWon = true;
                    break;
                }
                prompt = "Insert your move: ";
                i++;
            }
        }
        if (board.isGameOver() == 3)
            System.out.println("Nobody won");
    }

    public static String getMove(String prompt)
    {
        String play;
        System.out.print(prompt);
        do
        {
            play = scanner.nextLine();
            if (!board.isValidPlay(play))
            {
                System.out.println("Square doesn't exist.");
            }
        } while (!board.isValidPlay(play));
        return play;
    }

}