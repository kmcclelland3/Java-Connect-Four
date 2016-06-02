/**
 * This file contains the logic for playing a connect four game
 * either against another player or a random agent, depending on
 * the given command line args.
 *
 * I worked on the homework assignment alone, only using course materials.
 *
 * @author Kristen McClelland (GTID: 902944956)
 * @author Julia Ting
 */
import java.util.Random;
import java.util.Scanner;

public class ConnectFour {

    /*
     * Static variables to use throughout printBoard()
     * and the main method. You MUST use "X" and "O",
     * so you might as well use TOKEN1 and TOKEN2 variables!
     */
    private static final int GAME_WIDTH = 7;
    private static final int GAME_HEIGHT = 6;
    private static final String TOKEN1 = "X"; // player 1
    private static final String TOKEN2 = "O"; // player 2

    private static Scanner scan = new Scanner(System.in);
    private static Random gen = new Random();

    private static String[][] board = new String[GAME_HEIGHT][GAME_WIDTH];

    /**
     * This enumeration is used as outcomes for findWinner().
     *
     * Read the pdf for a complete description. Think of how
     * GameStatus.QUIT might help you in your game functionality!
     */
    private enum GameStatus {
        ONE, TWO, TIE, ONGOING, QUIT
    }

    public static void main(String[] args) {
        boolean singlePlayer = true;
        boolean playerOnesTurn = true;
        int result;
        int col = 0;
        int row = 0;
        // determine whether 1 player or 2 player match
        if (args.length == 0) {
            System.out.println("Would you like to play single player or two "
                + "player mode? (Enter 1 or 2)");
            result = scan.nextInt();
            if (result == 2) {
                singlePlayer = false;
            }
        } else {
            if (args[0].equals("2")) {
                singlePlayer = false;
            }
        }
        String input = "y";
        if (singlePlayer) {
        // single player match
            System.out.println("Welcome to single player mode of "
                + "Connect Four!");
            System.out.println("Choose where to go by entering the slot "
                + "number.");
            System.out.println("Type 'q' if you would like to quit.");
            System.out.println();
            printBoard();
            while ((!input.equals("q"))
                && (findWinner() == GameStatus.ONGOING)) {
                System.out.println("Your turn! Where would you like to go?");
                if (scan.hasNextInt()) {
                    col = scan.nextInt();
                } else {
                    input = scan.next();
                }
                // loops until a valid column location is chosen
                while ((!input.equals("q")) && !validColumn(col - 1)) {
                    System.out.println("That column is full. Please pick "
                        + "another!");
                    System.out.println("Where would you like to go?");
                    if (scan.hasNextInt()) {
                        col = scan.nextInt();
                    } else {
                        input = scan.next();
                    }
                }
                if (!input.equals("q")) {
                    col--;
                    row = markerLocation(col);
                    board[row][col] = TOKEN1;
                }
                printBoard();
                if (findWinner() == GameStatus.ONGOING) {
                    System.out.println("It's the AI's turn.");
                    col = generatedAIMove();
                    row = markerLocation(col);
                    board[row][col] = TOKEN2;
                    printBoard();
                }
            }
        } else {
        // two player match
            System.out.println("Welcome to 2-player mode of Connect Four!");
            System.out.println("Choose where to go by entering the slot "
                + "number.");
            System.out.println("Type 'q' if you would like to quit.");
            System.out.println();
            printBoard();
            // dont change/check input until a non int is entered with
            // the scanner
            while ((!input.equals("q"))
                && (findWinner() == GameStatus.ONGOING)) {
                if (playerOnesTurn) {
                    System.out.println("Player 1, where would you like to go?");
                } else {
                    System.out.println("Player 2, where would you like to go?");
                }
                if (scan.hasNextInt()) {
                    col = scan.nextInt();
                } else {
                    input = scan.next();
                }
                // loops until a valid column location is chosen
                while ((!input.equals("q")) && !validColumn(col - 1)) {
                    System.out.println("That column is full. Please pick "
                        + "another!");
                    if (playerOnesTurn) {
                        System.out.println("Player 1, where would you like "
                            + "to go?");
                    } else {
                        System.out.println("Player 2, where would you like "
                            + "to go?");
                    }
                    if (scan.hasNextInt()) {
                        col = scan.nextInt();
                    } else {
                        input = scan.next();
                    }
                }
                if (!input.equals("q")) {
                    col--;
                    row = markerLocation(col);
                    if (playerOnesTurn) {
                        board[row][col] = TOKEN1;
                    } else {
                        board[row][col] = TOKEN2;
                    }
                }
                playerOnesTurn = !playerOnesTurn;
                // print directly before exiting loop
                printBoard();
            }
        }
        // final statement to execute
        if (singlePlayer) {
            switch (findWinner()) {
            case ONE:
                System.out.println("Woohoo! You won!");
                break;
            case TWO:
                System.out.println("Woohoo! The AI won!");
                break;
            case TIE:
                System.out.println("There was a tie!");
                break;
            default:
                System.out.println("Goodbye!");
                break;
            }
        } else {
            switch (findWinner()) {
            case ONE:
                System.out.println("Woohoo! Player 1 won!");
                break;
            case TWO:
                System.out.println("Woohoo! Player 2 won!");
                break;
            case TIE:
                System.out.println("There was a tie!");
                break;
            default:
                System.out.println("Goodbye!");
                break;
            }
        }
    }

    /**
    * Use this method to generate a move for the AI to play
    * Keep generating moves until a playable column is chosen
    * Should already check before calling this method that the game is
    * not yet over
    * Still have to determine what row the marker should be placed in
    **/
    private static int generatedAIMove() {
        int loc = gen.nextInt(GAME_WIDTH);
        while (!validColumn(loc)) {
            loc = gen.nextInt(GAME_WIDTH);
        }
        return loc;
    }

    /**
    * Returns whether or not the player's selected column is valid
    * AKA not completely full
    **/
    private static boolean validColumn(int col) {
        boolean status = false;
        for (int r = 0; r < GAME_HEIGHT; r++) {
            if (board[r][col] == null) {
                status = true;
            }
        }
        return status;
    }

    /**
    * Determines what the row of the location of the marker should be
    * Returns the first null location in that col starting from the bottom up
    * Should be run after checking that validColumn() returns true
    * This prevents an error of potentially not returning an int
    **/
    private static int markerLocation(int col) {
        int loc = 0;
        for (int r = GAME_HEIGHT - 1; r >= 0; r--) {
            if (board[r][col] == null) {
                loc = r;
                break;
            }
        }
        return loc;
    }

    /**
     * Prints the current state of the board array.
     *
     * @return void
     **/
    private static void printBoard() {
        String elem;
        for (int r = 0; r < GAME_HEIGHT; r++) {
            for (int c = 0; c < GAME_WIDTH; c++) {
                if (board[r][c] == null) {
                    elem = " ";
                } else {
                    elem = board[r][c];
                }
                System.out.print("| " + elem + " ");
            }
            System.out.println("|");
        }
        System.out.println("-----------------------------");
        System.out.print("  ");
        for (int c = 1; c < GAME_WIDTH; c++) {
            System.out.print(c + "   ");
        }
        System.out.println(GAME_WIDTH + "  ");
    }

    /**
     * Determines what the current result of the game is
     * given the current state of the board.
     *
     * @return GameStatus enumeration value that determines
     * if player one has won, player two has won, a tie exists,
     * or there is no result yet.
     */
    private static GameStatus findWinner() {
        if (isColumnVictory(TOKEN1) || isRowVictory(TOKEN1)
                || isTopLeftDiagonalVictory(TOKEN1)
                || isTopRightDiagonalVictory(TOKEN1)) {
            return GameStatus.ONE;
        } else if (isColumnVictory(TOKEN2) || isRowVictory(TOKEN2)
                || isTopLeftDiagonalVictory(TOKEN2)
                || isTopRightDiagonalVictory(TOKEN2)) {
            return GameStatus.TWO;
        } else if (isBoardFull()) {
            return GameStatus.TIE;
        } else {
            return GameStatus.ONGOING;
        }
    }

    /*
     * ~~~~~~YOU SHOULD NOT BE CALLING METHODS BELOW THIS POINT~~~~~
     */

    /**
     * Determines whether or not the board has any moves
     * remaining.
     *
     * @return true or false
     */
    private static boolean isBoardFull() {
        for (int col = 0; col < GAME_WIDTH; col++) {
            for (int row = 0; row < GAME_HEIGHT; row++) {
                if (board[row][col] == null || board[row][col].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines whether or not a player has won via 4 in a row
     * in columns.
     *
     * @return true or false
     */
    private static boolean isColumnVictory(String token) {
        for (int col = 0; col < GAME_WIDTH; col++) {
            int count = 0;
            for (int row = 0; row < GAME_HEIGHT; row++) {
                if (board[row][col] != null) {
                    if (board[row][col].equals(token)) {
                        count++;
                    } else {
                        count = 0;
                    }
                } else {
                    count = 0;
                }
                if (count == 4) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines whether or not a player has won via 4 in a row
     * in rows.
     *
     * @return true or false
     */
    private static boolean isRowVictory(String token) {
        for (int row = 0; row < GAME_HEIGHT; row++) {
            int count = 0;
            for (int col = 0; col < GAME_WIDTH; col++) {
                if (board[row][col] != null) {
                    if (board[row][col].equals(token)) {
                        count++;
                    } else {
                        count = 0;
                    }
                } else {
                    count = 0;
                }
                if (count == 4) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines whether or not a player has won via 4 in a row
     * in top to left diagonals.
     *
     * @return true or false
     */
    private static boolean isTopLeftDiagonalVictory(String token) {
        for (int row = 0; row < GAME_HEIGHT; row++) {
            for (int col = 0; col < GAME_WIDTH; col++) {
                int count = 0;
                for (int delta = 0; delta < 5; delta++) {
                    if (withinBounds(row + delta, col + delta)
                            && board[row + delta][col + delta] != null) {
                        if (board[row + delta][col + delta].equals(token)) {
                            count++;
                        } else {
                            count = 0;
                        }
                    } else {
                        count = 0;
                    }
                    if (count == 4) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines whether or not a player has won via 4 in a row
     * in top to right diagonals.
     *
     * @return true or false
     */
    private static boolean isTopRightDiagonalVictory(String token) {
        for (int row = 0; row < GAME_HEIGHT; row++) {
            for (int col = GAME_WIDTH - 1; col >= 0; col--) {
                int count = 0;
                for (int delta = 0; delta < 5; delta++) {
                    if (withinBounds(row + delta, col - delta)
                            && board[row + delta][col - delta] != null) {
                        if (board[row + delta][col - delta].equals(token)) {
                            count++;
                        } else {
                            count = 0;
                        }
                    } else {
                        count = 0;
                    }
                    if (count == 4) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Small bounds checker helper method.
     */
    private static boolean withinBounds(int row, int col) {
        return (row < GAME_HEIGHT && row >= 0)
            && (col < GAME_WIDTH && col >= 0);
    }
}
