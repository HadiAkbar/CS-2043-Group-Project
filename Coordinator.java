import java.io.File;

public class Coordinator
{
    private String rootFolder;
    private ListOfPrograms programs;
    private ListOfTestSuite suites;

    public Coordinator()
    {
        this.programs = new ListOfPrograms();
        this.suites = new ListOfTestSuite();
    }

    public void setRootFolder(String path)
    {
        this.rootFolder = path;
        File root = new File(path);
        programs.loadFromRootFolder(root);
        System.out.println("Root folder set: " + path);
        System.out.println("Found " + programs.getPrograms().size() + " programs.");
    }

    public String getRootFolder() { return rootFolder; }

    // --- Test Suite Methods ---
    public void createTestSuite(String title)
    {
        TestSuite suite = new TestSuite(title);
        suites.addTestSuite(suite);
    }

    public TestSuite getCurrentTestSuite()
    {
        return suites.getFirstSuite();
    }

    // --- Test Suite Persistence (stub) ---
    public void saveCurrentTestSuite()
    {
        // TODO: implement file save (JSON or serialization)
        System.out.println("Test Suite saved: " + getCurrentTestSuite().getTitle());
    }

    public void loadTestSuite()
    {
        // TODO: implement file load
        System.out.println("Test Suite loaded (stub)");
    }

    // --- Execution ---
    public void executeTestSuite()
    {
        // TODO: compile & run each student program with all test cases
        System.out.println("Executing Test Suite (stub)");
    }

    public ListOfPrograms getPrograms() { return programs; }
    public ListOfTestSuite getSuites() { return suites; }
}
