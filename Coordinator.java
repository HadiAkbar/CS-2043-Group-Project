// Coordinator class manages test cases, test suites, and student programs
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Coordinator
{
    private String rootFolder; // Path where student submissions are stored
    private String saveFolder; // Path where test cases and suites are saved
    private ListOfTestSuites listOfTestSuites; // Holds all loaded/created test suites
    private ListOfTestCases listOfTestCases; // Global pool of available test cases
    private ListOfPrograms listOfPrograms; // List of student programs to test
    private TestSuite currentTestSuite; // The test suite currently selected

    // Constructor initializes lists and sets no current test suite
    /* Additional: Initializes internal lists so Coordinator starts in a clean state. */
    public Coordinator()
    {
        this.listOfTestSuites = new ListOfTestSuites();
        this.listOfTestCases = new ListOfTestCases();
        this.listOfPrograms = new ListOfPrograms();
        this.currentTestSuite = null;
    }

    // Returns the folder containing student submissions
    /* Additional: Getter used when accessing where student submissions are stored. */
    public String getRootFolder()
    {
        return rootFolder;
    }

    // Sets the folder containing student submissions
    /* Additional: Updates the directory path where all student code is located. */
    public void setRootFolder(String rootFolder)
    {
        this.rootFolder = rootFolder;
    }

    // Returns the save folder path
    /* Additional: Returns where all test definitions (cases + suites) are saved on disk. */
    public String getSaveFolder()
    {
        return saveFolder;
    }

    // Sets the save folder path and loads existing test cases and suites
    /* Additional: Sets save folder and immediately attempts to preload all saved test data. */
    public void setSaveFolder(String saveFolder)
    {
        this.saveFolder = saveFolder;
        loadTestCasesFromFolder(); // Load .testcase files
        loadTestSuitesFromFolder(); // Load .suite files
    }

    // Creates a new test suite and sets it as the current one
    /* Additional: Creates a new test suite container for grouping test cases. */
    public void createTestSuite(String title)
    {
        TestSuite suite = new TestSuite(title);
        listOfTestSuites.addSuite(suite);
        currentTestSuite = suite;
    }

    // Saves a given test suite to the save folder
    /* Additional: Serializes a TestSuite object into a file so it persists between sessions. */
    public void saveTestSuite(TestSuite suite) throws IOException
    {
        if (saveFolder == null || saveFolder.isEmpty())
        {
            throw new IOException("Save folder not set");
        }
        suite.saveToFile(saveFolder);
    }

    // Loads the first available test suite and sets it as current
    /* Additional: Convenience method to quickly pick the first loaded suite as the active one. */
    public void loadTestSuite()
    {
        if (!listOfTestSuites.getSuites().isEmpty())
        {
            currentTestSuite = listOfTestSuites.getSuites().get(0);
        }
    }

    // Returns all test suites
    /* Additional: Allows UI or caller to retrieve all known test suites. */
    public List<TestSuite> getAllTestSuites()
    {
        return listOfTestSuites.getSuites();
    }

    // Loads a single test suite from a file and sets it as current
    /* Additional: Loads a suite from disk and makes it instantly usable. */
    public void loadTestSuiteFromFile(File suiteFile) throws IOException
    {
        TestSuite suite = TestSuite.loadFromFile(suiteFile);
        listOfTestSuites.addSuite(suite);
        currentTestSuite = suite;
    }

    // Returns the currently selected test suite
    /* Additional: Returns whichever suite the instructor/user is currently working on. */
    public TestSuite getCurrentTestSuite()
    {
        return currentTestSuite;
    }

    // Sets the currently active test suite
    /* Additional: Manually switches the working suite to another chosen one. */
    public void setCurrentTestSuite(TestSuite suite)
    {
        this.currentTestSuite = suite;
    }

    // Returns list of all test suites
    /* Additional: Gives direct access to underlying TestSuite collection wrapper. */
    public ListOfTestSuites getListOfTestSuites()
    {
        return listOfTestSuites;
    }

    // Returns list of all test cases
    /* Additional: Gives access to global pool of test cases available for suite building. */
    public ListOfTestCases getListOfTestCases()
    {
        return listOfTestCases;
    }

    // Saves a test case to file and adds it to the global list
    /* Additional: Adds a new test case to storage AND registers it in memory for immediate use. */
    public void createAndSaveTestCase(TestCase testCase) throws IOException
    {
        if (saveFolder == null || saveFolder.isEmpty())
        {
            throw new IOException("Save folder not set");
        }
        testCase.saveToFile(saveFolder);
        listOfTestCases.addTestCase(testCase);
    }

    // Returns a test case by its filename, or null if not found
    /* Additional: Uses stream filtering to find a test case matching its stored filename. */
    public TestCase getTestCaseByFilename(String filename)
    {
        return listOfTestCases.getTestCases().stream()
                .filter(tc -> tc.getFilename().equals(filename))
                .findFirst()
                .orElse(null);
    }

    // Helper method to find a folder by name case-insensitively
    // Returns the actual folder File if found, or null if not found
    private File findFolderCaseInsensitive(File parentFolder, String folderName)
    {
        if (parentFolder == null || !parentFolder.exists() || !parentFolder.isDirectory())
        {
            return null;
        }
        
        File[] files = parentFolder.listFiles(File::isDirectory);
        if (files != null)
        {
            for (File file : files)
            {
                if (file.getName().equalsIgnoreCase(folderName))
                {
                    return file;
                }
            }
        }
        return null;
    }

    // Loads all .testcase files from the test-cases folder
    /* Additional: Scans saved test-case folder and loads all .testcase files into memory. */
    private void loadTestCasesFromFolder()
    {
        if (saveFolder == null || saveFolder.isEmpty())
        {
            return;
        }

        File saveFolderFile = new File(saveFolder);
        File testCasesFolder = findFolderCaseInsensitive(saveFolderFile, "test-cases");
        if (testCasesFolder == null || !testCasesFolder.exists() || !testCasesFolder.isDirectory())
        {
            return;
        }

        File[] files = testCasesFolder.listFiles((dir, name) -> name.endsWith(".testcase"));
        if (files != null)
        {
            for (File file : files)
            {
                try
                {
                    TestCase testCase = TestCase.loadFromFile(file);
                    listOfTestCases.addTestCase(testCase);
                }
                catch (IOException e)
                {
                    System.err.println("Error loading test case: " + file.getName() + " - " + e.getMessage());
                }
            }
        }
    }

    // Loads all .suite files from the test-suites folder
    /* Additional: Scans saved suite folder to restore previously created test suites. */
    private void loadTestSuitesFromFolder()
    {
        if (saveFolder == null || saveFolder.isEmpty())
        {
            return;
        }

        File saveFolderFile = new File(saveFolder);
        File suitesFolder = findFolderCaseInsensitive(saveFolderFile, "test-suites");
        if (suitesFolder == null || !suitesFolder.exists() || !suitesFolder.isDirectory())
        {
            return;
        }

        File[] files = suitesFolder.listFiles((dir, name) -> name.endsWith(".suite"));
        if (files != null)
        {
            for (File file : files)
            {
                try
                {
                    TestSuite suite = TestSuite.loadFromFile(file);
                    listOfTestSuites.addSuite(suite);
                }
                catch (IOException e)
                {
                    System.err.println("Error loading test suite: " + file.getName() + " - " + e.getMessage());
                }
            }
        }
    }

    // Returns a list of filenames for all available test cases
    /* Additional: Used to populate dropdowns or selection lists in UI. */
    public List<String> getAvailableTestCaseFilenames()
    {
        List<String> filenames = new ArrayList<>();
        for (TestCase tc : listOfTestCases.getTestCases())
        {
            filenames.add(tc.getFilename());
        }
        return filenames;
    }

    // Method to execute a test suite on all student programs
    // Loads programs from root folder, coordinates execution
    // Returns a list of TestResult objects containing execution results
    public List<TestResult> executeTestSuite(String codePath) throws IOException
    {
        if (currentTestSuite == null)
        {
            throw new IOException("No test suite selected");
        }
        if (rootFolder == null || rootFolder.isEmpty())
        {
            throw new IOException("Root folder not set");
        }

        List<TestResult> results = new ArrayList<>();
        
        // Load all student programs from root folder (which directly contains student submission folders)
        File rootFolderFile = new File(rootFolder);
        if (!rootFolderFile.exists() || !rootFolderFile.isDirectory())
        {
            throw new IOException("Root folder does not exist or is not a directory: " + rootFolder);
        }
        
        listOfPrograms.loadFromRootFolder(rootFolderFile, codePath);
        
        // Check if any programs were found
        if (listOfPrograms.getPrograms().isEmpty())
        {
            throw new IOException("No student programs found in root folder. Please check that the root folder contains student submission subfolders, each with a Java file containing a main method.");
        }
        
        // Get all test cases in the current suite
        List<TestCase> testCases = new ArrayList<>();
        for (String filename : currentTestSuite.getTestCaseFilenames())
        {
            TestCase tc = getTestCaseByFilename(filename);
            if (tc != null)
            {
                testCases.add(tc);
            }
        }
        
        // Check if any test cases were found
        if (testCases.isEmpty())
        {
            throw new IOException("No test cases found in the selected test suite. Please add test cases to the suite first.");
        }
        
        // For each program, test with each test case
        for (Program program : listOfPrograms.getPrograms())
        {
            for (TestCase testCase : testCases)
            {
                // Delegate execution to Program class
                TestResult result = program.executeTestCase(testCase);
                results.add(result);
            }
        }
        
        // Add entries for skipped folders (no main method found)
        List<String> skippedFolders = listOfPrograms.getSkippedFolders();
        for (String folderName : skippedFolders)
        {
            // Create a TestResult entry for each skipped folder
            // Use a special status to indicate it was skipped
            TestResult skippedResult = new TestResult(
                folderName,
                "N/A",
                "SKIPPED - NO MAIN METHOD",
                "",
                ""
            );
            results.add(skippedResult);
        }
        
        // Store results for UI retrieval (create a copy to prevent modification)
        lastExecutionResults = new ArrayList<>(results);
        
        return results;
    }

    // Store last execution results for UI retrieval
    private List<TestResult> lastExecutionResults = new ArrayList<>();
    
    public List<TestResult> getLastExecutionResults()
    {
        return lastExecutionResults;
    }
    
    // Get a specific test result by student name and test case title
    public TestResult getTestResult(String studentName, String testCaseTitle)
    {
        for (TestResult result : lastExecutionResults)
        {
            if (result.getStudentName().equals(studentName) && 
                result.getTestCaseTitle().equals(testCaseTitle))
            {
                return result;
            }
        }
        return null;
    }

    // Returns list of folder names that were skipped during program loading (no main method found)
    // Additional: Useful for informing the user which student submissions couldn't be tested
    public List<String> getSkippedFolders()
    {
        return listOfPrograms.getSkippedFolders();
    }
}
