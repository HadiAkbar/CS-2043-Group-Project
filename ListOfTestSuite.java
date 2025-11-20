import java.util.ArrayList;
import java.util.List;

public class ListOfTestSuite
{
    private List<TestSuite> suites;

    public ListOfTestSuite()
    {
        this.suites = new ArrayList<>();
    }

    public void addTestSuite(TestSuite suite)
    {
        suites.add(suite);
    }

    public void removeTestSuite(TestSuite suite)
    {
        suites.remove(suite);
    }

    public List<TestSuite> getSuites()
    {
        return suites;
    }

    // Version 1: return the single suite
    public TestSuite getFirstSuite()
    {
        if (!suites.isEmpty()) return suites.get(0);
        return null;
    }
}
