import java.util.ArrayList;
import java.util.List;

public class TestSuite
{
    private String title;
    private List<TestCase> testCases;

    public TestSuite(String title)
    {
        this.title = title;
        this.testCases = new ArrayList<>();
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public List<TestCase> getTestCases()
    {
        return testCases;
    }

    public void addTestCase(TestCase tc)
    {
        if (tc != null && !testCases.contains(tc))
        {
            testCases.add(tc);
        }
    }

    public void removeTestCase(TestCase tc)
    {
        testCases.remove(tc);
    }
}
