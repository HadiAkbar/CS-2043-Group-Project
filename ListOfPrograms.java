import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListOfPrograms
{
    private List<Program> programs;

    public ListOfPrograms()
    {
        this.programs = new ArrayList<>();
    }

    public void addProgram(Program program)
    {
        programs.add(program);
    }

    public List<Program> getPrograms()
    {
        return programs;
    }

    // Method to load all subfolders from root folder as programs
    // Each subfolder represents a student submission
    public void loadFromRootFolder(File root)
    {
        loadFromRootFolder(root, "");
    }

    // Method to load programs from root folder with optional code path
    // codePath is the subfolder within each submission (e.g., "src")
    // If codePath is empty, looks for Java files directly in submission folder
    public void loadFromRootFolder(File root, String codePath)
    {
        programs.clear();
        if (root.isDirectory())
        {
            for (File folder : root.listFiles(File::isDirectory))
            {
                File searchFolder = folder;
                
                // If code path is specified, look in that subfolder
                if (codePath != null && !codePath.trim().isEmpty())
                {
                    searchFolder = new File(folder, codePath.trim());
                }
                
                // Find Java files in the search folder
                if (searchFolder.exists() && searchFolder.isDirectory())
                {
                    File[] javaFiles = searchFolder.listFiles(f -> f.getName().endsWith(".java"));
                    if (javaFiles != null && javaFiles.length > 0)
                    {
                        // Use the first Java file found (assuming single main class per submission)
                        programs.add(new Program(folder.getName(), javaFiles[0]));
                    }
                }
            }
        }
    }
}
