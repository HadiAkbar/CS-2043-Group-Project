public class Coordinator
{
    private String rootFolder;
    private ListOfTestSuites listOfTestSuites;
    private TestSuite currentTestSuite;

    public Coordinator()
    {
        this.listOfTestSuites = new ListOfTestSuites();
        this.currentTestSuite = null;
    }

    public String getRootFolder()
    {
        return rootFolder;
    }

    public void setRootFolder(String rootFolder)
    {
        this.rootFolder = rootFolder;
    }

    public void createTestSuite(String title)
    {
        TestSuite suite = new TestSuite(title);
        listOfTestSuites.addSuite(suite);
        currentTestSuite = suite;
    }

    public void loadTestSuite()
    {
        // TODO: implement file chooser and load
        // for now just pick the first suite if exists
        if (!listOfTestSuites.getSuites().isEmpty())
        {
            currentTestSuite = listOfTestSuites.getSuites().get(0);
        }
    }

    public TestSuite getCurrentTestSuite()
    {
        return currentTestSuite;
    }

    public ListOfTestSuites getListOfTestSuites()
    {
        return listOfTestSuites;
    }
}
