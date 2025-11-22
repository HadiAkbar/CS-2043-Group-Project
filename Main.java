/***********************************
 * Group 3 Submission
 *
 * Main Class
 ***********************************/

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private Ui ui;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        ui = new Ui(stage);

        // Set window sizes
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        stage.setWidth(900);
        stage.setHeight(700);

        ui.showWelcomeScreen();

        stage.setTitle("Submission 3 Tool");
        stage.show();
    }
}
