
import java.util.ArrayList;
import java.util.List;

public class ListOfTestCases
{
    private List<TestCase> testCases; // Stores all TestCase objects

    /**
     * Constructor: Initializes internal list to keep track of all created or loaded test cases.
     */
    public ListOfTestCases()
    {
        testCases = new ArrayList<>();
    }

    /**
     * Adds a test case to the list for use in building suites or running tests.
     */
    public void addTestCase(TestCase testCase)
    {
        testCases.add(testCase);
    }

    /**
     * Removes a specific test case from the list if no longer needed.
     */
    public void removeTestCase(TestCase testCase)
    {
        testCases.remove(testCase);
    }

    /**
     * Returns full list of stored test cases for UI population or processing.
     */
    public List<TestCase> getTestCases()
    {
        return testCases;
    }

    /**
     * Searches for a test case by its title, returning the first matching one.
     * Uses a stream for clean, readable filtering.
     */
    public TestCase getTestCaseByTitle(String title)
    {
        return testCases.stream()
                .filter(tc -> tc.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }

    /**
     * Clears all stored test cases—useful when loading a new dataset from disk.
     */
    public void clear()
    {
        testCases.clear();
    }
}
