import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListOfPrograms
{
    private List<Program> programs; // Stores all detected student programs
    private List<String> skippedFolders; // Stores folder names that were skipped (no main method found)

    // Additional: Initializes the internal list to hold Program objects.
    public ListOfPrograms()
    {
        this.programs = new ArrayList<>();
        this.skippedFolders = new ArrayList<>();
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

    // Returns the list of folder names that were skipped (no main method found)
    // Additional: Useful for reporting which student submissions couldn't be loaded
    public List<String> getSkippedFolders()
    {
        return skippedFolders;
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
        skippedFolders.clear(); // Reset skipped folders list

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
                        // Search for the Java file containing public static void main
                        File entryPointFile = findEntryPointFile(javaFiles);
                        if (entryPointFile != null)
                        {
                            programs.add(new Program(folder.getName(), entryPointFile));
                        }
                        else
                        {
                            // No main method found - add to skipped folders list
                            skippedFolders.add(folder.getName());
                        }
                    }
                    else
                    {
                        // No Java files found in folder
                        skippedFolders.add(folder.getName());
                    }
                }
                else
                {
                    // Search folder doesn't exist or isn't a directory
                    skippedFolders.add(folder.getName());
                }
            }
        }
    }

    // Helper method to find the Java file containing public static void main
    // Searches through all Java files and returns the first one with a main method
    // Returns null if no main method is found
    private File findEntryPointFile(File[] javaFiles)
    {
        for (File javaFile : javaFiles)
        {
            if (hasMainMethod(javaFile))
            {
                return javaFile;
            }
        }
        return null; // No main method found in any file
    }

    // Helper method to check if a Java file contains public static void main
    // Reads the file and searches for the main method signature
    // Looks for the pattern: public static void main(String[] args) or main(String args[])
    private boolean hasMainMethod(File javaFile)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(javaFile)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                line = line.trim();
                
                // Look for "public static void main" signature
                // Must contain: public, static, void, and main(
                // This handles standard formats like:
                //   public static void main(String[] args)
                //   public static void main(String args[])
                //   public static void main(String... args)
                if (line.contains("public") && 
                    line.contains("static") && 
                    line.contains("void") && 
                    line.contains("main("))
                {
                    return true; // Found the main method signature
                }
            }
            return false;
        }
        catch (IOException e)
        {
            return false; // If we can't read the file, assume no main method
        }
    }
}
