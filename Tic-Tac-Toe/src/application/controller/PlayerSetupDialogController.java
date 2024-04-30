package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * Controller for the player setup dialog in the Tic Tac Toe game.
 * Manages player configuration before the game starts.
 */
public class PlayerSetupDialogController {

    private TicTacToeController mainController;
    @FXML
    private TextField nameField1;
    @FXML
    private TextField nameField2;
    @FXML
    private CheckBox computerCheckBox;
    @FXML
    private RadioButton rbPlayer1;
    @FXML
    private RadioButton rbPlayer2;
    @FXML
    private ToggleGroup group;
    private Stage dialogStage;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded. It sets up the radio buttons with a toggle group.
     */
    @FXML
    public void initialize() {
        group = new ToggleGroup();
        rbPlayer1.setToggleGroup(group);
        rbPlayer2.setToggleGroup(group);
    }

    /**
     * Handles the action when the 'OK' button is clicked.
     * It retrieves the player names and whether the game is against a computer,
     * and then initializes the game through the main controller.
     */
    @FXML
    private void handleOkButtonAction() {
        String player1Name = nameField1.getText();
        String player2Name = nameField2.getText();
        boolean isComputer = computerCheckBox.isSelected();
        String playerXname = rbPlayer1.isSelected() ? player1Name : player2Name;

        // Call the method to initialize the game with the provided settings
        if (mainController != null) {
            mainController.initializeGame(player1Name, player2Name, playerXname, isComputer);
        }

        // Close the dialog stage
        dialogStage.close();
    }

    /**
     * Handles the action when the 'Computer' checkbox is toggled.
     * It disables or enables the player 2 name field based on the checkbox state.
     */
    @FXML
    private void handleComputerCheckBoxAction() {
        if (computerCheckBox.isSelected()) {
            nameField2.setText("Computer");
            nameField2.setDisable(true);
        } else {
            nameField2.setText("Player2");
            nameField2.setDisable(false);
        }
    }

    /**
     * Sets the reference to the main controller of the Tic Tac Toe game.
     *
     * @param mainController The main controller to set.
     */
    public void setMainController(TicTacToeController mainController) {
        this.mainController = mainController;
    }


    /**
     * Sets the stage for this dialog to enable closing it from within.
     *
     * @param dialogStage The stage of this dialog.
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}

