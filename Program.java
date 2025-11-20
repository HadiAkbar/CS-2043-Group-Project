import java.io.File;

public class Program
{
    private String name;
    private File sourceFile;

    public Program(String name, File sourceFile)
    {
        this.name = name;
        this.sourceFile = sourceFile;
    }

    public String getName() { return name; }
    public File getSourceFile() { return sourceFile; }
}
