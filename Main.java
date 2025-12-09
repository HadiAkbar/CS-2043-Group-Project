/***********************************
 * Group 3 Submission
 * team members: Prabhas, Hadi, Christian, Rudolph
 *
 * Main Class
 ***********************************/

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private Ui ui; // Handles all UI interactions and screens

    /**
     * Main entry point of the program.
     * Launches the JavaFX application thread.
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        ui = new Ui(stage);

        // Set minimum window sizes to prevent resizing too small
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        // Set initial window size
        stage.setWidth(900);
        stage.setHeight(700);

        // Show the welcome screen
        ui.showWelcomeScreen();

        // Set the application title
        stage.setTitle("CS-2043 Automated Testing Tool");

        stage.show();
    }
}