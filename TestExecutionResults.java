import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to store test execution results for serialization
 * Contains all test results along with metadata about the execution
 */
public class TestExecutionResults implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private String testSuiteTitle;
    private String rootFolderPath;
    private String codePath;
    private List<TestResult> results;
    private int totalTestCases;
    
    public TestExecutionResults(String testSuiteTitle, String rootFolderPath, String codePath, List<TestResult> results, int totalTestCases)
    {
        this.testSuiteTitle = testSuiteTitle;
        this.rootFolderPath = rootFolderPath;
        this.codePath = codePath;
        this.results = new ArrayList<>(results);
        // Use the provided total test cases count from the suite
        // This ensures accuracy even if some tests weren't executed or results are missing
        this.totalTestCases = totalTestCases;
    }
    
    public String getTestSuiteTitle() { return testSuiteTitle; }
    public String getRootFolderPath() { return rootFolderPath; }
    public String getCodePath() { return codePath; }
    public List<TestResult> getResults() { return new ArrayList<>(results); }
    public int getTotalTestCases() { return totalTestCases; }
}

