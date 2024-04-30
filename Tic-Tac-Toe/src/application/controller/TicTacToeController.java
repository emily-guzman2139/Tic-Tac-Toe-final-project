package application.controller;


import application.model.TicTacToeModel;
import application.view.TicTacToeView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Controller for the Tic Tac Toe game. Manages game logic and interactions.
 */
public class TicTacToeController {
    private final TicTacToeModel model = new TicTacToeModel();
    private TicTacToeView mainApp;
    private static final String IMAGES_PATH = "/img/";
    @FXML
    private GridPane gameBoard;
    @FXML
    private Label turnLabel;
    @FXML
    private Label scoreLabel;

    /**
     * Initializes the game with provided player settings.
     * @param player1 Name of player one.
     * @param player2 Name of player two or "Computer" if playing against the AI.
     * @param playerXname Name of the player who will start as 'X'.
     * @param vsComputer Boolean indicating if the game is against the computer.
     */
    public void initializeGame(String player1, String player2, String playerXname, boolean vsComputer) {
        model.setPlayer1Name(player1);
        model.setPlayer2Name(player2);
        model.setVsComputer(vsComputer);
        model.setCurrentPlayerX(playerXname);
        model.resetScore();
        resetRound();
    }

    /**
     * Resets the game board to start a new round.
     */
    private void resetRound() {
        model.clearBoard();
        for (Node node : gameBoard.getChildren()) {
            if (node instanceof ImageView) {
                ((ImageView) node).setImage(null);
            }
            if (node instanceof Button) {
                ((Button) node).setGraphic(null);
                node.setDisable(false);
            }
        }
        model.setPlayerXTurn(true);
        if (model.isVsComputer() && model.isComputerX()) {
            computerMove();
        }

        updateScoreDisplay();
        updateTurnDisplay();
    }

    /**
     * Handles actions when a game board button is clicked.
     * @param event ActionEvent triggered when button is pressed.
     */
    @FXML
    private void handleButtonAction(ActionEvent event) {
        Button button = (Button) event.getSource();
        int row = GridPane.getRowIndex(button);
        int col = GridPane.getColumnIndex(button);
        if (model.setMove(row, col)) {
            drawXO(button);
        }
        if (model.isVsComputer() && !model.isRoundOver()) {
            computerMove();
        }
    }

    /**
     * Draws the 'X' or 'O' on the game board.
     * @param button The button where the image will be placed.
     */
    private void drawXO(Button button) {
        Image image = getXOimage();
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(button.getWidth());
        imageView.setFitHeight(button.getHeight());
        imageView.setPreserveRatio(true);
        button.setGraphic(imageView);
        model.switchTurn();
        checkGameStatus();
    }


    /**
     * Loads the appropriate image for 'X' or 'O'.
     * @return Image of 'X' or 'O'.
     */
    private Image getXOimage() {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(model.isPlayerXTurn() ? (IMAGES_PATH + "x.png") : (IMAGES_PATH + "o.png"))));
    }

    /**
     * Handles the "New Game" button action to show the player setup dialog.
     */
    @FXML
    private void handleModeButtonAction() {
        if (mainApp != null) {
            mainApp.showPlayerSetupDialog(new Stage());
        }
    }

    /**
     * Initializes the game with default settings.
     */
    public void initializeGameWithDefaults() {
        model.setDefaultSettings();
        resetRound();
    }

    /**
     * Sets the main application link for communication between the view and the controller.
     * @param mainApp The main application view class.
     */
    public void setMainApp(TicTacToeView mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Updates the turn display on the game board.
     */
    private void updateTurnDisplay() {
        if (model.isPlayerXTurn()) {
            turnLabel.setText("Turn: " + model.getCurrentPlayerX() + " (X)");
        } else {
            String otherPlayer = model.getOtherPlayerName();
            turnLabel.setText("Turn: " + otherPlayer + " (O)");
        }
    }

    /**
     * Updates the score display on the game board.
     */
    private void updateScoreDisplay() {
        scoreLabel.setText(model.getPlayer1Name() + ": " + model.getPlayer1Score() + "   " + model.getPlayer2Name() + ": " + model.getPlayer2Score());
    }

    /**
     * Handles the computer's move in the game.
     */
    private void computerMove() {
        Platform.runLater(() -> {
            int[] move = model.computeComputerMove();
            int row = move[0];
            int col = move[1];

            Button button = null;
            for (Node node : gameBoard.getChildren()) {
                if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col && node instanceof Button) {
                    button = (Button) node;
                    break;
                }
            }

            if (button != null && model.setMove(row, col)) {
                drawXO(button);
            }
        });
    }

    /**
     * Checks the game status to determine if there is a winner or if the board is full.
     */
    private void checkGameStatus() {
        model.checkGameStatus();
        if (model.isRoundOver()) {
            String winnerInfo = model.checkWinner();
            if (!winnerInfo.isEmpty()) {
                char winner = winnerInfo.charAt(0);
                displayWinningLine(winnerInfo.charAt(1), Integer.parseInt(winnerInfo.substring(2)));
                displayEndGameMessage(model.getWinnerName(winner) + " wins!");
                model.updateScore(winner);
                model.switchStartingPlayer();
            } else {
                displayEndGameMessage("It's a draw!");
                model.switchStartingPlayer();
            }
            resetRound();
        }
        updateTurnDisplay();
    }

    /**
     * Displays the winning line on the board when a player wins.
     * @param lineType The type of line ('H' for horizontal, 'V' for vertical, 'D' for diagonal).
     * @param lineIndex The index of the line that corresponds to the winning line.
     */
    public void displayWinningLine(char lineType, int lineIndex) {
        String imageFile = "";
        switch (lineType) {
            case 'H':
                imageFile = (lineIndex == 0) ? "GT.png" : (lineIndex == 1) ? "GM.png" : "GB.png";
                break;
            case 'V':
                imageFile = (lineIndex == 0) ? "VL.png" : (lineIndex == 1) ? "VM.png" : "VR.png";
                break;
            case 'D':
                imageFile = (lineIndex == 0) ? "LU-RD.png" : "LD-RU.png";
                break;
        }
        Image lineImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(IMAGES_PATH + imageFile)));
        ImageView lineImageView = new ImageView(lineImage);
        lineImageView.setFitWidth(300);
        lineImageView.setFitHeight(300);
        gameBoard.add(lineImageView, 0, 0, 3, 3);
    }

    /**
     * Displays an end game message indicating the result (win or draw).
     * @param message The message to display (e.g., "Player 1 wins!" or "It's a draw!").
     */
    private void displayEndGameMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Round Over");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
