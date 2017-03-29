package com.company;

public class TicTacToeBoard
{
    // 0 for an empty square
    // 1 if the square contains X
    // 2 if the square contains O
    static int A1, A2, A3, B1, B2, B3, C1, C2, C3;

    public TicTacToeBoard()
    {
        this.reset();
    }

    public void reset()
    {
        A1 = 0;
        A2 = 0;
        A3 = 0;
        B1 = 0;
        B2 = 0;
        B3 = 0;
        C1 = 0;
        C2 = 0;
        C3 = 0;
    }

    /*public int getSquare(String square)
    {
        String newSquare = square.toUpperCase();

        switch (newSquare)
        {
            case "A1":
                return A1;
            case "A2":
                return A2;
            case "A3":
                return A3;
            case "B1":
                return B1;
            case "B2":
                return B2;
            case "B3":
                return B3;
            case "C1":
                return C1;
            case "C2":
                return C2;
            case "C3":
                return C3;
        }

        throw new IllegalArgumentException();
    }*/

    public static boolean isValidPlay(String play)
    {
        if (play.equalsIgnoreCase("A1") & A1 == 0)
            return true;
        if (play.equalsIgnoreCase("A2") & A2 == 0)
            return true;
        if (play.equalsIgnoreCase("A3") & A3 == 0)
            return true;
        if (play.equalsIgnoreCase("B1") & B1 == 0)
            return true;
        if (play.equalsIgnoreCase("B2") & B2 == 0)
            return true;
        if (play.equalsIgnoreCase("B3") & B3 == 0)
            return true;
        if (play.equalsIgnoreCase("C1") & C1 == 0)
            return true;
        if (play.equalsIgnoreCase("C2") & C2 == 0)
            return true;
        if (play.equalsIgnoreCase("C3") & C3 == 0)
            return true;
        return false;
    }

    public void playAt(String square, int player)
    {
        String newSquare = square.toUpperCase();

        switch (newSquare)
        {
            case "A1":
                if (A1 != 0)
                    throw new IllegalArgumentException("Square is occupied");
                A1 = player;
                break;
            case "A2":
                if (A2 != 0)
                    throw new IllegalArgumentException("Square is occupied");
                A2 = player;
                break;
            case "A3":
                if (A3 != 0)
                    throw new IllegalArgumentException("Square is occupied");
                A3 = player;
                break;
            case "B1":
                if (B1 != 0)
                    throw new IllegalArgumentException("Square is occupied");
                B1 = player;
                break;
            case "B2":
                if (B2 != 0)
                    throw new IllegalArgumentException("Square is occupied");
                B2 = player;
                break;
            case "B3":
                if (B3 != 0)
                    throw new IllegalArgumentException("Square is occupied");
                B3 = player;
                break;
            case "C1":
                if (C1 != 0)
                    throw new IllegalArgumentException("Square is occupied");
                C1 = player;
                break;
            case "C2":
                if (C2 != 0)
                    throw new IllegalArgumentException("Square is occupied");
                C2 = player;
                break;
            case "C3":
                if (C3 != 0)
                    throw new IllegalArgumentException("Square is occupied");
                C3 = player;
                break;
            default:
                throw new IllegalArgumentException("Square doesn't exist.");
        }
    }


    public static int isGameOver()
    {
        if (isRowWon(A1, A2, A3))
            return A1;
        if (isRowWon(B1, B2, B3))
            return B1;
        if (isRowWon(C1, C2, C3))
            return C1;
        if (isRowWon(A1, B1, C1))
            return A1;
        if (isRowWon(A2, B2, C2))
            return A2;
        if (isRowWon(A3, B3, C3))
            return A3;
        if (isRowWon(A1, B2, C3))
            return A1;
        if (isRowWon(A3, B2, C1))
            return A3;

        // Here's a slick trick: Multiply all nine squares together.
        // The result will be 0 if at least one square is empty.

        if (A1 * A2 * A3 * B1 * B2 * B3 * C1 * C2 * C3 == 0)
            return 0;			// Game is not over

        return 3;				// Game is a draw
    }

    private static boolean isRowWon(int a, int b, int c)
    {
        return ((a == b) & (a == c) & (a != 0));
    }


    public String toString()
    {
        String line = "";
        line += " " + getXO(A1) + " | " + getXO(A2) + " | " + getXO(A3) + "\n";
        line +="-----------\n";
        line += " " + getXO(B1) + " | " + getXO(B2) + " | " + getXO(B3) + "\n";
        line +="-----------\n";
        line += " " + getXO(C1) + " | " + getXO(C2) + " | " + getXO(C3) + "\n";
        return line;
    }

    public String boardPattern()
    {
        String line = "";
        line += " " + "A1" + " | " + "A2" + " | " + "A3" + "\n";
        line +="-----------\n";
        line += " " + "B1" + " | " + "B2" + " | " + "B3" + "\n";
        line +="-----------\n";
        line += " " + "C1" + " | " + "C2" + " | " + "C3" + "\n";
        return line;
    }

    private String getXO(int square)
    {
        if (square == 1)
            return "X";
        if (square == 2)
            return "O";
        return " ";
    }

    public static String getNextMove()
    {
        if (B2 == 0)
            return "B2";
        if (A1 == 0)
            return "A1";
        if (A3 == 0)
            return "A3";
        if (C1 == 0)
            return "C1";
        if (C3 == 0)
            return "C3";
        if (A2 == 0)
            return  "A2";
        if (B1 == 0)
            return  "B1";
        if (B3 == 0)
            return  "B3";
        if (C2 == 0)
            return  "C2";
        return "";
    }

}

