# Application Walkthrough - Complete Scenario

## Scenario: Grading CS101 Assignment Submissions and Comparing Resubmissions

**Context:** You are a professor grading Assignment 1 for CS101. Students submitted programs that calculate the sum of two numbers. You need to:
1. Create test cases and suite for the assignment
2. Test all initial submissions
3. Save the results
4. Later, test resubmissions and compare them with initial submissions

**Prerequisites:**
- Student submissions folder: `C:\CS101\Assignment1\Submissions`
- Test storage folder: `C:\CS101\Assignment1\Tests`
- Resubmissions folder: `C:\CS101\Assignment1\Resubmissions`

---

## Part 1: Setting Up Test Cases and Testing Initial Submissions

### Step 1: Launch the Application
**Action:** Run the JavaFX application  
**What you see:** Home screen with a "Start" button

---

### Step 2: Select Student Submissions Folder
**Action:** Click the **Start** button  
**What you see:** File chooser dialog appears

**Type or browse to:**
```
C:\CS101\Assignment1\Submissions
```

**Action:** Click **Open** or **Select Folder**  
**What you see:** Main Menu screen appears

---

### Step 3: Access Test Manager
**Action:** Click the **Test Manager** button  
**What you see:** Test Manager screen with folder selection prompt

---

### Step 4: Select Test Directory
**Action:** Click the folder selection button  
**What you see:** File chooser dialog

**Type or browse to:**
```
C:\CS101\Assignment1\Tests
```

**Note:** This folder should contain (or will contain) `test-cases` and `test-suites` subfolders. If they don't exist, the application will create them.

**Action:** Click **Open** or **Select Folder**  
**What you see:** Test Suite Management screen

---

### Step 5: Create a New Test Suite
**Action:** Click the **Create New Suite** button  
**What you see:** Text input dialog appears

**Type exactly:**
```
Assignment1_SumCalculator
```

**Action:** Click **OK** or **Create**  
**What you see:** Success message, and the suite appears in the suite list

---

### Step 6: Create First Test Case
**Action:** Click the **Create New Test Case** button  
**What you see:** Test case creation dialog with multiple fields

**Fill in the fields exactly as shown:**

**Title field:**
```
Test1_PositiveNumbers
```

**Input Data field:**
```
5,10
```

**Expected Output field:**
```
15
```

**Type field:**
```
Basic
```

**Action:** Click **OK** or **Save**  
**What you see:** Success message: "Test case 'Test1_PositiveNumbers' has been created and saved."

---

### Step 7: Create Second Test Case
**Action:** Click the **Create New Test Case** button again

**Title field:**
```
Test2_NegativeNumbers
```

**Input Data field:**
```
-5,-10
```

**Expected Output field:**
```
-15
```

**Type field:**
```
Edge Case
```

**Action:** Click **OK** or **Save**  
**What you see:** Success message confirming the test case was created

---

### Step 8: Create Third Test Case
**Action:** Click the **Create New Test Case** button again

**Title field:**
```
Test3_MixedNumbers
```

**Input Data field:**
```
5,-3
```

**Expected Output field:**
```
2
```

**Type field:**
```
Edge Case
```

**Action:** Click **OK** or **Save**  
**What you see:** Success message confirming the test case was created

---

### Step 9: Edit a Test Case (Optional)
**Action:** Click the **Manage Test Cases** button  
**What you see:** List of all test cases

**Action:** Click on **Test1_PositiveNumbers** in the list to select it

**Action:** Click the **Edit Selected Test Case** button  
**What you see:** Test case dialog pre-filled with existing values

**Change the Expected Output field to:**
```
15
```
(Keep it the same, or change it if you want to test editing)

**Action:** Click **OK** or **Save**  
**What you see:** Success message: "Test case has been updated and saved."

**Action:** Click the **Back** button to return to Test Suite Management

---

### Step 10: Add Test Cases to the Suite
**Action:** Make sure **Assignment1_SumCalculator** is selected in the suite list

**Action:** Click the **Add Test Case** button  
**What you see:** Dropdown menu appears with all available test cases

**Action:** Click on **Test1_PositiveNumbers** in the dropdown  
**What you see:** The test case appears in the suite's test case list

**Action:** Click the **Add Test Case** button again

**Action:** Click on **Test2_NegativeNumbers** in the dropdown

**Action:** Click the **Add Test Case** button again

**Action:** Click on **Test3_MixedNumbers** in the dropdown

**What you see:** All three test cases now appear in the suite's test case list

---

### Step 11: Save the Test Suite
**Action:** Click the **Save Suite** button  
**What you see:** Success message confirming the suite was saved

---

### Step 12: Return to Main Menu
**Action:** Click the **Back** button (or navigate back to Main Menu)  
**What you see:** Main Menu screen

---

### Step 13: Execute the Test Suite
**Action:** Click the **Execute Test Suite** button  
**What you see:** Execution screen showing the root folder and a code path field

**Code Path field:** Leave this field **blank** (if student code is directly in their submission folders)

**Action:** Click the **Execute Test Suite** button  
**What you see:** Processing... then results screen appears

---

### Step 14: View Test Results
**What you see:** Results list showing each student's results, separated by dashes

**Example output format:**
```
Student1 | Test1_PositiveNumbers | PASSED
Student1 | Test2_NegativeNumbers | PASSED
Student1 | Test3_MixedNumbers | PASSED
-----------------------------------
Student2 | Test1_PositiveNumbers | FAILED
Student2 | Test2_NegativeNumbers | COMPILE ERROR
...
```

**Action:** Click on a **FAILED** result row to select it

**Action:** Click the **View Comparison (Selected)** button  
**What you see:** Side-by-side comparison showing:
- **Expected Output:** (the expected value)
- **Actual Output:** (what the student's program produced)

**Action:** Click **Back to Results** to return to the results list

---

### Step 15: Save Results as Serialized File
**Action:** Click the **Save Results (Serialized)** button  
**What you see:** File save dialog with suggested filename: `Assignment1_SumCalculator_results.ser`

**Navigate to or type:**
```
C:\CS101\Assignment1\Results
```

**Filename field (you can change it):**
```
InitialSubmissions_Assignment1.ser
```

**Action:** Click **Save**  
**What you see:** Success message: "Test results have been saved (serialized) to: C:\CS101\Assignment1\Results\InitialSubmissions_Assignment1.ser"

---

### Step 16: Save Results as Text File (Optional)
**Action:** Click the **Save Results As... (Text)** button  
**What you see:** File save dialog

**Navigate to:**
```
C:\CS101\Assignment1\Results
```

**Filename field:**
```
InitialSubmissions_Assignment1.txt
```

**Action:** Click **Save**  
**What you see:** Success message confirming the text file was saved

---

## Part 2: Testing Resubmissions and Comparing Results

### Step 17: Change Root Folder for Resubmissions
**Action:** Click the **Back** button to return to Main Menu

**Action:** Click the **Change Root Folder / Back to Start** button  
**What you see:** File chooser dialog

**Type or browse to:**
```
C:\CS101\Assignment1\Resubmissions
```

**Action:** Click **Open** or **Select Folder**  
**What you see:** Main Menu screen (now showing the new root folder path)

---

### Step 18: Execute Test Suite on Resubmissions
**Action:** Click the **Execute Test Suite** button  
**What you see:** Execution screen

**Code Path field:** Leave **blank** (or enter a subfolder path if needed, e.g., `src`)

**Action:** Click the **Execute Test Suite** button  
**What you see:** Results screen with resubmission results

---

### Step 19: Save Resubmission Results
**Action:** Click the **Save Results (Serialized)** button

**Navigate to:**
```
C:\CS101\Assignment1\Results
```

**Filename field:**
```
Resubmissions_Assignment1.ser
```

**Action:** Click **Save**  
**What you see:** Success message confirming save

---

### Step 20: Access Results Manager
**Action:** Click the **Back** button to return to Main Menu

**Action:** Click the **Results Manager** button  
**What you see:** Results Manager screen with two options:
- Load Saved Results
- Compare Two Result Files

---

### Step 21: Load and View Saved Results
**Action:** Click the **Load Saved Results** button  
**What you see:** File chooser dialog filtering for `.ser` files

**Navigate to:**
```
C:\CS101\Assignment1\Results
```

**Action:** Click on **InitialSubmissions_Assignment1.ser**

**Action:** Click **Open**  
**What you see:** Read-only text area displaying:
- Test suite metadata (title, root folder, code path, total test cases)
- All results grouped by student with dividers
- Summary statistics (total, passed, failed, compile errors, runtime errors, skipped)

**Action:** Scroll through the results to review them

**Action:** Click the **Back** button to return to Results Manager

---

### Step 22: Compare Initial Submissions vs Resubmissions
**Action:** Click the **Compare Two Result Files** button  
**What you see:** File chooser dialog for first file

**Navigate to:**
```
C:\CS101\Assignment1\Results
```

**Action:** Click on **InitialSubmissions_Assignment1.ser**

**Action:** Click **Open**  
**What you see:** File chooser dialog for second file

**Navigate to:**
```
C:\CS101\Assignment1\Results
```

**Action:** Click on **Resubmissions_Assignment1.ser**

**Action:** Click **Open**  
**What you see:** Side-by-side comparison screen showing:

**Left side (First Submission):**
```
First Submission
File: InitialSubmissions_Assignment1.ser
Suite: Assignment1_SumCalculator
==================================================
Student                    Success Rate
--------------------------------------------------
Student1                   3/3
Student2                   0/3
Student3                   COMPILE ERROR
Student4                   2/3
```

**Right side (Second Submission):**
```
Second Submission
File: Resubmissions_Assignment1.ser
Suite: Assignment1_SumCalculator
==================================================
Student                    Success Rate
--------------------------------------------------
Student1                   3/3
Student2                   2/3
Student3                   3/3
Student4                   3/3
```

**Note:** Each side displays:
- A header label ("First Submission" or "Second Submission")
- The result file name ("File: [filename].ser") - this identifies which saved result file is being compared
- The test suite title ("Suite: Assignment1_SumCalculator") - this identifies which test suite was used
- A table showing each student's success rate

**Additional details:**
- Success rates show as fractions (e.g., "3/3" means 3 out of 3 tests passed)
- "COMPILE ERROR" appears when all tests failed to compile
- "No submission" appears if a student is missing from one file

**Action:** Review the comparison to see student improvements

**Action:** Click the **Back** button to return to Results Manager

---

## Part 3: Managing Test Cases (Additional Features)

### Step 23: Edit Test Cases
**Action:** From Main Menu, click **Test Manager**

**Action:** Select your test directory if prompted: `C:\CS101\Assignment1\Tests`

**Action:** Click the **Manage Test Cases** button  
**What you see:** List of all test cases

**Action:** Click on **Test2_NegativeNumbers** to select it

**Action:** Click the **Edit Selected Test Case** button  
**What you see:** Test case dialog with pre-filled values

**Change the Expected Output field to:**
```
-15
```
(Verify it's correct)

**Action:** Click **OK** or **Save**  
**What you see:** Success message confirming update

---

### Step 24: Delete a Test Case (Optional)
**Action:** Still on the Manage Test Cases screen

**Action:** Click on a test case you want to delete (e.g., if you created a test case by mistake)

**Action:** Click the **Delete Selected Test Case** button  
**What you see:** The test case is removed from the list

**Note:** If this test case was in a test suite, the suite will still reference it, but it will show as "not found" when executing.

---

## Summary of Features Covered

This scenario demonstrates all Version 2 features:

✅ **Test Case Management:**
- Creating test cases with title, input, expected output, and type
- Editing existing test cases
- Deleting test cases

✅ **Test Suite Management:**
- Creating test suites
- Adding test cases to suites
- Saving test suites

✅ **Test Execution:**
- Executing test suites on student submissions
- Handling multiple Java files (automatically detects main method)
- Specifying code path for nested folder structures

✅ **Results Viewing:**
- Viewing test results grouped by student
- Side-by-side comparison of expected vs actual output

✅ **Results Persistence:**
- Saving results as text files
- Saving results as serialized files (`.ser` format)

✅ **Results Management:**
- Loading previously saved results
- Viewing loaded results in read-only format

✅ **Results Comparison:**
- Comparing two result files side-by-side
- Success rate calculation (fraction format)
- Handling special cases:
  - Missing students ("No submission")
  - Compile errors ("COMPILE ERROR")
  - Students appearing in only one file

✅ **Multiple Folder Support:**
- Changing root folder for different submission sets
- Executing same test suite on different folders

---

## Notes

- **File Paths:** All file paths in this scenario are examples. Use your actual folder paths when following along.
- **Code Path:** If student submissions have code in a subfolder (e.g., `src`), enter that path in the code path field. Leave blank if code is directly in the submission folder.
- **Serialized Files:** Use `.ser` files for comparisons. Text files are for human reading only.
- **Test Case Types:** The "Type" field is optional metadata. You can use any value (e.g., "Basic", "Edge Case", "Boundary", etc.).
