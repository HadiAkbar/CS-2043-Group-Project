import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
/* Represents an external Java program submission.
* This class is responsible for compiling and running the external source file
* and managing the results (exit code, output, compilation status).*/
public class Program
{
    private String name; // Name of the student or submission folder
    private File sourceFile; // The Java source file associated with this program
    private Boolean compilationStatus = null; // null = not tried, true = compiled successfully, false = compilation failed
    private Integer lastExitCode = null; // Exit code from last program execution
    private String className = null; // Cached class name extracted from source file

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
    
    //Attempts to compile the source file using the 'javac' command.
    //The compilation is run as a separate external process.
    // Compile this Java program
    // Returns true if compilation succeeds, false otherwise
    public boolean compile()
    {
        List<String> cmd = new ArrayList<>();
        cmd.add("javac");
        cmd.add(sourceFile.getAbsolutePath()); // Use full file path
        
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true); // Merge stdout & stderr
        
        try
        {
            Process p = pb.start();
            
            // Read combined output (discard for now, but could be logged)
            try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream())))
            {
                String line;
                while ((line = r.readLine()) != null)
                {
                    // Compilation errors are captured but not displayed here
                    // They're indicated by non-zero exit code
                }
            }
            
            int exit = p.waitFor();

            // Store whether compilation succeeded (true) or failed (false)
            // This allows other parts of the program to check compile status without re-compiling
            compilationStatus = (exit == 0);
        
            
            if (exit != 0)
            {
                return false;
            }
            return true;
        }
        catch (IOException e)
        {   // Compilation could not be started or was interrupted, treat as failure

            compilationStatus = false;
            return false;
        }
        catch (InterruptedException e)
        {    // Current thread was interrupted while waiting for the process to finish
            Thread.currentThread().interrupt();
            compilationStatus = false;
            return false;
        }
    }
    
    //Extracts the main public class name from the source file content.
    //This is required to properly execute the program using 'java <className>'.
    // Extract the class name from the Java source file
    // Reads the file and finds the public class declaration
    private String extractClassName()
    {
        if (className != null)
        {
            return className; // Return cached class name
        }
        
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
            String line;
            while ((line = reader.readLine()) != null)
            {
                line = line.trim();
                // Look for public class declaration
                if (line.startsWith("public class "))
                {
                    // Extract class name (e.g., "public class MyClass {" -> "MyClass")
                    int start = "public class ".length();
                    int end = line.indexOf(' ', start);
                    if (end == -1)
                    {
                        end = line.indexOf('{', start);
                    }
                    if (end == -1)
                    {
                        end = line.length();
                    }
                    className = line.substring(start, end).trim();
                    reader.close();
                    return className;
                }
            }
            reader.close();
        }
        catch (Exception e)
        {   // Handle exceptions during file reading or string manipulation]
            // If extraction fails, fall back to filename
        }
        
        // Fallback: use filename without extension
        className = sourceFile.getName().replace(".java", "");
        return className;
    }
    
    //Executes the compiled Java class file with the provided input data.
    //It pipes inputData to stdin and captures all output from stdout/stderr.
    // Run this compiled Java program with input data
    // Returns the program's output as a string
    public String run(String inputData)
    {
        try
        {
            File sourceDir = sourceFile.getParentFile();
            String classPath = sourceDir.getAbsolutePath();
            // The class files (.class) are assumed to be in the same directory as the .java source.
            String classNameToRun = extractClassName();
            
            // Build java command with explicit classpath
            List<String> cmd = new ArrayList<>();
            cmd.add("java");
            cmd.add("-cp");
            cmd.add(classPath);
            cmd.add(classNameToRun);
            
            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.redirectErrorStream(true); // Merge stdout & stderr
            
            Process process = pb.start();
            
            // Write input data to process stdin (UTF-8 encoded)
            if (inputData != null && !inputData.isEmpty())
            {    // Use try-with-resources to ensure the OutputStreamWriter is closed.
                try (OutputStreamWriter writer = new OutputStreamWriter(
                        process.getOutputStream(), StandardCharsets.UTF_8))
                {
                    writer.write(inputData);
                    writer.flush();
                    // Stream will be closed by try-with-resources, signaling EOF
                }
                catch (IOException e)
                {
                    // If writing fails, allow process to proceed
                }
            }
            else
            {
                // If no input, close stdin to avoid child waiting
                try
                {
                    process.getOutputStream().close();
                }
                catch (IOException ignored) { }
            }
            
            // Read output from process
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())))
            {
                String line;
                while ((line = reader.readLine()) != null)
                {
                    if (output.length() > 0)
                    {
                        output.append("\n");
                    }
                    output.append(line);
                }
            }
            
            int exitCode = process.waitFor();
            
            // Store exit code for runtime error detection
            lastExitCode = exitCode;
            
            return output.toString();
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            // Indicates the grading system thread was terminated while waiting for the program.
            return "ERROR: Execution interrupted";
        }
        catch (Exception e)
        {    // Catch-all for IO or process-related errors.
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

    //Executes a single test case against the compiled program.
    //Handles compilation status, runtime errors, and output comparison.
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
            
            // Check for runtime errors (non-zero exit code)
            if (lastExitCode != null && lastExitCode != 0)
            {
                status = "RUNTIME ERROR";
            }
            else
            {
                // Compare outputs
                boolean passed = compareOutputs(actualOutput, expectedOutput, testCase.getType());
                status = passed ? "PASSED" : "FAILED";
            }
        }
        
        return new TestResult(name, testCase.getTitle(), status, actualOutput, expectedOutput);
    }

    //Compares the actual output string against the expected output based on the specified data type.
    //The comparison logic is crucial for robust test case validation.
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
