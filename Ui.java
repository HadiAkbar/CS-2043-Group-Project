import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
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

        // styling
        welcomeLabel.setStyle("-fx-text-fill: #E8E8F2; -fx-font-size: 20px; -fx-font-weight: bold;");
        styleButtonBold(startButton, "10 20");

        VBox layout = new VBox(20, welcomeLabel, startButton);
        // background gradient + centering + padding
        layout.setStyle("-fx-padding: 50; -fx-alignment: center; -fx-background-color: linear-gradient(to bottom right, #1e1e2f, #2d2d44);");

        Scene scene = new Scene(layout, 900, 750);
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
        errorLabel.setStyle("-fx-text-fill: #ff6b6b;");
        errorLabel.setVisible(false);

        // style controls
        instructionLabel.setStyle("-fx-text-fill: #E8E8F2; -fx-font-size: 16px; -fx-font-weight: 600;");
        pathField.setStyle("-fx-background-color: #303046; -fx-text-fill: #E8E8F2; -fx-background-radius: 6; -fx-padding: 6 8;");
        styleButton(browseButton, "8 14");
        styleButton(proceedButton, "8 14");

        VBox layout = new VBox(15, instructionLabel, pathField, browseButton, proceedButton, errorLabel);
        layout.setStyle("-fx-padding: 50; -fx-alignment: center; -fx-background-color: linear-gradient(to bottom right, #1e1e2f, #2d2d44);");

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

        Label saveFolderLabel = new Label("Folder containing test-suites/ and test-cases: Not set");
        saveFolderLabel.setStyle("-fx-text-fill: #E8E8F2; -fx-font-weight: 600;");
        Button browseSaveFolderButton = new Button("Browse Test Folders");
        Button createSuiteButton = new Button("Create New Suite");
        Button selectSuiteButton = new Button("Select Existing Suite");
        Button createCaseButton = new Button("Create New Test Case");
        Button manageCasesButton = new Button("Manage Test Cases");

        // style buttons (apply consistent style + hover)
        Button[] topButtons = {browseSaveFolderButton, createSuiteButton, selectSuiteButton, createCaseButton, manageCasesButton};
        for (Button b : topButtons) {
            styleButton(b);
        }

        ListView<String> testCaseList = new ListView<>();
        testCaseList.setStyle("-fx-background-color: #262634; -fx-control-inner-background: #262634; -fx-border-color: #3a3a5a; -fx-border-radius: 6; -fx-padding: 6; -fx-text-fill: #E8E8F2;");
        Label suiteLabel = new Label("Selected Suite: None");
        suiteLabel.setStyle("-fx-text-fill: #E8E8F2; -fx-font-weight: 600;");
        Button addCaseButton = new Button("Add Test Case to Suite");
        Button removeCaseButton = new Button("Remove Test Case from Suite");
        Button saveSuiteButton = new Button("Save Suite");
        Button executeSuiteButton = new Button("Execute Test Suite");
        Button backToStartButton = new Button("Back to Start");
        Button[] otherButtons = {addCaseButton, removeCaseButton, saveSuiteButton, executeSuiteButton, backToStartButton};
        for (Button b : otherButtons) {
            styleButton(b);
        }

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
                new Separator(),
                executeSuiteButton,
                backToStartButton
        );

        // style labels inside the layout (header & list label)
        for (javafx.scene.Node node : layout.getChildren()) {
            if (node instanceof Label) {
                ((Label) node).setStyle("-fx-text-fill: #E8E8F2; -fx-font-weight: 600;");
            }
        }

        layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: linear-gradient(to bottom right, #1e1e2f, #2d2d44);");

        Scene scene = new Scene(layout, 1000, 850);

        // Runnable to update the folder label display
        // Checks if folder is set and updates the label text accordingly
        Runnable updateSaveFolderLabel = () -> {
            String saveFolder = coordinator.getSaveFolder();
            if (saveFolder != null && !saveFolder.isEmpty())
            {
                saveFolderLabel.setText("Folder containing test-suites/ and test-cases: " + saveFolder);
            }
            else
            {
                saveFolderLabel.setText("Folder containing test-suites/ and test-cases: Not set");
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

        // Button action: Opens folder browser to select folder for test cases and suites
        // Folder should contain test-suites/ and test-cases/ subfolders
        // Once selected, loads any existing test cases and suites from that folder
        browseSaveFolderButton.setOnAction(e -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Select Folder (contains test-suites/ and test-cases/)");
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
                showErrorDialog("No Suites Available", "No test suites found. Please create a test suite first or set the root folder.");
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
        // Saves the suite with all its test case references to the root folder
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
                showErrorDialog("Student Submissions Folder Not Set", "Please set the folder containing student submissions first.");
                return;
            }
            // Check if suite has any test cases
            if (coordinator.getCurrentTestSuite().getTestCaseFilenames().isEmpty())
            {
                showErrorDialog("Empty Test Suite", "The selected test suite has no test cases. Please add test cases to the suite before executing.");
                return;
            }
            showExecuteTestSuiteScreen();
        });

        backToStartButton.setOnAction(e -> {
            showWelcomeScreen();
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
        testCaseList.setStyle("-fx-background-color: #262634; -fx-control-inner-background: #262634; -fx-border-color: #3a3a5a; -fx-border-radius: 6; -fx-padding: 6; -fx-text-fill: #E8E8F2;");
        Button editCaseButton = new Button("Edit Selected Test Case");
        Button deleteCaseButton = new Button("Delete Selected Test Case");
        Button backButton = new Button("Back");
        Button restartButton = new Button("Restart from Beginning");

        // style buttons
        Button[] bset = {editCaseButton, deleteCaseButton, backButton, restartButton};
        for (Button b : bset) {
            styleButton(b);
        }

        VBox layout = new VBox(10,
                new Label("All Test Cases:"),
                testCaseList,
                new Separator(),
                editCaseButton,
                deleteCaseButton,
                backButton,
                restartButton
        );
        // style labels in this layout
        for (javafx.scene.Node node : layout.getChildren()) {
            if (node instanceof Label) {
                ((Label) node).setStyle("-fx-text-fill: #E8E8F2; -fx-font-weight: 600;");
            }
        }
        layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: linear-gradient(to bottom right, #1e1e2f, #2d2d44);");

        Scene scene = new Scene(layout, 900, 700);

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
    // Uses expandable content area for long messages to prevent text cutoff
    private void showErrorDialog(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Make the dialog expandable for long messages
        alert.getDialogPane().setExpandableContent(new javafx.scene.control.TextArea(message));
        alert.getDialogPane().setExpanded(true);
        
        // Set a minimum width to prevent cutoff
        alert.getDialogPane().setMinWidth(500);
        
        alert.showAndWait();
    }

    // Method to display the test suite execution configuration screen
    // Shows the root folder and allows user to specify the code path within each submission
    // (e.g., "src" if code is in a src subfolder). When execute is clicked, runs the test suite
    private void showExecuteTestSuiteScreen()
    {
        Coordinator coordinator = this.coordinator;

        Label rootFolderLabel = new Label("Student Submissions Folder: " + 
            (coordinator.getRootFolder() != null ? coordinator.getRootFolder() : "Not set"));
        TextField codePathField = new TextField();
        codePathField.setPromptText("e.g., src (leave empty if code is directly in submission folder)");
        Label codePathLabel = new Label("Code path within each submission folder:");

        Button executeButton = new Button("Execute Test Suite");
        Button backButton = new Button("Back");

        // styles
        rootFolderLabel.setStyle("-fx-text-fill: #E8E8F2;");
        codePathLabel.setStyle("-fx-text-fill: #E8E8F2;");
        codePathField.setStyle("-fx-background-color: #303046; -fx-text-fill: #E8E8F2; -fx-background-radius: 6; -fx-padding: 6 8;");
        styleButton(executeButton, "8 14");
        styleButton(backButton);

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
        // style header label
        ((Label) layout.getChildren().get(0)).setStyle("-fx-text-fill: #E8E8F2; -fx-font-weight: 600; -fx-font-size: 18;");

        layout.setStyle("-fx-padding: 30; -fx-alignment: center; -fx-background-color: linear-gradient(to bottom right, #1e1e2f, #2d2d44);");

        Scene scene = new Scene(layout, 800, 500);

        // Button action: Executes the test suite on all student submissions
        // Gets the code path (if specified) and triggers test execution
        executeButton.setOnAction(e -> {
            String codePath = codePathField.getText().trim();
            try
            {
                // Execute the test suite and get results
                List<TestResult> results = coordinator.executeTestSuite(codePath);
                showResultsScreen(results);
            }
            catch (Exception ex)
            {
                showErrorDialog("Execution Error", "Failed to execute test suite: " + ex.getMessage());
            }
        });

        backButton.setOnAction(e -> {
            showTestSuiteManagementScreen();
        });

        primaryStage.setScene(scene);
    }

    // Method to display the test execution results screen
    // Shows a list of all students with their test case results (PASSED/FAILED/COMPILE ERROR)
    // User can select a result to view detailed side-by-side comparison
    private void showResultsScreen(List<TestResult> results)
    {
        Coordinator coordinator = this.coordinator;
        TestSuite suite = coordinator.getCurrentTestSuite();

        Label titleLabel = new Label("Test Results for: " + suite.getTitle());
        titleLabel.setStyle("-fx-text-fill: #E8E8F2; -fx-font-weight: 600; -fx-font-size: 18;");
        
        // ListView to show students and their results
        ListView<String> resultsList = new ListView<>();
        resultsList.setPrefHeight(400);
        resultsList.setStyle("-fx-background-color: #262634; -fx-control-inner-background: #262634; -fx-border-color: #3a3a5a; -fx-border-radius: 6; -fx-padding: 6; -fx-text-fill: #E8E8F2;");

        Button viewComparisonButton = new Button("View Comparison (Selected)");
        Button saveAsButton = new Button("Save Results As...");
        Button backButton = new Button("Back");
        Button restartButton = new Button("Restart from Beginning");

        Button[] btns = {viewComparisonButton, saveAsButton, backButton, restartButton};
        for (Button b : btns) {
            styleButton(b);
        }

        VBox layout = new VBox(10,
                titleLabel,
                new Separator(),
                new Label("Results (Student - Test Case - Status):"),
                resultsList,
                new Separator(),
                viewComparisonButton,
                saveAsButton,
                backButton,
                restartButton
        );
        // style labels in layout
        for (javafx.scene.Node node : layout.getChildren()) {
            if (node instanceof Label) ((Label) node).setStyle("-fx-text-fill: #E8E8F2;");
        }
        layout.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom right, #1e1e2f, #2d2d44);");

        Scene scene = new Scene(layout, 1000, 700);

        // Display actual results from execution with dividers between different students
        String currentStudent = null;
        for (TestResult result : results)
        {
            String studentName = result.getStudentName();
            
            // Add divider when student changes (skip for first student)
            if (currentStudent != null && !currentStudent.equals(studentName))
            {
                resultsList.getItems().add("-----------------------------------");
            }
            
            resultsList.getItems().add(result.toDisplayString());
            currentStudent = studentName;
        }
        
        // Store results for comparison screen access
        final List<TestResult> resultsForComparison = new java.util.ArrayList<>(results);
        
        // Button action: Opens the side-by-side comparison screen for the selected result
        // Parses the result string to extract student name and test case title
        // Result format is "StudentName | TestCaseTitle | Status" or "-----------------------------------" for dividers
        viewComparisonButton.setOnAction(e -> {
            String selected = resultsList.getSelectionModel().getSelectedItem();
            if (selected != null && !selected.equals("-----------------------------------"))
            {
                // Parse selected item to get student and test case
                // Format: "StudentName | TestCaseTitle | Status" or "StudentName | Status" for skipped
                String[] parts = selected.split("\\|");
                if (parts.length >= 2)
                {
                    String studentName = parts[0].trim();
                    
                    // Check if this is a skipped entry (only 2 parts)
                    if (parts.length == 2)
                    {
                        // Skipped entry - show message that comparison is not available
                        showErrorDialog("No Comparison Available", "This entry was skipped (no main method found). Comparison is not available.");
                        return;
                    }
                    
                    // Normal entry with test case
                    String testCaseTitle = parts[1].trim();
                    
                    // Find the corresponding TestResult
                    TestResult result = null;
                    for (TestResult r : resultsForComparison)
                    {
                        if (r.getStudentName().equals(studentName) && 
                            r.getTestCaseTitle().equals(testCaseTitle))
                        {
                            result = r;
                            break;
                        }
                    }
                    
                    if (result != null)
                    {
                        showComparisonScreen(result);
                    }
                    else
                    {
                        showErrorDialog("Result Not Found", "Could not find result data.");
                    }
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

        // Button action: Saves the test results to a file
        saveAsButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Test Results As...");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("All Files", "*.*")
            );
            
            // Suggest a filename based on suite title and timestamp
            String suiteTitle = coordinator.getCurrentTestSuite().getTitle();
            String suggestedFilename = sanitizeFilename(suiteTitle) + "_results.txt";
            fileChooser.setInitialFileName(suggestedFilename);
            
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null)
            {
                try
                {
                    saveResultsToFile(results, file, suiteTitle);
                    showInfoDialog("Results Saved", "Test results have been saved to:\n" + file.getAbsolutePath());
                }
                catch (Exception ex)
                {
                    showErrorDialog("Save Error", "Failed to save results: " + ex.getMessage());
                }
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

    // Helper method to sanitize a string to be a valid filename
    private String sanitizeFilename(String name)
    {
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    // Helper method to repeat a string (for compatibility with older Java versions)
    private String repeatString(String str, int count)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++)
        {
            sb.append(str);
        }
        return sb.toString();
    }

    // Method to save test results to a file
    // Formats results with student names, test cases, and status
    private void saveResultsToFile(List<TestResult> results, File file, String suiteTitle) throws java.io.IOException
    {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(file)))
        {
            // Write header
            writer.println("Test Results for: " + suiteTitle);
            writer.println("Generated: " + new java.util.Date());
            writer.println(repeatString("=", 80));
            writer.println();
            
            // Group results by student
            String currentStudent = null;
            for (TestResult result : results)
            {
                String studentName = result.getStudentName();
                
                // Add divider when student changes
                if (currentStudent != null && !currentStudent.equals(studentName))
                {
                    writer.println(repeatString("-", 80));
                }
                
                // Write result line
                if (result.getStatus().startsWith("SKIPPED"))
                {
                    writer.println(studentName + " | " + result.getStatus());
                }
                else
                {
                    writer.println(studentName + " | " + result.getTestCaseTitle() + " | " + result.getStatus());
                }
                
                currentStudent = studentName;
            }
            
            // Write summary
            writer.println();
            writer.println(repeatString("=", 80));
            writer.println("Summary:");
            int total = results.size();
            long passed = results.stream().filter(r -> r.getStatus().equals("PASSED")).count();
            long failed = results.stream().filter(r -> r.getStatus().equals("FAILED")).count();
            long compileErrors = results.stream().filter(r -> r.getStatus().equals("COMPILE ERROR")).count();
            long runtimeErrors = results.stream().filter(r -> r.getStatus().equals("RUNTIME ERROR")).count();
            long skipped = results.stream().filter(r -> r.getStatus().startsWith("SKIPPED")).count();
            
            writer.println("Total Results: " + total);
            writer.println("Passed: " + passed);
            writer.println("Failed: " + failed);
            writer.println("Compile Errors: " + compileErrors);
            writer.println("Runtime Errors: " + runtimeErrors);
            writer.println("Skipped: " + skipped);
        }
    }

    // Method to display the side-by-side comparison screen
    // Shows expected output (from test case) and actual output (from program execution) side by side
    // Allows user to visually compare what was expected vs what the program actually produced
    private void showComparisonScreen(TestResult result)
    {
        Coordinator coordinator = this.coordinator;

        Label titleLabel = new Label("Comparison: " + result.getStudentName() + " - " + result.getTestCaseTitle());
        titleLabel.setStyle("-fx-text-fill: #E8E8F2; -fx-font-weight: 600; -fx-font-size: 18;");

        TextArea expectedArea = new TextArea();
        expectedArea.setEditable(false);
        expectedArea.setPrefRowCount(15);
        expectedArea.setPrefColumnCount(40);
        expectedArea.setWrapText(true);
        expectedArea.setStyle("-fx-control-inner-background: #262634; -fx-text-fill: #E8E8F2; -fx-background-radius: 6;");

        TextArea actualArea = new TextArea();
        actualArea.setEditable(false);
        actualArea.setPrefRowCount(15);
        actualArea.setPrefColumnCount(40);
        actualArea.setWrapText(true);
        actualArea.setStyle("-fx-control-inner-background: #262634; -fx-text-fill: #E8E8F2; -fx-background-radius: 6;");

        Label expectedLabel = new Label("Expected Output:");
        expectedLabel.setStyle("-fx-text-fill: #E8E8F2; -fx-font-weight: 600;");
        Label actualLabel = new Label("Actual Output:");
        actualLabel.setStyle("-fx-text-fill: #E8E8F2; -fx-font-weight: 600;");

        Button backButton = new Button("Back to Results");
        Button restartButton = new Button("Restart from Beginning");

        Button[] smallBtns = {backButton, restartButton};
        for (Button b : smallBtns) {
            styleButton(b);
        }

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
        layout.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom right, #1e1e2f, #2d2d44);");

        Scene scene = new Scene(layout, 1100, 600);

        // Display actual output data from TestResult
        String expectedOutput = result.getExpectedOutput();
        String actualOutput = result.getActualOutput();
        
        expectedArea.setText(expectedOutput != null ? expectedOutput : "");
        String status = result.getStatus();
        if (status.equals("COMPILE ERROR"))
        {
            actualArea.setText("Compilation failed - no output available");
        }
        else if (status.equals("RUNTIME ERROR"))
        {
            actualArea.setText((actualOutput != null && !actualOutput.isEmpty() ? actualOutput : "") + 
                "\n\n[Program exited with non-zero exit code]");
        }
        else if (status.startsWith("SKIPPED"))
        {
            // For skipped folders, show a clear message
            actualArea.setText("Folder \"" + result.getStudentName() + "\" didn't contain main method, skipped");
            expectedArea.setText("N/A - Folder was skipped");
        }
        else
        {
            actualArea.setText(actualOutput != null ? actualOutput : "");
        }

        backButton.setOnAction(e -> {
            // Always try to go back to results screen with stored results
            List<TestResult> storedResults = coordinator.getLastExecutionResults();
            if (storedResults != null && !storedResults.isEmpty())
            {
                showResultsScreen(storedResults);
            }
            else
            {
                // Fallback: if results aren't available, go to execute screen
                showErrorDialog("Results Not Available", "Test results are no longer available. Please re-run the test suite.");
                showExecuteTestSuiteScreen();
            }
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

        titleField.setStyle("-fx-background-color: #303046; -fx-text-fill: #E8E8F2; -fx-background-radius: 6;");
        inputField.setStyle("-fx-background-color: #303046; -fx-text-fill: #E8E8F2; -fx-background-radius: 6;");
        expectedField.setStyle("-fx-background-color: #303046; -fx-text-fill: #E8E8F2; -fx-background-radius: 6;");

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

        // style labels inside dialog content
        for (javafx.scene.Node node : content.getChildren()) {
            if (node instanceof Label) ((Label) node).setStyle("-fx-text-fill: #E8E8F2;");
        }
        content.setStyle("-fx-padding: 10; -fx-background-color: #232334;");

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

    // Helper method to apply consistent button styling with hover effects
    // Standard button style with default padding (6 12)
    private void styleButton(Button button)
    {
        styleButton(button, "6 12");
    }

    // Helper method to apply consistent button styling with hover effects
    // Allows custom padding (e.g., "8 14", "10 20")
    private void styleButton(Button button, String padding)
    {
        String normalStyle = "-fx-background-color: linear-gradient(#6a5acd,#4c4cff); -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: " + padding + ";";
        String hoverStyle = "-fx-background-color: linear-gradient(#7b68ee,#6f6fff); -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: " + padding + "; -fx-effect: dropshadow(gaussian, rgba(111,111,255,0.45),10,0,0,0);";
        
        button.setStyle(normalStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(normalStyle));
    }

    // Helper method for buttons with bold font weight (like the welcome screen start button)
    private void styleButtonBold(Button button, String padding)
    {
        String normalStyle = "-fx-background-color: linear-gradient(#6a5acd,#4c4cff); -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: " + padding + "; -fx-font-weight: bold;";
        String hoverStyle = "-fx-background-color: linear-gradient(#7b68ee,#6f6fff); -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: " + padding + "; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(111,111,255,0.55), 12,0,0,0);";
        
        button.setStyle(normalStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(normalStyle));
    }

}
