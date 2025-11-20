import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;

import javax.swing.text.html.ListView;

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

    private void showFolderSelectionScreen()
    {
        Label instructionLabel = new Label("Select or enter root folder for student submissions:");
        TextField pathField = new TextField();
        pathField.setPromptText("Enter folder path here...");

        Button browseButton = new Button("Browse");
        Button proceedButton = new Button("Proceed");

        // --- Error label ---
        Label errorLabel = new Label("Please select or enter a valid folder.");
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setVisible(false);

        VBox layout = new VBox(15, instructionLabel, pathField, browseButton, proceedButton, errorLabel);
        layout.setStyle("-fx-padding: 50; -fx-alignment: center;");

        browseButton.setOnAction(e -> 
        {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Select Root Folder");
            File selected = chooser.showDialog(primaryStage);
            if (selected != null) 
            {
                pathField.setText(selected.getAbsolutePath());
                errorLabel.setVisible(false); // clear error if valid
            }
        });

        proceedButton.setOnAction(e -> 
        {
            String path = pathField.getText();
            if (path != null && !path.isEmpty())
            {
                File folder = new File(path);
                if (folder.exists() && folder.isDirectory())
                {
                    coordinator.setRootFolder(path);
                    showTestSuiteScreen();
                }
                else
                {
                    errorLabel.setText("Folder does not exist. Please select a valid folder.");
                    errorLabel.setVisible(true);
                }
            }
            else
            {
                errorLabel.setText("Please select or enter a valid folder.");
                errorLabel.setVisible(true);
            }
        });

        primaryStage.setScene(new Scene(layout, 600, 400));
    }


    // --- Test Suite Management Screen ---
    private void showTestSuiteManagementScreen()
    {
        Coordinator coordinator = this.coordinator; // convenience

        // --- Suite Selection / Creation ---
        Button createSuiteButton = new Button("Create New Suite");
        Button selectSuiteButton = new Button("Select Existing Suite");

        ListView<String> testCaseList = new ListView<>();
        Button addCaseButton = new Button("Add Test Case");
        Button editCaseButton = new Button("Edit Selected Case");
        Button removeCaseButton = new Button("Remove Selected Case");
        Button doneButton = new Button("Done");

        VBox layout = new VBox(10,
                createSuiteButton,
                selectSuiteButton,
                new Separator(),
                new Label("Test Cases in Suite:"),
                testCaseList,
                new Separator(),
                addCaseButton,
                editCaseButton,
                removeCaseButton,
                doneButton
        );
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(layout, 600, 600);

        // --- Button Actions ---
        createSuiteButton.setOnAction(e -> 
        {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Create Test Suite");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter Test Suite Title:");
            dialog.showAndWait().ifPresent(title -> 
            {
                if (!title.isEmpty())
                {
                    coordinator.createTestSuite(title);
                    testCaseList.getItems().clear();
                    System.out.println("Test Suite created: " + title);
                }
            });
        });

        selectSuiteButton.setOnAction(e -> 
        {
            // TODO: implement file chooser to load suite
            coordinator.loadTestSuite();
            testCaseList.getItems().clear();
            if (coordinator.getCurrentTestSuite() != null)
            {
                for (TestCase tc : coordinator.getCurrentTestSuite().getTestCases())
                {
                    testCaseList.getItems().add(tc.getTitle());
                }
            }
        });

        addCaseButton.setOnAction(e -> 
        {
            TestCase tc = promptTestCase(null);
            if (tc != null)
            {
                coordinator.getCurrentTestSuite().addTestCase(tc);
                testCaseList.getItems().add(tc.getTitle());
            }
        });

        editCaseButton.setOnAction(e -> 
        {
            String selected = testCaseList.getSelectionModel().getSelectedItem();
            if (selected != null)
            {
                TestCase tc = coordinator.getCurrentTestSuite().getTestCases().stream()
                        .filter(t -> t.getTitle().equals(selected))
                        .findFirst()
                        .orElse(null);
                if (tc != null)
                {
                    TestCase edited = promptTestCase(tc);
                    if (edited != null)
                    {
                        tc.setTitle(edited.getTitle());
                        tc.setInputData(edited.getInputData());
                        tc.setExpectedOutput(edited.getExpectedOutput());
                        testCaseList.getItems().set(testCaseList.getSelectionModel().getSelectedIndex(), edited.getTitle());
                    }
                }
            }
        });

        removeCaseButton.setOnAction(e -> 
        {
            String selected = testCaseList.getSelectionModel().getSelectedItem();
            if (selected != null)
            {
                TestCase tc = coordinator.getCurrentTestSuite().getTestCases().stream()
                        .filter(t -> t.getTitle().equals(selected))
                        .findFirst()
                        .orElse(null);
                if (tc != null)
                {
                    coordinator.getCurrentTestSuite().removeTestCase(tc);
                    testCaseList.getItems().remove(selected);
                }
            }
        });

        doneButton.setOnAction(e -> 
        {
            System.out.println("Test Suite finalized: " + coordinator.getCurrentTestSuite().getTitle());
            // TODO: transition to execution or main menu
        });

        primaryStage.setScene(scene);
    }

    // --- Prompt for Adding / Editing Test Case ---
    private TestCase promptTestCase(TestCase existing)
    {
        Dialog<TestCase> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Add Test Case" : "Edit Test Case");

        // --- Buttons ---
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // --- Fields ---
        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField inputField = new TextField();
        inputField.setPromptText("Input Data");

        TextField expectedField = new TextField();
        expectedField.setPromptText("Expected Output");

        // --- Type selection ---
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Boolean", "Int", "Double", "String");
        typeCombo.setPromptText("Select Type");

        if (existing != null)
        {
            titleField.setText(existing.getTitle());
            inputField.setText(existing.getInputData());
            expectedField.setText(existing.getExpectedOutput());
            typeCombo.setValue(existing.getType()); // existing type
        }

        VBox content = new VBox(10,
                new Label("Title:"), titleField,
                new Label("Input:"), inputField,
                new Label("Expected Output:"), expectedField,
                new Label("Type:"), typeCombo
        );
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> 
        {
            if (dialogButton == okButtonType)
            {
                return new TestCase(
                    titleField.getText(),
                    inputField.getText(),
                    expectedField.getText(),
                    typeCombo.getValue()
                );
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

}
