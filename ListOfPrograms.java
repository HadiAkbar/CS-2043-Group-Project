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

    // Load all subfolders from root folder as programs
    public void loadFromRootFolder(File root)
    {
        programs.clear();
        if (root.isDirectory())
        {
            for (File folder : root.listFiles(File::isDirectory))
            {
                // Assume Java file inside folder has same name as folder
                File[] javaFiles = folder.listFiles(f -> f.getName().endsWith(".java"));
                if (javaFiles != null && javaFiles.length > 0)
                {
                    programs.add(new Program(folder.getName(), javaFiles[0]));
                }
            }
        }
    }
}
