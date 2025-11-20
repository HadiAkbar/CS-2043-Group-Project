public class TestCase
{
    private String title;
    private String inputData;
    private String expectedOutput;

    public TestCase(String title, String inputData, String expectedOutput)
    {
        this.title = title;
        this.inputData = inputData;
        this.expectedOutput = expectedOutput;
    }

    // Getters
    public String getTitle() 
    { 
        return title; 
    }
    public String getInputData() { return inputData; 

    }
    public String getExpectedOutput() { return expectedOutput; 
        
    }

    // Setters
    public void setTitle(String title) 
    { 
        this.title = title; 
    }
    public void setInputData(String inputData) 
    { 
        this.inputData = inputData; 
    }
    public void setExpectedOutput(String expectedOutput) 
    { 
        this.expectedOutput = expectedOutput; 
    }
}
