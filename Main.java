/***********************************
 * Group 3 Submission
 * 
 * Main Class
 ***********************************/

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private Ui ui;  // your UI logic class

    public static void main(String[] args) {
        launch(args); // always correct here
    }

    @Override
    public void start(Stage stage) {
        ui = new Ui(stage);  // give the UI class the primary stage
        
        // Set minimum window size to prevent resizing issues
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        
        // Set initial window size
        stage.setWidth(900);
        stage.setHeight(700);
        
        ui.showWelcomeScreen();

        stage.setTitle("Submission 3 Tool");
        stage.show();
    }
}
