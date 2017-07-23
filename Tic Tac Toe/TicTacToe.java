import java.util.Random;

public class TicTacToe implements Cloneable {
    //Who made a move
    private final int NOBODY_TURN   = 0;
    private final int PLAYER_TURN   = 1;
    private final int COMPUTER_TURN = -1;

    //Marks on grid, which will being changed during a game
    private final char NOBODY_MARK   = ' ';
    private final char PLAYER_MARK   = 'X';
    private final char COMPUTER_MARK = 'O';

    // The difficulty of the game.  This value is used by bestGuess().
    private final int GAME_LEVEL = 8;

    private int whoseTurn = NOBODY_TURN;
    private int firstTurn = NOBODY_TURN;

    //Game's board as a grid
    private char[] grid = new char[9];

    //If move is available - contain 1, if not 0
    private int[] moves = new int[9];

    //How many moves were made during one game
    private int numMoves = 0;

    public TicTacToe() {
        // Fill out the board to be empty
        for(int i = 0; i < 9; i++) {
            grid[i] = NOBODY_MARK;
        }
        // All possible moves
        generateMoves();
    }

    public TicTacToe clone() throws CloneNotSupportedException {
        // Need to clone the arrays since they're treated as objects in Java.
        TicTacToe clone = (TicTacToe)super.clone();
        clone.grid = this.grid.clone();
        clone.moves = this.moves.clone();
        return clone;
    }

    //Who will get a first move in a game
    public void chooseFirstPlayer()
    {

        Random random = new Random();

        if(random.nextInt(2) == 0) { //50% of chance (0 or 1)
            setFirstTurn(PLAYER_TURN);
        }
        else {
            setFirstTurn(COMPUTER_TURN);
        }
    }

    //Showing actual situation in a game (in a console) and let to click and mark a move
    public String drawBoard() {
        String toReturn = "";

        for(char space : grid) {
            if(space == PLAYER_MARK) toReturn += "1";
            else if(space == COMPUTER_MARK) toReturn += "2";
            else toReturn += "-";
        }
        return toReturn + "\n";
    }


    //Checks, which squares are available for actual game
    public int[] generateMoves() {
        for(int i = 0; i < moves.length; i++) {
            if (grid[i] == NOBODY_MARK)
                moves[i] = 1; //Square is available
            else
                moves[i] = 0; //Square is unavailable
        }
        return moves;
    }

    //All possible remaining moves for actual game
    public int[] generateLegalMoves() {
        // Create the list of legal moves from the move set.
        int[] legalMoves = new int[0];
        for(int i = 0; i < moves.length; i++) {
            // If the move is available, add it to the legal moves array.
            if(moves[i] == 1) {
                // Increase size of the legal moves array.
                int[] tempLegalMoves = legalMoves;
                legalMoves = new int[tempLegalMoves.length + 1];
                // Rewrite all array to the new one (increased)
                for(int j = 0; j < tempLegalMoves.length; j++)
                    legalMoves[j] = tempLegalMoves[j];
                legalMoves[legalMoves.length - 1] = i;
            }
        }
        return legalMoves;
    }

    //Checks if a move isn't outside a board
    public boolean legalMove(int move) {
        if(move < 0 || move > 8)
            return false;
        return moves[move] == 1;
    }

    //Makes computer moves
    public void computerMove() throws CloneNotSupportedException {
        int computerMove = bestMove();
        placePiece(COMPUTER_TURN, computerMove);
    }

    //Determines the best move in a current game (returns the best possible move)
    public int bestMove() throws CloneNotSupportedException
    {
        int bestGuessValue, currentGuessValue;

        //Auxiliary variable to stores the best move found (so far)
        int best;

        //Next move to try
        int tryMove;

        //Copy of the current game situation
        TicTacToe tempSituation;

        //There will be stored the best and legal moves
        int[] legalMoves;

        //Copy the current game situation
        tempSituation = this.clone();

        //Makes list of legal moves for computer
        legalMoves = tempSituation.generateLegalMoves();

        //The first legal move for computer
        tryMove = legalMoves[0];

        //Whose turn
        tempSituation.placePiece(COMPUTER_TURN, tryMove);

        // Determine the chance of the computer winning by making this move.
        bestGuessValue = tempSituation.bestGuess(GAME_LEVEL);

        // The best move the computer can make is the first legal move (now)
        best = tryMove;

        //Checks other possible moves and determine (with bestGuess) which is the best
        int currentMoveIndex = 1;
        while(currentMoveIndex < legalMoves.length)
        {
            tempSituation = this.clone();
            tryMove = legalMoves[currentMoveIndex];

            tempSituation.placePiece(COMPUTER_TURN, tryMove);

            // Determine the chance of the computer winning by making this move.
            currentGuessValue = tempSituation.bestGuess(GAME_LEVEL);

            /* Choose the move that gives the computer the greatest chance of winning.
               Do so by taking the move with the highest guess value (closest to winning 100) if computer turn.
               Otherwise, "block" the move that would allow the player to win (closest to 0).*/
            if((tempSituation.getWhoseTurn() == COMPUTER_TURN && currentGuessValue > bestGuessValue) ||
                    tempSituation.getWhoseTurn() != COMPUTER_TURN && currentGuessValue < bestGuessValue) {
                bestGuessValue = currentGuessValue;
                best = tryMove;
            }
            currentMoveIndex++;
        }
        return best;
    }



     /*Returns an integer value describing how likely the computer is to win based on the invoking
     game's board state.
      100 - computer has won
      50  - tied game
      0   - computer has lost
     Level describes how much further the game will play in order to determine the best possible move.
     Values for level range from 0-8, with 8 being technically unbeatable.*/

    public int bestGuess(int level) throws CloneNotSupportedException {
        int bestGuessValue, currentGuessValue;
        int tryMove;
        TicTacToe tempSituation;
        int[] legalMoves = this.generateLegalMoves();

        //If we're at the base level or the game is over, return how well the computer has done.
        if((level == 0 || this.isOver()))
            return judge();

        //Copy the current game.
        tempSituation = this.clone();

        // If the level is even - the computer's move
        if(level % 2 == 0)
            tempSituation.setWhoseTurn(COMPUTER_TURN);

        //If the level is unpaired - the player's move
        else
            tempSituation.setWhoseTurn(PLAYER_TURN);

        //Place the first legal move and determine how good this move is for the computer.
        tryMove = legalMoves[0];
        tempSituation.placePiece(tempSituation.getWhoseTurn(), tryMove);
        bestGuessValue = tempSituation.bestGuess(level - 1);

        //Checks other possible moves and determine (with bestGuess) which is the best
        int currentMoveIndex = 1;
        while(currentMoveIndex < legalMoves.length) {
            tempSituation = this.clone();
            tryMove = legalMoves[currentMoveIndex];
            tempSituation.placePiece(tempSituation.getWhoseTurn(), tryMove);
            currentGuessValue = tempSituation.bestGuess(level - 1);

            //Computer will try to prevent a player's win
            if(tempSituation.getWhoseTurn() == PLAYER_TURN)
                bestGuessValue = Math.max(bestGuessValue, currentGuessValue);

            //The best possible move for computer
            else
                bestGuessValue = Math.min(bestGuessValue, currentGuessValue);
            currentMoveIndex++;
        }
        return bestGuessValue;
    }

    //Update the game
    public void placePiece(int player, int move) {
        grid[move] = (player == PLAYER_TURN) ? PLAYER_MARK : COMPUTER_MARK;
        numMoves++;

        // Generate the new set of moves taken/not taken in the game.
        generateMoves();
    }


     // 0 - game hasn't finished yet
     // 1 - player won the game
     // 2 - computer won the game
     // 3 - game is a draw
    public int result()
    {
        // First checking the columns
        for(int i = 0; i < 3; i++)
        {
            //Checks if a player won
            if(grid[i] == PLAYER_MARK && grid[i + 3] == PLAYER_MARK && grid[i + 6] == PLAYER_MARK)
                return 1;

            //Checks if the computer won
            else if(grid[i] == COMPUTER_MARK && grid[i + 3] == COMPUTER_MARK && grid[i + 6] == COMPUTER_MARK)
                return 2;
        }

        // Checking the rows
        for(int i = 0; i <= 6; i += 3)
        {
            //Checks if a player won
            if(grid[i] == PLAYER_MARK && grid[i + 1] == PLAYER_MARK && grid[i + 2] == PLAYER_MARK)
                return 1;

            //Checks if the computer won
            if(grid[i] == COMPUTER_MARK && grid[i + 1] == COMPUTER_MARK && grid[i + 2] == COMPUTER_MARK)
                return 2;
        }

        // Checking diagonals.
        if(grid[0] == PLAYER_MARK && grid[4] == PLAYER_MARK && grid[8] == PLAYER_MARK)
            return 1;
        else if(grid[2] == PLAYER_MARK && grid[4] == PLAYER_MARK && grid[6] == PLAYER_MARK)
            return 1;

        if(grid[0] == COMPUTER_MARK && grid[4] == COMPUTER_MARK && grid[8] == COMPUTER_MARK)
            return 2;
        else if(grid[2] == COMPUTER_MARK && grid[4] == COMPUTER_MARK && grid[6] == COMPUTER_MARK)
            return 2;

        //If there were made 9 moves - the game is a draw
        if(numMoves == 9)
            return 3;
        //The game hasn't finished yet
        else
            return 0;
    }

     // 0 - a player won the game
     // 50 - there isn't a winner
     // 100 - the computer won the game
    public int judge() {
        switch(result()) {
            case 0:
                return 50;
            case 1:
                return 0;
            case 2:
                return 100;
            case 3:
                return 50;
        }

        //It will never be done
        return 50;
    }

    // True - game is finished
    // False - game hasn't finished yet
    public boolean isOver() {
        return (result() != 0);
    }

    //Whose a turn
    public int getWhoseTurn() {
        return whoseTurn;
    }
    public void setWhoseTurn(int whoseTurn) {
        this.whoseTurn = whoseTurn;
    }

    //Whose the first move
    public int getFirstTurn() {
        return firstTurn;
    }
    public void setFirstTurn(int firstTurn) {
        this.firstTurn = firstTurn;
    }


    //Returns an array of legal moves
    public int[] getMoves() {
        return moves;
    }

    //Player has the first move of the game
    public int getPLAYER_TURN() {
        return PLAYER_TURN;
    }
    //Computer has the fist move of the game
    public int getCOMPUTER_TURN() {
        return COMPUTER_TURN;
    }
}
