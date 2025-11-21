import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.control.ListView;
import java.io.File;
import java.util.List;

public class Ui
{
    private Coordinator coordinator;
    private Stage primaryStage;

    // Constructor receives the JavaFX Stage from Main
    public Ui(Stage stage)
    {
        this.primaryStage = stage;
        this.coordinator = new Coordinator();
    }

    // ------------------ UI SCREENS ------------------

    // Method to display the initial welcome screen with a start button
    // This is the entry point of the application after Main initializes the stage
    public void showWelcomeScreen()
    {
        Label welcomeLabel = new Label("Welcome to Group 3's Submission 3");
        Button startButton = new Button("Start");

        VBox layout = new VBox(20, welcomeLabel, startButton);
        layout.setStyle("-fx-padding: 50; -fx-alignment: center;");

        Scene scene = new Scene(layout, 800, 600);
        startButton.setOnAction(e -> showFolderSelectionScreen());

        primaryStage.setScene(scene);
    }

    // Method to display the folder selection screen where user specifies the root folder
    // containing student submissions. User can browse or type the path manually
    // Validates the folder exists before proceeding to test suite management
    public void showFolderSelectionScreen()
    {
        Label instructionLabel = new Label("Select or enter root folder for student submissions:");
        TextField pathField = new TextField();
        pathField.setPromptText("Enter folder path here...");

        Button browseButton = new Button("Browse");
        Button proceedButton = new Button("Proceed");

        Label errorLabel = new Label("Please select or enter a valid folder.");
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setVisible(false);

        VBox layout = new VBox(15, instructionLabel, pathField, browseButton, proceedButton, errorLabel);
        layout.setStyle("-fx-padding: 50; -fx-alignment: center;");

        browseButton.setOnAction(e -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Select Root Folder");
            File selected = chooser.showDialog(primaryStage);
            if (selected != null)
            {
                pathField.setText(selected.getAbsolutePath());
                errorLabel.setVisible(false);
            }
        });

        proceedButton.setOnAction(e -> {
            String path = pathField.getText();
            if (path != null && !path.isEmpty())
            {
                File folder = new File(path);
                if (folder.exists() && folder.isDirectory())
                {
                    coordinator.setRootFolder(path);
                    showTestSuiteManagementScreen();
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

        Scene scene = new Scene(layout, 800, 500);
        primaryStage.setScene(scene);
    }

    // Method to display the main test suite management screen
    // Allows user to: create/select test suites, create/manage test cases,
    // add test cases to suites, save suites, and execute test suites
    // This is the central hub for all test suite and test case operations
    public void showTestSuiteManagementScreen()
    {
        Coordinator coordinator = this.coordinator;

        Label saveFolderLabel = new Label("Save Folder: Not set");
        Button browseSaveFolderButton = new Button("Browse Save Folder");
        Button createSuiteButton = new Button("Create New Suite");
        Button selectSuiteButton = new Button("Select Existing Suite");
        Button createCaseButton = new Button("Create New Test Case");
        Button manageCasesButton = new Button("Manage Test Cases");

        ListView<String> testCaseList = new ListView<>();
        Label suiteLabel = new Label("Selected Suite: None");
        Button addCaseButton = new Button("Add Test Case to Suite");
        Button removeCaseButton = new Button("Remove Test Case from Suite");
        Button saveSuiteButton = new Button("Save Suite");
        Button executeSuiteButton = new Button("Execute Test Suite");
        Button backToStartButton = new Button("Back to Start");
        Button doneButton = new Button("Done");

        VBox layout = new VBox(10,
                saveFolderLabel,
                browseSaveFolderButton,
                new Separator(),
                createSuiteButton,
                selectSuiteButton,
                new Separator(),
                suiteLabel,
                new Separator(),
                createCaseButton,
                manageCasesButton,
                new Separator(),
                new Label("Test Cases in Suite:"),
                testCaseList,
                new Separator(),
                addCaseButton,
                removeCaseButton,
                saveSuiteButton,
                executeSuiteButton,
                new Separator(),
                backToStartButton,
                doneButton
        );
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(layout, 900, 750);

        // Runnable to update the save folder label display
        // Checks if save folder is set and updates the label text accordingly
        Runnable updateSaveFolderLabel = () -> {
            String saveFolder = coordinator.getSaveFolder();
            if (saveFolder != null && !saveFolder.isEmpty())
            {
                saveFolderLabel.setText("Save Folder: " + saveFolder);
            }
            else
            {
                saveFolderLabel.setText("Save Folder: Not set");
            }
        };
        updateSaveFolderLabel.run();

        // Runnable to refresh the test case list display for the currently selected suite
        // Loads all test cases referenced by the suite and displays them in the list
        // Also updates the suite label to show which suite is currently selected
        Runnable refreshSuiteCaseList = () -> {
            testCaseList.getItems().clear();
            if (coordinator.getCurrentTestSuite() != null)
            {
                suiteLabel.setText("Selected Suite: " + coordinator.getCurrentTestSuite().getTitle());
                for (String filename : coordinator.getCurrentTestSuite().getTestCaseFilenames())
                {
                    TestCase tc = coordinator.getTestCaseByFilename(filename);
                    if (tc != null)
                    {
                        testCaseList.getItems().add(tc.getTitle() + " (" + filename + ")");
                    }
                    else
                    {
                        testCaseList.getItems().add(filename + " (not found)");
                    }
                }
            }
            else
            {
                suiteLabel.setText("Selected Suite: None");
            }
        };

        // Button action: Opens folder browser to select where test cases and suites are saved
        // Once selected, loads any existing test cases and suites from that folder
        browseSaveFolderButton.setOnAction(e -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Select Folder for Test Cases and Suites");
            File selected = chooser.showDialog(primaryStage);
            if (selected != null)
            {
                coordinator.setSaveFolder(selected.getAbsolutePath());
                updateSaveFolderLabel.run();
                refreshSuiteCaseList.run();
            }
        });

        // Button action: Creates a new test suite with a user-provided title
        // Opens a text input dialog to get the suite name, then creates and selects the new suite
        createSuiteButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Create Test Suite");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter Test Suite Title:");
            dialog.showAndWait().ifPresent(title -> {
                if (!title.isEmpty())
                {
                    coordinator.createTestSuite(title);
                    refreshSuiteCaseList.run();
                }
            });
        });

        // Button action: Allows user to select an existing test suite from a list
        // Shows all available suites in a choice dialog, then loads and selects the chosen suite
        selectSuiteButton.setOnAction(e -> {
            List<TestSuite> suites = coordinator.getAllTestSuites();
            if (suites.isEmpty())
            {
                showErrorDialog("No Suites Available", "No test suites found. Please create a test suite first or set the save folder.");
                return;
            }

            List<String> suiteTitles = new java.util.ArrayList<>();
            for (TestSuite suite : suites)
            {
                suiteTitles.add(suite.getTitle());
            }

            ChoiceDialog<String> dialog = new ChoiceDialog<>(suiteTitles.get(0), suiteTitles);
            dialog.setTitle("Select Test Suite");
            dialog.setHeaderText(null);
            dialog.setContentText("Select test suite:");
            dialog.showAndWait().ifPresent(title -> {
                TestSuite selectedSuite = coordinator.getListOfTestSuites().findSuiteByTitle(title);
                if (selectedSuite != null)
                {
                    coordinator.setCurrentTestSuite(selectedSuite);
                    refreshSuiteCaseList.run();
                }
            });
        });

        // Button action: Creates a new test case by opening the test case dialog
        // Saves the test case to file and adds it to the global pool of test cases
        createCaseButton.setOnAction(e -> {
            TestCase tc = promptTestCase(null);
            if (tc != null)
            {
                try
                {
                    coordinator.createAndSaveTestCase(tc);
                    showInfoDialog("Test Case Created", "Test case '" + tc.getTitle() + "' has been created and saved.");
                }
                catch (Exception ex)
                {
                    showErrorDialog("Error", "Failed to save test case: " + ex.getMessage());
                }
            }
        });

        manageCasesButton.setOnAction(e -> {
            showTestCaseManagementScreen();
        });

        // Button action: Adds an existing test case to the currently selected test suite
        // Shows a list of all available test cases, user selects one to add by reference
        // This implements the many-to-many relationship (test cases can be in multiple suites)
        addCaseButton.setOnAction(e -> {
            if (coordinator.getCurrentTestSuite() == null)
            {
                showErrorDialog("No Suite Selected", "Please create or select a test suite first.");
                return;
            }

            List<String> availableFilenames = coordinator.getAvailableTestCaseFilenames();
            if (availableFilenames.isEmpty())
            {
                showErrorDialog("No Test Cases", "No test cases available. Please create a test case first.");
                return;
            }

            ChoiceDialog<String> dialog = new ChoiceDialog<>(availableFilenames.get(0), availableFilenames);
            dialog.setTitle("Add Test Case to Suite");
            dialog.setHeaderText(null);
            dialog.setContentText("Select test case to add:");
            dialog.showAndWait().ifPresent(filename -> {
                coordinator.getCurrentTestSuite().addTestCaseFilename(filename);
                refreshSuiteCaseList.run();
            });
        });

        // Button action: Removes a test case from the currently selected test suite
        // Only removes the reference from the suite, does not delete the test case file
        // The test case can still be used in other suites
        removeCaseButton.setOnAction(e -> {
            String selected = testCaseList.getSelectionModel().getSelectedItem();
            if (selected != null && coordinator.getCurrentTestSuite() != null)
            {
                // Extract filename from the display string
                String filename = extractFilenameFromDisplay(selected);
                coordinator.getCurrentTestSuite().removeTestCaseFilename(filename);
                refreshSuiteCaseList.run();
            }
        });

        // Button action: Saves the currently selected test suite to a file
        // Saves the suite with all its test case references to the save folder
        saveSuiteButton.setOnAction(e -> {
            if (coordinator.getCurrentTestSuite() == null)
            {
                showErrorDialog("No Suite Selected", "Please create or select a test suite first.");
                return;
            }

            try
            {
                coordinator.saveTestSuite(coordinator.getCurrentTestSuite());
                showInfoDialog("Suite Saved", "Test suite '" + coordinator.getCurrentTestSuite().getTitle() + "' has been saved.");
            }
            catch (Exception ex)
            {
                showErrorDialog("Error", "Failed to save test suite: " + ex.getMessage());
            }
        });

        // Button action: Navigates to the test suite execution screen
        // Validates that a suite is selected and root folder is set before proceeding
        executeSuiteButton.setOnAction(e -> {
            if (coordinator.getCurrentTestSuite() == null)
            {
                showErrorDialog("No Suite Selected", "Please create or select a test suite first.");
                return;
            }
            if (coordinator.getRootFolder() == null || coordinator.getRootFolder().isEmpty())
            {
                showErrorDialog("Root Folder Not Set", "Please set the root folder for student submissions first.");
                return;
            }
            showExecuteTestSuiteScreen();
        });

        backToStartButton.setOnAction(e -> {
            showWelcomeScreen();
        });

        doneButton.setOnAction(e -> {
            System.out.println("Test Suite finalized: " +
                    (coordinator.getCurrentTestSuite() != null ? coordinator.getCurrentTestSuite().getTitle() : "none"));
        });

        refreshSuiteCaseList.run();
        primaryStage.setScene(scene);
    }

    // Method to display the test case management screen
    // Shows all available test cases and allows user to edit or delete them
    // Test cases are managed globally and can be reused across multiple test suites
    private void showTestCaseManagementScreen()
    {
        Coordinator coordinator = this.coordinator;

        ListView<String> testCaseList = new ListView<>();
        Button editCaseButton = new Button("Edit Selected Test Case");
        Button deleteCaseButton = new Button("Delete Selected Test Case");
        Button backButton = new Button("Back");
        Button restartButton = new Button("Restart from Beginning");

        VBox layout = new VBox(10,
                new Label("All Test Cases:"),
                testCaseList,
                new Separator(),
                editCaseButton,
                deleteCaseButton,
                backButton,
                restartButton
        );
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(layout, 800, 600);

        // Runnable to refresh the list of all available test cases
        // Displays each test case with its title and filename
        Runnable refreshCaseList = () -> {
            testCaseList.getItems().clear();
            for (TestCase tc : coordinator.getListOfTestCases().getTestCases())
            {
                testCaseList.getItems().add(tc.getTitle() + " (" + tc.getFilename() + ")");
            }
        };

        // Button action: Edits the selected test case
        // Opens the test case dialog pre-filled with existing values, then saves the updated test case
        editCaseButton.setOnAction(e -> {
            String selected = testCaseList.getSelectionModel().getSelectedItem();
            if (selected != null)
            {
                String filename = extractFilenameFromDisplay(selected);
                TestCase tc = coordinator.getTestCaseByFilename(filename);
                if (tc != null)
                {
                    TestCase edited = promptTestCase(tc);
                    if (edited != null)
                    {
                        try
                        {
                            // Update the test case
                            tc.setTitle(edited.getTitle());
                            tc.setInputData(edited.getInputData());
                            tc.setExpectedOutput(edited.getExpectedOutput());
                            tc.setType(edited.getType());
                            
                            // Save the updated test case
                            coordinator.createAndSaveTestCase(tc);
                            refreshCaseList.run();
                            showInfoDialog("Test Case Updated", "Test case has been updated and saved.");
                        }
                        catch (Exception ex)
                        {
                            showErrorDialog("Error", "Failed to save test case: " + ex.getMessage());
                        }
                    }
                }
            }
        });

        // Button action: Deletes the selected test case
        // Removes it from the global list and deletes the file from disk
        // Note: This does not remove references from test suites (they will show as "not found")
        deleteCaseButton.setOnAction(e -> {
            String selected = testCaseList.getSelectionModel().getSelectedItem();
            if (selected != null)
            {
                String filename = extractFilenameFromDisplay(selected);
                TestCase tc = coordinator.getTestCaseByFilename(filename);
                if (tc != null)
                {
                    // Remove from list
                    coordinator.getListOfTestCases().removeTestCase(tc);
                    
                    // Delete file
                    try
                    {
                        String saveFolder = coordinator.getSaveFolder();
                        if (saveFolder != null && !saveFolder.isEmpty())
                        {
                            File testCaseFile = new File(new File(saveFolder, "test-cases"), filename);
                            if (testCaseFile.exists())
                            {
                                testCaseFile.delete();
                            }
                        }
                    }
                    catch (Exception ex)
                    {
                        System.err.println("Error deleting test case file: " + ex.getMessage());
                    }
                    
                    refreshCaseList.run();
                }
            }
        });

        backButton.setOnAction(e -> {
            showTestSuiteManagementScreen();
        });

        restartButton.setOnAction(e -> {
            showWelcomeScreen();
        });

        refreshCaseList.run();
        primaryStage.setScene(scene);
    }

    // Helper method to extract the filename from a display string
    // Display format is "Title (filename.testcase)" - this extracts just the filename part
    // Used when user selects a test case from a list and we need the actual filename
    private String extractFilenameFromDisplay(String displayString)
    {
        // Extract filename from display string like "Title (filename.testcase)"
        int start = displayString.indexOf("(");
        int end = displayString.indexOf(")");
        if (start != -1 && end != -1 && end > start)
        {
            return displayString.substring(start + 1, end);
        }
        return displayString;
    }

    // Helper method to display an information dialog to the user
    // Shows a popup with the given title and message, user must click OK to dismiss
    private void showInfoDialog(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper method to display an error dialog to the user
    // Shows a popup with the given title and error message, user must click OK to dismiss
    private void showErrorDialog(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to display the test suite execution configuration screen
    // Shows the root folder and allows user to specify the code path within each submission
    // (e.g., "src" if code is in a src subfolder). When execute is clicked, runs the test suite
    private void showExecuteTestSuiteScreen()
    {
        Coordinator coordinator = this.coordinator;

        Label rootFolderLabel = new Label("Root Folder: " + 
            (coordinator.getRootFolder() != null ? coordinator.getRootFolder() : "Not set"));
        TextField codePathField = new TextField();
        codePathField.setPromptText("e.g., src (leave empty if code is directly in submission folder)");
        Label codePathLabel = new Label("Code path within each submission folder:");

        Button executeButton = new Button("Execute Test Suite");
        Button backButton = new Button("Back");

        VBox layout = new VBox(15,
                new Label("Execute Test Suite: " + coordinator.getCurrentTestSuite().getTitle()),
                new Separator(),
                rootFolderLabel,
                codePathLabel,
                codePathField,
                new Separator(),
                executeButton,
                backButton
        );
        layout.setStyle("-fx-padding: 30; -fx-alignment: center;");

        Scene scene = new Scene(layout, 800, 500);

        // Button action: Executes the test suite on all student submissions
        // Gets the code path (if specified) and triggers test execution
        // Currently shows placeholder results - actual execution logic will be implemented here
        executeButton.setOnAction(e -> {
            String codePath = codePathField.getText().trim();
            // TODO: Call actual execution logic here
            // For now, show results screen with placeholder data
            showResultsScreen(codePath);
        });

        backButton.setOnAction(e -> {
            showTestSuiteManagementScreen();
        });

        primaryStage.setScene(scene);
    }

    // Method to display the test execution results screen
    // Shows a list of all students with their test case results (PASSED/FAILED/COMPILE ERROR)
    // User can select a result to view detailed side-by-side comparison
    private void showResultsScreen(String codePath)
    {
        Coordinator coordinator = this.coordinator;
        TestSuite suite = coordinator.getCurrentTestSuite();

        Label titleLabel = new Label("Test Results for: " + suite.getTitle());
        
        // ListView to show students and their results
        ListView<String> resultsList = new ListView<>();
        resultsList.setPrefHeight(400);

        Button viewComparisonButton = new Button("View Comparison (Selected)");
        Button backButton = new Button("Back");
        Button restartButton = new Button("Restart from Beginning");

        VBox layout = new VBox(10,
                titleLabel,
                new Separator(),
                new Label("Results (Student - Test Case - Status):"),
                resultsList,
                new Separator(),
                viewComparisonButton,
                backButton,
                restartButton
        );
        layout.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(layout, 1000, 700);

        // TODO: Replace with actual results from execution
        // Placeholder data for UI structure - format: "StudentName | TestCaseTitle | Status"
        resultsList.getItems().add("Student1 | TestCase1 | PASSED");
        resultsList.getItems().add("Student1 | TestCase2 | FAILED");
        resultsList.getItems().add("Student2 | TestCase1 | PASSED");
        resultsList.getItems().add("Student2 | TestCase2 | PASSED");
        resultsList.getItems().add("Student3 | TestCase1 | COMPILE ERROR");

        // Button action: Opens the side-by-side comparison screen for the selected result
        // Parses the result string to extract student name and test case title
        // Result format is "StudentName | TestCaseTitle | Status"
        viewComparisonButton.setOnAction(e -> {
            String selected = resultsList.getSelectionModel().getSelectedItem();
            if (selected != null)
            {
                // Parse selected item to get student and test case
                // Format: "StudentName | TestCaseTitle | Status"
                String[] parts = selected.split("\\|");
                if (parts.length >= 2)
                {
                    String studentName = parts[0].trim();
                    String testCaseTitle = parts[1].trim();
                    showComparisonScreen(studentName, testCaseTitle);
                }
                else
                {
                    showErrorDialog("Invalid Format", "Could not parse result selection.");
                }
            }
            else
            {
                showErrorDialog("No Selection", "Please select a result to compare.");
            }
        });

        backButton.setOnAction(e -> {
            showExecuteTestSuiteScreen();
        });

        restartButton.setOnAction(e -> {
            showWelcomeScreen();
        });

        primaryStage.setScene(scene);
    }

    // Method to display the side-by-side comparison screen
    // Shows expected output (from test case) and actual output (from program execution) side by side
    // Allows user to visually compare what was expected vs what the program actually produced
    private void showComparisonScreen(String studentName, String testCaseTitle)
    {
        Coordinator coordinator = this.coordinator;

        Label titleLabel = new Label("Comparison: " + studentName + " - " + testCaseTitle);

        TextArea expectedArea = new TextArea();
        expectedArea.setEditable(false);
        expectedArea.setPrefRowCount(15);
        expectedArea.setPrefColumnCount(40);
        expectedArea.setWrapText(true);

        TextArea actualArea = new TextArea();
        actualArea.setEditable(false);
        actualArea.setPrefRowCount(15);
        actualArea.setPrefColumnCount(40);
        actualArea.setWrapText(true);

        Label expectedLabel = new Label("Expected Output:");
        Label actualLabel = new Label("Actual Output:");

        Button backButton = new Button("Back to Results");
        Button restartButton = new Button("Restart from Beginning");

        HBox comparisonBox = new HBox(20,
                new VBox(5, expectedLabel, expectedArea),
                new VBox(5, actualLabel, actualArea)
        );
        comparisonBox.setStyle("-fx-padding: 10;");

        VBox layout = new VBox(10,
                titleLabel,
                new Separator(),
                comparisonBox,
                new Separator(),
                backButton,
                restartButton
        );
        layout.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(layout, 1100, 600);

        // Sample output data for display
        expectedArea.setText("Expected output placeholder\nThis will show the expected output from the test case.");
        actualArea.setText("Actual output placeholder\nThis will show the actual output from running the program.");

        backButton.setOnAction(e -> {
            showResultsScreen("");
        });

        restartButton.setOnAction(e -> {
            showWelcomeScreen();
        });

        primaryStage.setScene(scene);
    }

    // Method to display a dialog for creating or editing a test case
    // Shows input fields for: title, input data, expected output, and type (Boolean/Int/Double/String)
    // If existing is null, creates a new test case. If existing is provided, pre-fills fields for editing
    // Returns the TestCase object if user clicks OK, or null if cancelled
    private TestCase promptTestCase(TestCase existing)
    {
        Dialog<TestCase> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Add Test Case" : "Edit Test Case");

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        TextField titleField = new TextField();
        TextField inputField = new TextField();
        TextField expectedField = new TextField();

        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Boolean", "Int", "Double", "String");

        if (existing != null)
        {
            titleField.setText(existing.getTitle());
            inputField.setText(existing.getInputData());
            expectedField.setText(existing.getExpectedOutput());
            typeCombo.setValue(existing.getType());
        }

        VBox content = new VBox(10,
                new Label("Title:"), titleField,
                new Label("Input:"), inputField,
                new Label("Expected Output:"), expectedField,
                new Label("Type:"), typeCombo
        );
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(button -> {
            if (button == okButtonType)
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

