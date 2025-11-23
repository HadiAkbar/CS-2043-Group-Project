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

    // Main entry point of the program
    // Additional: Launches the JavaFX application thread
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        ui = new Ui(stage); // Initialize the UI with the main stage

        // Set minimum window sizes
        // Additional: Prevents the window from being resized too small
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        // Set initial window size
        // Additional: Provides default size when the application opens
        stage.setWidth(900);
        stage.setHeight(700);

        // Show the welcome/start screen of the application
        // Additional: First screen displayed to the user
        ui.showWelcomeScreen();

        // Set the title of the main window
        // Additional: Visible in the window bar and helps identify the app
        stage.setTitle("Submission 3 Tool");

        stage.show(); // Display the stage on screen
        // Additional: Starts the JavaFX event loop and makes the window interactive
    }
}
