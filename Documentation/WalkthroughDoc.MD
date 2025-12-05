# Application Walkthrough

## 1. Launching the Application
Run the program as a JavaFX application.  
You will be presented with the **Home Screen**, prompting you to begin.

---

## 2. Starting the Application
Click the **Start** button.  
The application will prompt you to select the folder path containing all **student program sub-folders**.

Choose the correct folder and confirm.

---

## 3. Entering the Main Menu
After selecting the student folder, you will arrive at the **Main Menu**, which contains three main options:

- **Test Manager**  
- **Results Manager**
- **Execute Test Suite**

For this walkthrough, choose **Test Manager**.

---

## 4. Selecting the Test Directory
Inside the Test Manager, you will be prompted to select a folder path that includes:

- A folder for **test suites**
- A folder for **test cases**

Both of these must exist in the same root directory.  
This location is also where newly created tests will be saved.

---

## 5. Creating a Test Suite
Create a new **Test Suite**, give it a meaningful name, and confirm.  
This suite will serve as a container for the test cases you want to run.

---

## 6. Creating Test Cases
Create one or more **Test Cases**. Each test case requires:

- A title  
- An input value  
- An expected output  

Save each test case once finished.

---

## 7. Assigning Test Cases to the Suite
With your test suite selected:

1. Click **Add Test Case**.  
2. A dropdown will appear listing all available test cases in your directory.  
3. Choose the test cases you want to include in this suite.  
4. Save the suite after adding all desired cases.

---

## 8. Returning to the Main Menu
Navigate back to the **Main Menu** after finishing your test configurations.

---

## 9. Executing the Test Suite
Click **Execute**.  
You may be prompted for an additional folder path if your student submission folders follow a specific internal structure.

- If your student program files are all in the root directory, leave the field blank and click **Execute/Run** again.

After a brief moment, the results will be generated.

---

## 10. Viewing Results
Test results will display for each student sub-folder.  
Each folder’s results are separated visually by a line of dashes (`----------`).

To view why a test failed:

- Select the corresponding row  
- Click **View Comparison (Selected)**  
- You will see the expected output versus the actual output displayed side-by-side.

---

## 11. Saving Results
From the results screen, you can save your test results in two formats:

- **Text file** (`Save Results As... (Text)`) - A human-readable text file containing all results  
- **Serialized file** (`Save Results (Serialized)`) - A binary `.ser` file that preserves all result data and metadata (recommended, as these can be reloaded and compared later)

Choose the format you prefer and save your results for future reference.

---

## 12. Accessing the Results Manager
To work with previously saved results, navigate to the **Main Menu** and click **Results Manager**.  
The Results Manager provides two options:

- **Load Saved Results** - View previously saved test results  
- **Compare Two Result Files** - Compare success rates between two different result files

---

## 13. Loading Saved Results
To view previously saved results:

1. From the **Results Manager**, click **Load Saved Results**.  
2. A file chooser will appear, filtering for serialized files (`.ser`).  
3. Select the saved results file you want to view.  
4. The results will be displayed in a read-only text area showing:
   - Test suite metadata (title, root folder, code path, total test cases)
   - All test results grouped by student (separated by dividers)
   - Summary statistics (total results, passed, failed, compile errors, runtime errors, skipped)

You can navigate back to the Results Manager or restart the application from this screen.

---

## 14. Comparing Two Result Files
To compare success rates between two different result files (useful for comparing initial submissions with resubmissions):

1. From the **Results Manager**, click **Compare Two Result Files**.  
2. Select the first result file (`.ser` format).  
3. Select the second result file (`.ser` format).  
4. A side-by-side comparison will display showing:
   - Each student's success rate as a fraction (e.g., "3/5" means 3 out of 5 tests passed)
   - Special indicators for compile errors or cases where a student appears in only one file
   - Both result sets displayed side-by-side for easy comparison

This is particularly useful for tracking student improvement between submissions.

---

If you want, I can also generate an **"Installation"**, **"Requirements"**, **"Folder Structure"**, or **"Quick Start"** section for your README.
