import java.io.*;
import java.nio.file.Files;

public class TestCase
{
    private String title; // Title of the test case
    private String inputData; // Input data to provide to the program
    private String expectedOutput; // Expected output to compare against program output
    private String type; // Type/category of test case

    /**
     * Constructor: Initializes all fields of the test case.
     * Used to create new test cases programmatically or from files.
     */
    public TestCase(String title, String inputData, String expectedOutput, String type)
    {
        this.title = title;
        this.inputData = inputData;
        this.expectedOutput = expectedOutput;
        this.type = type;
    }

    // Getter and setter methods for all fields
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getInputData() { return inputData; }
    public void setInputData(String inputData) { this.inputData = inputData; }

    public String getExpectedOutput() { return expectedOutput; }
    public void setExpectedOutput(String expectedOutput) { this.expectedOutput = expectedOutput; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    /**
     * Get the filename for this test case based on its title.
     * Sanitizes title to remove invalid characters for filenames.
     */
    public String getFilename()
    {
        return sanitizeFilename(title) + ".testcase";
    }

    /**
     * Save test case to a file under rootFolder/test-cases (case-insensitive folder lookup).
     * Creates the folder if it does not exist, writes fields in order.
     */
    public void saveToFile(String rootFolder) throws IOException
    {
        File rootFolderFile = new File(rootFolder);
        File testCasesFolder = findFolderCaseInsensitive(rootFolderFile, "test-cases");
        if (testCasesFolder == null)
        {
            // Folder doesn't exist, create it with standard name
            testCasesFolder = new File(rootFolder, "test-cases");
            testCasesFolder.mkdirs();
        }

        File testCaseFile = new File(testCasesFolder, getFilename());

        try (PrintWriter writer = new PrintWriter(new FileWriter(testCaseFile)))
        {
            writer.println(title);
            writer.println(type);
            writer.println(inputData);
            writer.println(expectedOutput);
        }
    }

    /**
     * Load test case from a file.
     * Reads all lines and expects at least 4 lines in proper order.
     */
    public static TestCase loadFromFile(File testCaseFile) throws IOException
    {
        java.util.List<String> lines = Files.readAllLines(testCaseFile.toPath());
        if (lines.size() < 4)
        {
            throw new IOException("Invalid test case file format");
        }
         // Trim each field to remove extra spaces or newline characters.
         // This prevents formatting issues if the .testcase file contains trailing spaces.
          return new TestCase(
            lines.get(0).trim(), // title
            lines.get(2).trim(), // inputData
            lines.get(3).trim(), // expectedOutput
            lines.get(1).trim()  // type
       );

    }

    // Helper method to find a folder by name case-insensitively
    // Returns the actual folder File if found, or null if not found
    private static File findFolderCaseInsensitive(File parentFolder, String folderName)
    {
        if (parentFolder == null || !parentFolder.exists() || !parentFolder.isDirectory())
        {
            return null;
        }
        
        File[] files = parentFolder.listFiles(File::isDirectory);
        if (files != null)
        {
            for (File file : files)
            {
                if (file.getName().equalsIgnoreCase(folderName))
                {
                    return file;
                }
            }
        }
        return null;
    }

    /**
     * Helper method to sanitize a string to be a valid filename.
     * Replaces any character not allowed in filenames with underscore.
     */
    private String sanitizeFilename(String name)
    {
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
