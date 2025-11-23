import java.io.File;

public class Program
{
    private String name; // Name of the student or submission folder
    private File sourceFile; // The Java source file associated with this program

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
}
