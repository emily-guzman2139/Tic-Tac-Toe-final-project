package application.model;

/**
 * Model class representing the game logic for Tic Tac Toe.
 */
public class TicTacToeModel {
    // Game board represented as a 2D array of strings
    private final String[][] board = new String[3][3];

    // Flags and player information
    // Flags and player information
    private boolean isPlayerXTurn; // Control of turn, defaults to X's turn
    private boolean vsComputer; // Whether the game is against a computer
    private String player1Name; // First player's name
    private String player2Name; // Second player's name
    private String currentPlayerX; // Current player playing as 'X'
    private int player1Score; // First player's score
    private int player2Score; // Second player's score
    private boolean isRoundOver; // Flag to indicate if the round has ended

    /**
     * Constructor initializes the game board.
     */
    public TicTacToeModel() {
        clearBoard();
    }

    /**
     * Sets default settings for a new game.
     */
    public void setDefaultSettings() {
        this.player1Name = "Player1";
        this.player2Name = "Computer";
        this.currentPlayerX = player1Name; // Player1 starts as X by default
        this.vsComputer = true; // Defaults playing against the computer
        this.isRoundOver = false;
        resetScore();
    }

    /**
     * Clears the game board by setting all spots to an empty string.
     */
    public void clearBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = "";
            }
        }
    }

    /**
     * Processes a move made by a player.
     *
     * @param row The row index of the move.
     * @param col The column index of the move.
     * @return true if the move was successful, false otherwise.
     */
    public boolean setMove(int row, int col) {
        if (board[row][col].isEmpty()) {
            board[row][col] = isPlayerXTurn ? "X" : "O";
            return true;
        }
        return false;
    }

    /**
     * Checks for a winner on the game board.
     *
     * @return A string indicating the winner and the winning line, or an empty string if there's no winner yet.
     */
    public String checkWinner() {
        // Check for a winning line in rows and columns
        for (int i = 0; i < 3; i++) {
            if (!board[i][0].isEmpty() && board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2])) {
                return board[i][0] + "H" + i;
            }
            if (!board[0][i].isEmpty() && board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i])) {
                return board[0][i] + "V" + i;
            }
        }
        // Check diagonals
        if (!board[0][0].isEmpty() && board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2])) {
            return board[0][0] + "D0";
        }
        if (!board[0][2].isEmpty() && board[0][2].equals(board[1][1]) && board[0][2].equals(board[2][0])) {
            return board[0][2] + "D1";
        }
        return "";
    }

    /**
     * Checks if the board is completely filled.
     *
     * @return true if the board is full, false otherwise.
     */
    public boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Switches the current turn between Players.
     */
    public void switchTurn() {
        this.isPlayerXTurn = !this.isPlayerXTurn;
    }

    /**
     * Switches the starting player for the next round.
     */
    public void switchStartingPlayer() {
        currentPlayerX = currentPlayerX.equals(player1Name) ? player2Name : player1Name;
    }

    /**
     * Retrieves the name of the player who won the round.
     *
     * @param winner The symbol of the winner ('X' or 'O').
     * @return The name of the winning player.
     */
    public String getWinnerName(char winner) {
        return (winner == 'X') ? currentPlayerX : (currentPlayerX.equals(player1Name) ? player2Name : player1Name);
    }

    /**
     * Updates the score for the winning player.
     *
     * @param winner The symbol of the winner ('X' or 'O').
     */
    public void updateScore(char winner) {
        if (winner == 'X') {
            if (getWinnerName(winner).equals(player1Name)) {
                player1Score++;
            } else {
                player2Score++;
            }
        } else {
            if (getWinnerName(winner).equals(player1Name)) {
                player1Score++;
            } else {
                player2Score++;
            }
        }
    }

    /**
     * Gets the name of the player who is not currently the player X.
     *
     * @return The name of the other player.
     */
    public String getOtherPlayerName() {
        return currentPlayerX.equals(player1Name) ? player2Name : player1Name;
    }

    /**
     * Resets the score for both players to zero.
     */
    public void resetScore() {
        player1Score = player2Score = 0;
    }

    /**
     * Computes the computer's next move with a preference for winning moves or blocking opponent's winning moves.
     *
     * @return An array with two integers representing the row and column of the computer's chosen move.
     */
    public int[] computeComputerMove() {
        // Check if there's a winning move for the computer ("O")
        int[] winMove = findWinningMove("O"); // Find a winning move for the computer
        if (winMove != null) return winMove; // If found, return the winning move

        // If no winning move, check if there's a move to block the opponent ("X")
        int[] blockMove = findWinningMove("X"); // Find a move to block the opponent
        if (blockMove != null) return blockMove; // If found, return the blocking move

        // If neither a winning nor a blocking move, make a random move
        int row, col;
        do {
            row = (int) (Math.random() * 3); // Generate a random row index
            col = (int) (Math.random() * 3); // Generate a random column index
        } while (!board[row][col].isEmpty()); // Keep generating random positions until an empty spot is found
        return new int[]{row, col}; // Return the randomly selected position
    }

    /**
     * Searches for a winning or blocking move.
     *
     * @param playerSymbol The player symbol to check for the winning or blocking move.
     * @return An array with the winning/blocking move coordinates, or null if none found.
     */
    private int[] findWinningMove(String playerSymbol) {
        // Iterate through the board to find an empty spot
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].isEmpty()) { // If the spot is empty
                    board[i][j] = playerSymbol; // Try placing the player's symbol
                    if (isWin(playerSymbol)) { // If placing the symbol results in a win
                        board[i][j] = ""; // Reset the spot
                        return new int[]{i, j}; // Return the winning move
                    }
                    board[i][j] = ""; // Reset the spot regardless of win or not
                }
            }
        }
        return null; // If no winning move found, return null
    }


    /**
     * Checks if the given player symbol has a winning configuration on the board.
     *
     * @param playerSymbol The player symbol to check for a win.
     * @return true if the player has won, false otherwise.
     */
    private boolean isWin(String playerSymbol) {
        // проверка строк, столбцов и диагоналей на выигрыш
        for (int i = 0; i < 3; i++) {
            if (playerSymbol.equals(board[i][0]) && playerSymbol.equals(board[i][1]) && playerSymbol.equals(board[i][2]) || playerSymbol.equals(board[0][i]) && playerSymbol.equals(board[1][i]) && playerSymbol.equals(board[2][i])) {
                return true;
            }
        }
        return playerSymbol.equals(board[0][0]) && playerSymbol.equals(board[1][1]) && playerSymbol.equals(board[2][2]) || playerSymbol.equals(board[0][2]) && playerSymbol.equals(board[1][1]) && playerSymbol.equals(board[2][0]);
    }

    /**
     * Checks the status of the game, updates round over status based on the board state.
     */
    public void checkGameStatus() {
        String winnerInfo = checkWinner();
        if (!winnerInfo.isEmpty()) {
            setRoundOver(true);
        } else setRoundOver(isBoardFull());
    }

    // Getters and Setters

    public String[][] getBoard() {
        return board;
    }

    public boolean isPlayerXTurn() {
        return isPlayerXTurn;
    }

    public void setPlayerXTurn(boolean isPlayerXTurn) {
        this.isPlayerXTurn = isPlayerXTurn;
    }

    public boolean isVsComputer() {
        return vsComputer;
    }

    public void setVsComputer(boolean vsComputer) {
        this.vsComputer = vsComputer;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    public String getCurrentPlayerX() {
        return currentPlayerX;
    }

    public void setCurrentPlayerX(String currentPlayerX) {
        this.currentPlayerX = currentPlayerX;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public void setPlayer1Score(int player1Score) {
        this.player1Score = player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public void setPlayer2Score(int player2Score) {
        this.player2Score = player2Score;
    }

    public boolean isRoundOver() {
        return isRoundOver;
    }

    public void setRoundOver(boolean isRoundOver) {
        this.isRoundOver = isRoundOver;
    }

    public boolean isComputerX() {
        return currentPlayerX.equals(player2Name);
    }

}

