public class TestCase
{
    private String title;
    private String inputData;
    private String expectedOutput;
    private String type;

    public TestCase(String title, String inputData, String expectedOutput, String type)
    {
        this.title = title;
        this.inputData = inputData;
        this.expectedOutput = expectedOutput;
        this.type = type;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getInputData()
    {
        return inputData;
    }

    public void setInputData(String inputData)
    {
        this.inputData = inputData;
    }

    public String getExpectedOutput()
    {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput)
    {
        this.expectedOutput = expectedOutput;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
