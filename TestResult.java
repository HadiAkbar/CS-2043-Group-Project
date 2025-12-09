import java.io.Serializable;

/**
 * Represents the result of running ONE test case for ONE student.
 * This class is Serializable so we can save the results to a file
 * and reload them later (for comparison between submissions).
 */
public class TestResult implements Serializable
{
    // Version ID for serialization (prevents problems if the class changes later)
    private static final long serialVersionUID = 1L;
    
    // --- Instance Variables (one result holds these pieces of information) ---
    
    private String studentName;      // Name of the student whose program was tested
    private String testCaseTitle;    // The name of the test case that was run
    private String status;           // PASSED, FAILED, COMPILE ERROR, RUNTIME ERROR, SKIPPED
    private String actualOutput;     // What the student's program produced
    private String expectedOutput;   // What the program SHOULD have produced

    /**
     * Constructor: creates a new TestResult object with all required information.
     */
    public TestResult(String studentName, String testCaseTitle, String status,
                      String actualOutput, String expectedOutput)
    {
        this.studentName = studentName;
        this.testCaseTitle = testCaseTitle;
        this.status = status;
        this.actualOutput = actualOutput;
        this.expectedOutput = expectedOutput;
    }

    // --- Getter Methods (used by UI and other classes) ---

    public String getStudentName() { return studentName; }
    public String getTestCaseTitle() { return testCaseTitle; }
    public String getStatus() { return status; }
    public String getActualOutput() { return actualOutput; }
    public String getExpectedOutput() { return expectedOutput; }

    /**
     * Returns a simplified string for displaying the result in lists.
     * Example Outputs:
     *   "Alice | Test1 | PASSED"
     *   "Bob | SKIPPED - missing file"
     */
    public String toDisplayString()
    {
        // If the test case was skipped, show only student + skip reason
        if (status.startsWith("SKIPPED"))
        {
            return studentName + " | " + status;
        }

        // Normal display format for passed/failed/errors
        return studentName + " | " + testCaseTitle + " | " + status;
    }
}
