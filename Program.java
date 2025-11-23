import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Program
{
    private String name; // Name of the student or submission folder
    private File sourceFile; // The Java source file associated with this program
    private Boolean compilationStatus = null; // null = not tried, true = compiled successfully, false = compilation failed

    // Constructor: initializes a Program object with a name and source file
    // Additional: Used to represent a student's submission in the grading system
    public Program(String name, File sourceFile)
    {
        this.name = name;
        this.sourceFile = sourceFile;
    }

    // Returns the name of the program/student
    // Additional: Useful for reporting results or displaying student info
    public String getName() { return name; }

    // Returns the Java source file
    // Additional: Needed for compiling or executing the student's program
    public File getSourceFile() { return sourceFile; }

    // Compile this Java program
    // Returns true if compilation succeeds, false otherwise
    public boolean compile()
    {
        try
        {
            File sourceDir = sourceFile.getParentFile();
            
            // Build javac command
            ProcessBuilder pb = new ProcessBuilder("javac", sourceFile.getName());
            pb.directory(sourceDir);
            pb.redirectErrorStream(true);
            
            Process process = pb.start();
            int exitCode = process.waitFor();
            
            return exitCode == 0;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    // Run this compiled Java program with input data
    // Returns the program's output as a string
    public String run(String inputData)
    {
        try
        {
            File sourceDir = sourceFile.getParentFile();
            String className = sourceFile.getName().replace(".java", "");
            
            // Build java command
            ProcessBuilder pb = new ProcessBuilder("java", className);
            pb.directory(sourceDir);
            pb.redirectErrorStream(true);
            
            Process process = pb.start();
            
            // Write input data to process stdin
            if (inputData != null && !inputData.isEmpty())
            {
                OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream());
                writer.write(inputData);
                writer.flush();
                writer.close();
            }
            
            // Read output from process
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );
            
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
            {
                if (output.length() > 0)
                {
                    output.append("\n");
                }
                output.append(line);
            }
            
            process.waitFor();
            reader.close();
            
            return output.toString();
        }
        catch (Exception e)
        {
            return "ERROR: " + e.getMessage();
        }
    }

    // Ensure the program is compiled (only compiles once, reuses compilation status)
    // Returns true if compilation succeeds or already succeeded, false otherwise
    public boolean ensureCompiled()
    {
        // If we've already tried to compile, return the cached result
        if (compilationStatus != null)
        {
            return compilationStatus;
        }
        
        // Try to compile and cache the result
        compilationStatus = compile();
        return compilationStatus;
    }

    // Execute a test case against this program
    // Returns a TestResult object containing execution results
    public TestResult executeTestCase(TestCase testCase)
    {
        // Ensure program is compiled (only compiles once)
        boolean compiled = ensureCompiled();
        
        String expectedOutput = testCase.getExpectedOutput();
        String actualOutput = "";
        String status;
        
        if (!compiled)
        {
            status = "COMPILE ERROR";
        }
        else
        {
            // Run the program with test case input
            actualOutput = run(testCase.getInputData());
            
            // Compare outputs
            boolean passed = compareOutputs(actualOutput, expectedOutput, testCase.getType());
            status = passed ? "PASSED" : "FAILED";
        }
        
        return new TestResult(name, testCase.getTitle(), status, actualOutput, expectedOutput);
    }

    // Helper method to compare actual output with expected output
    // Handles different types (Boolean, Int, Double, String) appropriately
    private boolean compareOutputs(String actual, String expected, String type)
    {
        if (actual == null) actual = "";
        if (expected == null) expected = "";
        
        // Trim whitespace for comparison
        actual = actual.trim();
        expected = expected.trim();
        
        if (type == null || type.isEmpty() || type.equals("String"))
        {
            // String comparison - exact match
            return actual.equals(expected);
        }
        else if (type.equals("Boolean"))
        {
            // Boolean comparison - case insensitive
            return actual.equalsIgnoreCase(expected);
        }
        else if (type.equals("Int"))
        {
            // Integer comparison - parse and compare
            try
            {
                int actualInt = Integer.parseInt(actual);
                int expectedInt = Integer.parseInt(expected);
                return actualInt == expectedInt;
            }
            catch (NumberFormatException e)
            {
                return false;
            }
        }
        else if (type.equals("Double"))
        {
            // Double comparison - parse and compare with small epsilon
            try
            {
                double actualDouble = Double.parseDouble(actual);
                double expectedDouble = Double.parseDouble(expected);
                double epsilon = 0.0001;
                return Math.abs(actualDouble - expectedDouble) < epsilon;
            }
            catch (NumberFormatException e)
            {
                return false;
            }
        }
        
        // Default to string comparison
        return actual.equals(expected);
    }

}
