import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListOfPrograms
{
    private List<Program> programs; // Stores all detected student programs

    // Additional: Initializes the internal list to hold Program objects.
    public ListOfPrograms()
    {
        this.programs = new ArrayList<>();
    }

    // Additional: Adds a Program object to the collection.
    public void addProgram(Program program)
    {
        programs.add(program);
    }

    // Additional: Returns the list of all stored Program objects.
    public List<Program> getPrograms()
    {
        return programs;
    }

    // Method to load all subfolders from root folder as programs
    // Each subfolder represents a student submission
    // Additional: This convenience method assumes all code is stored directly in each submission folder.
    public void loadFromRootFolder(File root)
    {
        loadFromRootFolder(root, "");
    }

    // Method to load programs from root folder with optional code path
    // codePath is the subfolder within each submission (e.g., "src")
    // If codePath is empty, looks for Java files directly in submission folder
    // Additional: This supports multiple project structures by allowing nested code folders.
    public void loadFromRootFolder(File root, String codePath)
    {
        programs.clear(); // Additional: Reset list before loading to avoid duplicates.

        if (root.isDirectory()) // Additional: Ensure provided root is a valid directory.
        {
            for (File folder : root.listFiles(File::isDirectory)) // Additional: Iterate student submission folders.
            {
                File searchFolder = folder; // Additional: Default search location is the submission root.
                
                // If code path is specified, look in that subfolder
                if (codePath != null && !codePath.trim().isEmpty())
                {
                    searchFolder = new File(folder, codePath.trim()); // Additional: Navigate into nested code folder.
                }
                
                // Find Java files in the search folder
                if (searchFolder.exists() && searchFolder.isDirectory())
                {
                    File[] javaFiles = searchFolder.listFiles(f -> f.getName().endsWith(".java"));

                    // Additional: Only detect Java files; assumes student submits at least one .java file.
                    if (javaFiles != null && javaFiles.length > 0)
                    {
                        // Use the first Java file found (assuming single main class per submission)
                        // Additional: Simplified assumption — useful for automated grading systems.
                        programs.add(new Program(folder.getName(), javaFiles[0]));
                    }
                }
            }
        }
    }
}
