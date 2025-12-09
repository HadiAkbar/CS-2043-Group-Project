
import java.util.ArrayList;
import java.util.List;

public class ListOfTestSuites
{
    private List<TestSuite> suites; // Stores all loaded test suites

    /**
     * Constructor: Initializes the internal list to hold TestSuite objects.
     * Ensures the class starts empty and ready to add test suites.
     */
    public ListOfTestSuites()
    {
        this.suites = new ArrayList<>();
    }

    /**
     * Returns the list of all test suites.
     * Useful for iterating over suites or populating UI components.
     */
    public List<TestSuite> getSuites()
    {
        return suites;
    }

    /**
     * Adds a test suite to the collection if it is not null and not already present.
     * Prevents duplicate entries and ensures safe addition.
     */
    public void addSuite(TestSuite suite)
    {
        if (suite != null && !suites.contains(suite))
        {
            suites.add(suite);
        }
    }

    /**
     * Removes a specific test suite from the collection.
     * Useful for deleting outdated or incorrect test suites.
     */
    public void removeSuite(TestSuite suite)
    {
        suites.remove(suite);
    }

    /**
     * Finds a test suite by its title.
     * Returns the first suite that matches the title, or null if none found.
     */
    public TestSuite findSuiteByTitle(String title)
    {
        for (TestSuite suite : suites)
        {
            if (suite.getTitle().equals(title))
            {
                return suite;
            }
        }
        return null;
    }
}
