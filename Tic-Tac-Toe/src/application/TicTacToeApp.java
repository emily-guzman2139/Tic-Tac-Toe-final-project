package application;
	
import application.view.TicTacToeView;
import javafx.application.Application;
import javafx.stage.Stage;


/**
 * Entry point for the Tic Tac Toe application.
 * This class launches the JavaFX application.
 */
public class TicTacToeApp extends Application {

    /**
     * Main method that is the entry point of the application.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the primary stage and sets up the game view.
     * @param primaryStage The primary stage for this application.
     * @throws Exception if there is a problem loading the view.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        TicTacToeView ticTacToeView = new TicTacToeView();
        ticTacToeView.start(primaryStage);
    }
}