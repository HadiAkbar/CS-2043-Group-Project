import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;

public class Ui extends Application
{
    private Coordinator coordinator;
    private Stage primaryStage;

    @Override
    public void start(Stage stage)
    {
        this.primaryStage = stage;
        this.coordinator = new Coordinator();

        showWelcomeScreen();
        primaryStage.setTitle("Submission 3 Tool");
        primaryStage.show();
    }

    // --- Welcome Screen ---
    private void showWelcomeScreen()
    {
        Label welcomeLabel = new Label("Welcome to Group 3's Submission 3");
        Button startButton = new Button("Start");

        VBox layout = new VBox(20, welcomeLabel, startButton);
        layout.setStyle("-fx-padding: 50; -fx-alignment: center;");

        Scene scene = new Scene(layout, 500, 300);
        startButton.setOnAction(e -> showFolderSelectionScreen());

        primaryStage.setScene(scene);
    }

    // --- Folder Selection Screen ---
    private void showFolderSelectionScreen()
    {
        Label instructionLabel = new Label("Select or enter root folder for student submissions:");
        TextField pathField = new TextField();
        pathField.setPromptText("Enter folder path here...");

        Button browseButton = new Button("Browse");
        Button proceedButton = new Button("Proceed");

        VBox layout = new VBox(15, instructionLabel, pathField, browseButton, proceedButton);
        layout.setStyle("-fx-padding: 50; -fx-alignment: center;");

        browseButton.setOnAction(e -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Select Root Folder");
            File selected = chooser.showDialog(primaryStage);
            if (selected != null) pathField.setText(selected.getAbsolutePath());
        });

        proceedButton.setOnAction(e -> {
            String path = pathField.getText();
            if (path != null && !path.isEmpty())
            {
                coordinator.setRootFolder(path);
                showTestSuiteScreen();
            }
            else System.out.println("Please select or enter a valid folder.");
        });

        primaryStage.setScene(new Scene(layout, 600, 400));
    }

    // --- Test Suite / Test Case Screen ---
    private void showTestSuiteScreen()
    {
        // Suite creation
        Label suiteLabel = new Label("Enter Test Suite Title:");
        TextField suiteField = new TextField();
        Button createSuiteButton = new Button("Create Test Suite");

        // Test Case creation
        Label caseTitleLabel = new Label("Test Case Title:");
        TextField caseTitleField = new TextField();
        Label inputLabel = new Label("Input Data:");
        TextField inputField = new TextField();
        Label expectedLabel = new Label("Expected Output:");
        TextField expectedField = new TextField();
        Button addCaseButton = new Button("Add Test Case");

        // Save / Load buttons
        Button saveSuiteButton = new Button("Save Test Suite");
        Button loadSuiteButton = new Button("Load Test Suite");

        // Display added test cases
        ListView<String> testCaseList = new ListView<>();

        VBox layout = new VBox(10,
                suiteLabel, suiteField, createSuiteButton,
                new Separator(),
                caseTitleLabel, caseTitleField,
                inputLabel, inputField,
                expectedLabel, expectedField,
                addCaseButton,
                new Separator(),
                new Label("Test Cases:"), testCaseList,
                new Separator(),
                saveSuiteButton, loadSuiteButton
        );
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // --- Button actions ---
        createSuiteButton.setOnAction(e -> {
            String title = suiteField.getText();
            if (!title.isEmpty())
            {
                coordinator.createTestSuite(title);
                testCaseList.getItems().clear();
                System.out.println("Test Suite created: " + title);
            }
        });

        addCaseButton.setOnAction(e -> {
            String title = caseTitleField.getText();
            String input = inputField.getText();
            String expected = expectedField.getText();
            if (!title.isEmpty())
            {
                TestCase testCase = new TestCase(title, input, expected);
                coordinator.getCurrentTestSuite().addTestCase(testCase);
                testCaseList.getItems().add(title);
                System.out.println("Added Test Case: " + title);
            }
        });

        saveSuiteButton.setOnAction(e -> coordinator.saveCurrentTestSuite());
        loadSuiteButton.setOnAction(e -> {
            coordinator.loadTestSuite();
            testCaseList.getItems().clear();
            for (TestCase tc : coordinator.getCurrentTestSuite().getTestCases())
                testCaseList.getItems().add(tc.getTitle());
        });

        primaryStage.setScene(new Scene(layout, 600, 600));
    }

    public static void main(String[] args)
    {
        launch(args); // clean, no logic here
    }
}
