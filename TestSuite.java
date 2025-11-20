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

    public void addTestCase(TestCase testCase)
    {
        testCases.add(testCase);
    }

    public void removeTestCase(TestCase testCase)
    {
        testCases.remove(testCase);
    }

    public List<TestCase> getTestCases()
    {
        return testCases;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
