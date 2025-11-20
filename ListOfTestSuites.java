import java.util.ArrayList;
import java.util.List;

public class ListOfTestSuites
{
    private List<TestSuite> suites;

    public ListOfTestSuites()
    {
        this.suites = new ArrayList<>();
    }

    public List<TestSuite> getSuites()
    {
        return suites;
    }

    public void addSuite(TestSuite suite)
    {
        if (suite != null && !suites.contains(suite))
        {
            suites.add(suite);
        }
    }

    public void removeSuite(TestSuite suite)
    {
        suites.remove(suite);
    }

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
