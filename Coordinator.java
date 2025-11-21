import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Coordinator
{
    private String rootFolder; // For student submissions
    private String saveFolder; // For saving test cases and suites
    private ListOfTestSuites listOfTestSuites;
    private ListOfTestCases listOfTestCases; // Global pool of test cases
    private ListOfPrograms listOfPrograms; // List of student programs to test
    private TestSuite currentTestSuite;

    public Coordinator()
    {
        this.listOfTestSuites = new ListOfTestSuites();
        this.listOfTestCases = new ListOfTestCases();
        this.listOfPrograms = new ListOfPrograms();
        this.currentTestSuite = null;
    }

    public String getRootFolder()
    {
        return rootFolder;
    }

    public void setRootFolder(String rootFolder)
    {
        this.rootFolder = rootFolder;
    }

    public String getSaveFolder()
    {
        return saveFolder;
    }

    public void setSaveFolder(String saveFolder)
    {
        this.saveFolder = saveFolder;
        // Load existing test cases and test suites from the folder
        loadTestCasesFromFolder();
        loadTestSuitesFromFolder();
    }

    public void createTestSuite(String title)
    {
        TestSuite suite = new TestSuite(title);
        listOfTestSuites.addSuite(suite);
        currentTestSuite = suite;
    }

    public void saveTestSuite(TestSuite suite) throws IOException
    {
        if (saveFolder == null || saveFolder.isEmpty())
        {
            throw new IOException("Save folder not set");
        }
        suite.saveToFile(saveFolder);
    }

    // Method to load a test suite - currently loads the first available suite
    public void loadTestSuite()
    {
        if (!listOfTestSuites.getSuites().isEmpty())
        {
            currentTestSuite = listOfTestSuites.getSuites().get(0);
        }
    }

    public List<TestSuite> getAllTestSuites()
    {
        return listOfTestSuites.getSuites();
    }

    public void loadTestSuiteFromFile(File suiteFile) throws IOException
    {
        TestSuite suite = TestSuite.loadFromFile(suiteFile);
        listOfTestSuites.addSuite(suite);
        currentTestSuite = suite;
    }

    public TestSuite getCurrentTestSuite()
    {
        return currentTestSuite;
    }

    public void setCurrentTestSuite(TestSuite suite)
    {
        this.currentTestSuite = suite;
    }

    public ListOfTestSuites getListOfTestSuites()
    {
        return listOfTestSuites;
    }

    public ListOfTestCases getListOfTestCases()
    {
        return listOfTestCases;
    }

    public void createAndSaveTestCase(TestCase testCase) throws IOException
    {
        if (saveFolder == null || saveFolder.isEmpty())
        {
            throw new IOException("Save folder not set");
        }
        testCase.saveToFile(saveFolder);
        listOfTestCases.addTestCase(testCase);
    }

    public TestCase getTestCaseByFilename(String filename)
    {
        return listOfTestCases.getTestCases().stream()
                .filter(tc -> tc.getFilename().equals(filename))
                .findFirst()
                .orElse(null);
    }

    // Load all test cases from the test-cases folder
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

    // Load all test suites from the test-suites folder
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

    // Get all test case filenames available
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
    // Loads programs from root folder, compiles and runs each with all test cases
    // Returns a list of result strings in format "StudentName | TestCaseTitle | Status"
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
        // Implementation will be added in subsequent commits
        return results;
    }

    // Helper method to compile a Java program
    // Returns true if compilation succeeds, false otherwise
    private boolean compileProgram(Program program)
    {
        try
        {
            File sourceFile = program.getSourceFile();
            File sourceDir = sourceFile.getParentFile();
            
            // Build javac command
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
