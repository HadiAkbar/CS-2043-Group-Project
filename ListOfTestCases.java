import java.util.ArrayList;
import java.util.List;

public class ListOfTestCases
{
    private List<TestCase> testCases;

    public ListOfTestCases()
    {
        testCases = new ArrayList<>();
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

    public TestCase getTestCaseByTitle(String title)
    {
        return testCases.stream()
                .filter(tc -> tc.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }

    public void clear()
    {
        testCases.clear();
    }
}
