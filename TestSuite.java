import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.file.Files;

public class TestSuite
{
    private String title; // Title of the test suite
    private List<String> testCaseFilenames; // Stores filenames of test cases included in the suite

    /**
     * Constructor: Initializes a TestSuite with a title and empty list of test case filenames.
     * Prepares suite for adding test cases and saving/loading.
     */
    public TestSuite(String title)
    {
        this.title = title;
        this.testCaseFilenames = new ArrayList<>();
    }

    // Getter and setter for suite title
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    // Returns the list of test case filenames
    public List<String> getTestCaseFilenames() { return testCaseFilenames; }

    /**
     * Add a test case filename if not null, empty, or duplicate.
     * Maintains unique references for each suite.
     */
    public void addTestCaseFilename(String filename)
    {
        if (filename != null && !filename.isEmpty() && !testCaseFilenames.contains(filename))
        {
            testCaseFilenames.add(filename);
        }
    }

    // Remove a test case filename from the suite
    public void removeTestCaseFilename(String filename)
    {
        testCaseFilenames.remove(filename);
    }

    /**
     * Save this test suite to a file in rootFolder/test-suites (case-insensitive folder lookup).
     * Creates folder if necessary and writes title + list of test case filenames.
     */
    public void saveToFile(String rootFolder) throws IOException
    {
        File rootFolderFile = new File(rootFolder);
        File suitesFolder = findFolderCaseInsensitive(rootFolderFile, "test-suites");
        if (suitesFolder == null)
        {
            // Folder doesn't exist, create it with standard name
            suitesFolder = new File(rootFolder, "test-suites");
            suitesFolder.mkdirs(); // Ensure folder exists
        }

        String filename = sanitizeFilename(title) + ".suite";
        File suiteFile = new File(suitesFolder, filename);

        try (PrintWriter writer = new PrintWriter(new FileWriter(suiteFile)))
        {
            writer.println(title); // Write title as first line
            for (String testCaseFilename : testCaseFilenames)
            {
                writer.println(testCaseFilename); // Write each test case filename
            }
        }
    }

    /**
     * Load a test suite from a file.
     * Reads title from first line, remaining lines are test case filenames.
     */
    public static TestSuite loadFromFile(File suiteFile) throws IOException
    {
        List<String> lines = Files.readAllLines(suiteFile.toPath());
        if (lines.isEmpty())
        {
            throw new IOException("Empty test suite file");
        }

        TestSuite suite = new TestSuite(lines.get(0)); // Title
        for (int i = 1; i < lines.size(); i++)
        {
            String filename = lines.get(i).trim();
            if (!filename.isEmpty())
            {
                suite.addTestCaseFilename(filename);
            }
        }
        return suite;
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
     * Helper to sanitize suite title to be a valid filename.
     * Replaces characters not allowed in filenames with underscore.
     */
    private String sanitizeFilename(String name)
    {
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
