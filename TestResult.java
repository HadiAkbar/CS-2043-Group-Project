public class TestResult
{
    private String studentName;
    private String testCaseTitle;
    private String status; // PASSED, FAILED, COMPILE ERROR, RUNTIME ERROR, or SKIPPED
    private String actualOutput;
    private String expectedOutput;

    public TestResult(String studentName, String testCaseTitle, String status, String actualOutput, String expectedOutput)
    {
        this.studentName = studentName;
        this.testCaseTitle = testCaseTitle;
        this.status = status;
        this.actualOutput = actualOutput;
        this.expectedOutput = expectedOutput;
    }

    public String getStudentName() { return studentName; }
    public String getTestCaseTitle() { return testCaseTitle; }
    public String getStatus() { return status; }
    public String getActualOutput() { return actualOutput; }
    public String getExpectedOutput() { return expectedOutput; }

    // Format for display in results list
    public String toDisplayString()
    {
        if (status.startsWith("SKIPPED"))
        {
            // For skipped entries, show a clearer message
            return studentName + " | " + status;
        }
        return studentName + " | " + testCaseTitle + " | " + status;
    }
}

