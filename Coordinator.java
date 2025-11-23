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

    // Loads all .testcase files from the test-cases folder
    /* Additional: Scans saved test-case folder and loads all .testcase files into memory. */
    private void loadTestCasesFromFolder()
    {
        if (saveFolder == null || saveFolder.isEmpty())
        {
            return;
        }

        File testCasesFolder = new File(saveFolder, "test-cases");
        if (!testCasesFolder.exists() || !testCasesFolder.isDirectory())
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

        File suitesFolder = new File(saveFolder, "test-suites");
        if (!suitesFolder.exists() || !suitesFolder.isDirectory())
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

    // Executes the current test suite on all programs in the root folder
    /* Additional: This will run every program against every test case and gather formatted results. */
    public List<String> executeTestSuite(String codePath) throws IOException
    {
        if (currentTestSuite == null)
        {
            throw new IOException("No test suite selected");
        }
        if (rootFolder == null || rootFolder.isEmpty())
        {
            throw new IOException("Root folder not set");
        }

        List<String> results = new ArrayList<>();
        return results; // Execution logic will be added later
    }

    // Compiles a Java program and returns true if compilation succeeds
    /* Additional: Builds a javac command to compile student's Java source file. */
    private boolean compileProgram(Program program)
    {
        try
        {
            File sourceFile = program.getSourceFile();
            File sourceDir = sourceFile.getParentFile();

            ProcessBuilder pb = new ProcessBuilder("javac", sourceFile.getName());
            pb.directory(sourceDir);
            pb.redirectErrorStream(true);

            Process process = pb.start();
            int exitCode = process.waitFor();

            return exitCode == 0;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
