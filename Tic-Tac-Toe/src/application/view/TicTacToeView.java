package application.view;

import application.controller.PlayerSetupDialogController;
import application.controller.TicTacToeController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * This class represents the main view of the Tic Tac Toe game.
 */
public class TicTacToeView {

    private TicTacToeController mainController;
    private FXMLLoader mainLoader;


    /**
     * Starts the main application stage and sets up the game interface.
     *
     * @param primaryStage The primary stage for this application.
     * @throws Exception if there is a problem loading the view.
     */
    public void start(Stage primaryStage) throws Exception {
        mainLoader = new FXMLLoader(getClass().getResource("/TicTacToe.fxml"));
        Parent root = mainLoader.load();
        primaryStage.setTitle("Tic Tac Toe");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/icon.jpg")));
        primaryStage.getIcons().add(icon);
        primaryStage.show();

        mainController = mainLoader.getController();
        TicTacToeController controller = mainLoader.getController();
        controller.setMainApp(this);

        showPlayerSetupDialog(primaryStage);
    }

    /**
     * Shows the player setup dialog as a modal window.
     *
     * @param primaryStage The primary stage to which the dialog will be modal.
     */
    public void showPlayerSetupDialog(Stage primaryStage) {
        try {
            FXMLLoader dialogLoader = new FXMLLoader(getClass().getResource("/PlayerSetupDialog.fxml"));
            Parent dialogRoot = dialogLoader.load();

            TicTacToeController controller = mainLoader.getController();
            controller.initializeGameWithDefaults();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Setup Players");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene dialogScene = new Scene(dialogRoot);
            dialogStage.setOnCloseRequest(event -> {
                controller.initializeGameWithDefaults();
            });
            dialogStage.setResizable(false);
            dialogStage.setScene(dialogScene);

            PlayerSetupDialogController dialogController = dialogLoader.getController();
            dialogController.setDialogStage(dialogStage);
            dialogController.setMainController(mainController);

            dialogStage.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
