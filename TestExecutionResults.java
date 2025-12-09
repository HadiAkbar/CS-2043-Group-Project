import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores ALL results of running an entire test suite against all students' programs.
 * 
 * This object is saved using serialization so we can:
 *  - reload results later,
 *  - compare first submission vs second submission,
 *  - avoid recomputing results every time.
 *
 * A TestExecutionResults object represents ONE full run of the test suite.
 */
public class TestExecutionResults implements Serializable
{
    private static final long serialVersionUID = 1L;

    // --- Metadata about this execution run ---
    private String testSuiteTitle;   // Name of the test suite that was executed
    private String rootFolderPath;   // Path to the folder containing student subfolders
    private String codePath;         // Path inside each student folder where code files are located

    // The full list of individual test results (one per student per test case)
    private List<TestResult> results;

    // The number of test cases in the suite (calculated automatically)
    private int totalTestCases;

    /**
     * Creates a TestExecutionResults object containing metadata and the full list of results.
     *
     * @param testSuiteTitle  Name of the suite that was run
     * @param rootFolderPath  Student submissions folder
     * @param codePath        Directory inside each student folder containing their code
     * @param results         List of all TestResult objects produced during execution
     */
    public TestExecutionResults(String testSuiteTitle, String rootFolderPath,
                                String codePath, List<TestResult> results)
    {
        this.testSuiteTitle = testSuiteTitle;
        this.rootFolderPath = rootFolderPath;
        this.codePath = codePath;

        // Make a defensive copy so the original list cannot modify this object from outside
        this.results = new ArrayList<>(results);

        /**
         * Determine the number of UNIQUE test cases.
         * Example:
         *   If 30 students and 5 test cases → results list contains 150 entries.
         *   But we want totalTestCases = 5.
         *
         * We count distinct testCaseTitle values.
         */
        this.totalTestCases = (int) results.stream()
                .map(TestResult::getTestCaseTitle)
                .distinct()
                .count();
    }

    // --- Getters for other components of the program ---

    public String getTestSuiteTitle() { return testSuiteTitle; }
    public String getRootFolderPath() { return rootFolderPath; }
    public String getCodePath() { return codePath; }

    /**
     * Returns a COPY of the results list so callers cannot modify the internal list.
     */
    public List<TestResult> getResults() { return new ArrayList<>(results); }

    public int getTotalTestCases() { return totalTestCases; }
}
