# Modifications Required for Submission #1 to Support Version 2 Requirements

## Overview
This document outlines the modifications needed to the requirements and modeling (Submission #1) to support Version 2 features detailed in the submission 4 pdf, with particular focus on handling resubmissions and result comparison. The document describes both what we already implemented in Version 1 and what new components we are to add to meet expectations for Version 2.

---

## 1. New Classes to Add

### 1.1 TestExecutionResults
**Implementation Status:** **NEW CLASS - Added in Version 2**

**Purpose:** 
The `TestExecutionResults` class was created to address the requirement of storing test execution results for later retrieval and comparison. This class encapsulates an entire test execution session, including not just the test results themselves, but also the metadata necessary to understand the context of that execution. This metadata is crucial for comparing results from different submission folders (e.g., first submission vs. resubmission).

**What It Does:**
When a professor executes a test suite, the system creates a `TestExecutionResults` object that captures everything about that execution session. This object can then be serialized to a file, allowing the professor to save the results and reload them later, even after closing the application. The class stores the test suite that was used, the folder path where student submissions were located, any code path that was specified, all the individual test results, and the total number of test cases in the suite.

**Attributes:**
- `testSuiteTitle: String` - The name of the test suite that was executed. This allows the system to identify which suite was used when results are loaded later.
- `rootFolderPath: String` - The path to the folder containing student submissions. This is essential for understanding which set of submissions these results represent (e.g., "first_submissions" vs. "resubmissions").
- `codePath: String` - Optional subfolder path within each submission (e.g., "src"). This captures whether the code was in a nested folder structure.
- `results: List<TestResult>` - All test results from the execution. This is a collection of all individual test case results for all students.
- `totalTestCases: int` - Total number of test cases in the suite. This is used when calculating success rates as fractions.

**Relationships:**
- **Composition with TestResult:** The `TestExecutionResults` class contains a list of `TestResult` objects in a one-to-many relationship. Each `TestExecutionResults` instance owns multiple `TestResult` instances, meaning that when a result set is saved, all individual test results are saved with it.
- **Association with TestSuite:** The class references a `TestSuite` by storing its title as a string. This creates a many-to-one association (many result sets can reference the same test suite) but uses a string reference rather than a direct object reference, which is appropriate for serialization.
- **Implements Serializable:** The class implements Java's `Serializable` interface, which allows the entire object (including all its nested `TestResult` objects) to be converted to a byte stream and saved to disk, then later reconstructed exactly as it was.

**Methods:**
- Constructor: `TestExecutionResults(String testSuiteTitle, String rootFolderPath, String codePath, List<TestResult> results)` - Creates a new instance with all execution metadata and results. The constructor also calculates the total number of test cases by counting unique test case titles in the results.
- `getTestSuiteTitle(): String` - Returns the test suite title
- `getRootFolderPath(): String` - Returns the root folder path
- `getCodePath(): String` - Returns the code path
- `getResults(): List<TestResult>` - Returns a copy of the results list
- `getTotalTestCases(): int` - Returns the total number of test cases

**Why This Class Was Needed:**
In Version 1, test results were only displayed immediately after execution and could be saved as a text file. There was no way to reload results programmatically or compare results from different executions. The `TestExecutionResults` class enables the system to store complete execution sessions in a structured, machine-readable format, making it possible to reload results later and perform automated comparisons between different result sets.

---

## 2. Modified Existing Classes

### 2.1 TestResult
**Implementation Status:** **MODIFIED - Already existed in Version 1, enhanced in Version 2**

**What Was Already Implemented:**
In Version 1, the `TestResult` class was already a well-established component that stored individual test case execution results. It contained information about which student the result belonged to, which test case was executed, whether it passed or failed, and the actual vs. expected outputs. The class was used extensively throughout the system to display results immediately after test execution.

**What Needed to Be Modified:**
To support the requirement of storing results using object serialization, the `TestResult` class needed to implement Java's `Serializable` interface. This was a straightforward modification that involved adding `implements Serializable` to the class declaration and adding a `serialVersionUID` constant for version control. The `serialVersionUID` is a long value that helps ensure compatibility when deserializing objects that may have been saved with a different version of the class.

**Why This Modification Was Necessary:**
Since `TestExecutionResults` contains a list of `TestResult` objects, and we need to serialize entire execution sessions, every object in that chain must be serializable. When Java serializes a `TestExecutionResults` object, it recursively serializes all its nested objects, including all the `TestResult` instances in the results list. Without making `TestResult` serializable, the serialization process would fail.

**Technical Details:**
- Added `implements Serializable` to class declaration
- Added `private static final long serialVersionUID = 1L;` constant
- No other changes were needed - all existing fields (String types and primitives) are already serializable

---

### 2.2 Coordinator
**Implementation Status:** **MODIFIED - Core class from Version 1, extended in Version 2**

**What Was Already Implemented:**
The `Coordinator` class was the central orchestrator in Version 1, managing test cases, test suites, and student programs. It already had methods to execute test suites and return lists of `TestResult` objects. The class maintained state about the current test suite, root folder, and save folder. It also had the capability to load programs from a root folder and execute test cases against them.

**What Needed to Be Added:**
To support the new requirements for storing and comparing results, the `Coordinator` class needed several enhancements. First, it needed to track metadata about the last execution (specifically the code path and root folder used), so that when results are saved, this context information can be included. Second, it needed methods to save and load `TestExecutionResults` objects using Java's serialization mechanism. Finally, the existing `executeTestSuite` method needed to be modified to capture and store this execution metadata.

**New Attributes Added:**
- `lastExecutionCodePath: String` - This attribute stores the code path that was used during the most recent test suite execution. The code path is an optional parameter that allows the system to handle submissions where the Java files are in a subfolder (e.g., "src" or "source"). This information is crucial when saving results because it documents exactly how the submissions were structured.
- `lastExecutionRootFolder: String` - This attribute stores the root folder path that was used during the most recent execution. This is essential for understanding which set of submissions the results represent, particularly important when comparing first submissions to resubmissions.

**New Methods Added:**
- `saveTestExecutionResults(TestExecutionResults results, File file): void` - This method takes a `TestExecutionResults` object and a file location, then uses Java's `ObjectOutputStream` to serialize the entire object (including all nested `TestResult` objects) to a `.ser` file. The method handles the file I/O operations and throws exceptions if the save fails.
- `loadTestExecutionResults(File file): TestExecutionResults` - This method performs the reverse operation, using `ObjectInputStream` to deserialize a previously saved `TestExecutionResults` object from a `.ser` file. This allows the system to restore complete execution sessions that were saved earlier.
- `getLastExecutionCodePath(): String` - A simple getter method that returns the code path from the last execution. This is used by the UI when creating a `TestExecutionResults` object to save.
- `getLastExecutionRootFolder(): String` - A simple getter method that returns the root folder from the last execution. This is also used when saving results to preserve the execution context.

**Modified Methods:**
- `executeTestSuite(String codePath): List<TestResult>` - This method was modified to store the execution metadata after a successful execution. Specifically, after the test suite execution completes and results are generated, the method now stores the `codePath` parameter and the current `rootFolder` value in the new attributes mentioned above. This ensures that whenever results are saved, they include the complete context of how they were generated.

**Relationships:**
- **Association with TestExecutionResults:** The `Coordinator` class now has a one-to-many association with `TestExecutionResults`. The Coordinator can create multiple `TestExecutionResults` objects (one for each execution session that gets saved), and it manages the saving and loading of these objects through the file system.

**Why These Modifications Were Necessary:**
The ability to save and reload results is fundamental to the resubmission comparison feature. Without these methods, professors would have no way to preserve execution results from the first submission round to compare them with resubmission results later. The metadata tracking ensures that when results are loaded, the system knows exactly which folder and configuration were used, which is important for understanding the context of the results.

---

### 2.3 Program
**Implementation Status:** **NO CHANGES NEEDED - Already supported requirement #5**

**What Was Already Implemented:**
The `Program` class in Version 1 was designed to handle compilation and execution of Java programs. When a `Program` instance is created, it is given a reference to a single Java source file (the one containing the main method). The class has a `compile()` method that uses the `javac` command to compile this file.

**Why No Changes Were Needed:**
Requirement #5 states that when you compile the file containing the main method using `javac`, all other required files in the same folder will be automatically compiled as well. This is standard Java compiler behavior - when `javac` encounters dependencies (other classes referenced by the file being compiled), it automatically compiles those dependencies if their source files are present in the same directory. Therefore, the existing `Program` class already supports multiple-file submissions correctly. When `compile()` is called on the main file, Java's compiler handles all the dependent files automatically.

**Technical Note:**
The detection of which file contains the main method (when there are multiple Java files) is handled by the `ListOfPrograms` class, not the `Program` class itself. The `Program` class simply compiles whatever file it is given, and the Java compiler handles the rest.

---

### 2.4 ListOfPrograms
**Implementation Status:** **NO CHANGES NEEDED - Already supported requirement #5**

**What Was Already Implemented:**
The `ListOfPrograms` class in Version 1 already had sophisticated logic for handling multiple Java files. When loading programs from a folder, the class would scan for all `.java` files in that folder. It then had a method called `findEntryPointFile()` that would iterate through all the Java files and search each one for the `public static void main(` signature. The method `hasMainMethod()` would read through each file line by line, looking for a line that contained all the keywords: "public", "static", "void", and "main(".

**Why No Changes Were Needed:**
This existing implementation perfectly satisfies requirement #5, which states that the system must detect which file contains the main method by searching through all files for the string "public static void main(". The Version 1 implementation already did exactly this - it would find all Java files in a folder, check each one for the main method signature, and use the file that contains it to create a `Program` instance. The system correctly handles cases where there are multiple Java files, finding the one with main and using that as the entry point.

**How It Works:**
When `loadFromRootFolder()` is called, it finds all `.java` files in the specified folder. Then `findEntryPointFile()` is called, which iterates through each file and calls `hasMainMethod()` on each one. The first file that contains the main method signature is returned and used to create a `Program` object. If no file contains main, the folder is added to the skipped folders list. This approach ensures that the system correctly identifies the entry point even when there are multiple Java files, and it doesn't make assumptions about file or class names.

---

## 3. New Use Cases

### 3.1 Save Test Execution Results
**Implementation Status:** **NEW USE CASE - Added in Version 2**

**Actor:** Professor/Instructor

**Preconditions:** 
A test suite has been executed successfully, and the results screen is currently displayed. The system has all the test results in memory along with the execution metadata (test suite title, root folder path, code path).

**Main Flow:**
1. After viewing the test execution results, the professor clicks the "Save Results (Serialized)" button on the results screen. This button is located alongside the existing "Save Results As... (Text)" button, giving professors two options: save as human-readable text or save as a serialized object for later programmatic access.

2. The system displays a file chooser dialog, allowing the professor to select where to save the file. The dialog is pre-configured to suggest a filename based on the test suite title (e.g., "RealTestSuite_results.ser") and filters for `.ser` files (serialized files).

3. Once the professor selects a location and confirms, the system creates a new `TestExecutionResults` object. This object is constructed with the test suite title (from the currently selected suite), the root folder path that was used (stored in `Coordinator.lastExecutionRootFolder`), the code path that was used (stored in `Coordinator.lastExecutionCodePath`), and the complete list of `TestResult` objects from the execution.

4. The system then calls `Coordinator.saveTestExecutionResults()`, which uses Java's `ObjectOutputStream` to serialize the entire `TestExecutionResults` object (including all nested `TestResult` objects) to the specified file. The serialization process converts the object graph into a byte stream that can be written to disk.

5. Upon successful completion, the system displays a confirmation dialog informing the professor that the results have been saved, showing the full file path where they were stored.

**Postconditions:** 
The test execution results are now persisted to disk as a serialized object file. This file can be loaded later, even after the application is closed and reopened. The file contains not just the results, but all the metadata needed to understand the context of the execution.

**Alternative Flows:**
- If the file cannot be written (e.g., insufficient permissions, disk full), the system displays an error message and the save operation is cancelled.
- If the professor cancels the file chooser, no save operation occurs and the system returns to the results screen.

---

### 3.2 Load Test Execution Results
**Implementation Status:** **NEW USE CASE - Added in Version 2**

**Actor:** Professor/Instructor

**Preconditions:** 
A previously saved result file (`.ser` file) exists on the file system. The professor is on the Results Manager screen.

**Main Flow:**
1. The professor navigates to the Results Manager screen from the Main Menu and clicks the "Load Saved Results" button. This provides access to previously saved execution results without needing to re-run the test suite.

2. The system displays a file chooser dialog that filters for `.ser` files (serialized result files). The professor can browse to the location where they previously saved results and select the desired file.

3. Once a file is selected, the system calls `Coordinator.loadTestExecutionResults()`, which uses Java's `ObjectInputStream` to deserialize the `TestExecutionResults` object from the file. This process reconstructs the entire object graph exactly as it was when saved, including all `TestResult` objects and all metadata.

4. The system then displays the loaded results in a dedicated screen with a non-modifiable text area. The display includes:
   - The test suite title that was used
   - The root folder path where submissions were located
   - The code path that was specified (if any)
   - The total number of test cases in the suite
   - All test results, grouped by student, showing student name, test case title, and status (PASSED/FAILED/COMPILE ERROR/RUNTIME ERROR)
   - A summary showing total results, passed, failed, compile errors, runtime errors, and skipped entries

**Postconditions:** 
The previously saved results are now displayed on screen in a read-only format. The professor can review the results but cannot modify them. The professor can navigate back to the Results Manager or restart the application.

**Alternative Flows:**
- If the selected file is corrupted or not a valid serialized `TestExecutionResults` object, the system displays an error message explaining that the file could not be loaded.
- If the professor cancels the file chooser, no load operation occurs and the system returns to the Results Manager screen.

---

### 3.3 Compare Two Result Files
**Implementation Status:** **NEW USE CASE - Added in Version 2**

**Actor:** Professor/Instructor

**Preconditions:** 
Two previously saved result files (`.ser` files) exist on the file system. These typically represent results from a first submission and a resubmission. The professor is on the Results Manager screen.

**Main Flow:**
1. The professor clicks the "Compare Two Result Files" button on the Results Manager screen. This initiates the comparison workflow designed specifically for evaluating student improvement between submission rounds.

2. The system displays a file chooser dialog prompting the professor to select the first result file. This would typically be the results from the first submission round.

3. Once the first file is selected, the system loads it using `Coordinator.loadTestExecutionResults()`, deserializing the first `TestExecutionResults` object.

4. The system then displays another file chooser dialog, this time prompting for the second result file. This would typically be the results from the resubmission round.

5. Once the second file is selected, the system loads it as well, creating a second `TestExecutionResults` object.

6. The system then performs the comparison logic:
   - It extracts all unique student names from both result sets (students may appear in one or both sets)
   - For each student, it calculates the success rate from the first result set. The success rate is calculated as: (number of tests passed) / (total number of test cases in the suite), displayed as a fraction (e.g., "3/5" means 3 out of 5 tests passed).
   - For each student, it calculates the success rate from the second result set using the same formula.
   - Special handling is applied for edge cases (see Special Cases below).

7. The system displays a side-by-side comparison view with two text areas:
   - Left side: Shows "First Submission" with the test suite title and a table of students with their success rates
   - Right side: Shows "Second Submission" with the test suite title and a table of students with their success rates
   - Both sides are aligned so that the same student appears on the same row, making it easy to compare their performance

**Postconditions:** 
The comparison view is displayed, allowing the professor to easily see how each student's performance changed (or didn't change) between the two submission rounds. The professor can identify which students improved, which stayed the same, and which may have regressed.

**Special Cases:**
- **Missing Student in One Set:** If a student appears in the first result set but not in the second (or vice versa), the system displays "No submission" for that student on the side where they are missing. This clearly indicates that the student did not provide a resubmission (or did not submit in the first round).
- **Compile Errors:** If all test cases for a student resulted in compilation errors (all results have status "COMPILE ERROR"), the system displays "COMPILE ERROR" instead of a fraction like "0/5". This provides more meaningful feedback than showing "0/5", as it specifically indicates that the issue was compilation failure rather than test case failures.
- **Skipped Entries:** Students whose folders were skipped (no main method found) are handled separately and don't appear in the success rate calculation, as they have no valid test results.

**Why This Use Case Is Important:**
This use case directly addresses the core requirement of Version 2: enabling professors to compare first submissions with resubmissions to evaluate student improvement. The side-by-side display makes it immediately clear which students improved their success rates, which is the primary metric for assessing resubmission quality.

---

### 3.4 Execute Test Suite on Different Folder
**Implementation Status:** **ALREADY SUPPORTED - No new implementation needed**

**Actor:** Professor/Instructor

**Preconditions:** 
A test suite is selected and configured. The professor has access to multiple folders containing student submissions (e.g., one folder for first submissions, another for resubmissions).

**Main Flow:**
1. The professor navigates to the Main Menu and clicks "Change Root Folder / Back to Start". This allows them to change which folder of student submissions the system will use.

2. The system displays the folder selection screen, where the professor can browse to or type the path of a different folder containing student submissions. For example, if they previously tested "first_submissions", they might now select "resubmissions".

3. Once a new folder is selected, the system updates the `Coordinator.rootFolder` attribute and returns to the Main Menu.

4. The professor then clicks "Execute Test Suite" from the Main Menu. The system validates that a test suite is selected and that it contains test cases.

5. The system executes the same test suite (the one that was previously selected) on the new folder of submissions. This means the exact same test cases are run against a potentially different set of student code.

6. During execution, the system stores the execution metadata (root folder path and code path) so that when results are saved, they include information about which folder was used.

**Postconditions:** 
New execution results are generated from the new folder. These results can then be saved using the "Save Results (Serialized)" functionality, creating a second result file that can be compared with the first submission results.

**Why This Use Case Was Already Supported:**
The ability to execute a test suite on different folders was already present in Version 1. The system always allowed professors to change the root folder and re-execute. What Version 2 added was the ability to save these results and compare them, but the core execution-on-different-folders capability was already there. This demonstrates that the resubmission workflow was architecturally feasible from the start - Version 2 just added the persistence and comparison layers on top.

---

## 4. Updated Class Diagram Relationships

### New Relationships to Add:

**1. Coordinator → TestExecutionResults (Association, 1-to-many)**
- **Type:** Association (one-to-many)
- **Direction:** Coordinator creates and manages TestExecutionResults objects
- **Label:** "saves/loads"
- **Description:** The `Coordinator` class has the responsibility of creating `TestExecutionResults` objects when results need to be saved, and of loading them when results need to be retrieved. This is a one-to-many relationship because a single `Coordinator` instance can create and manage multiple `TestExecutionResults` objects over time (one for each execution session that gets saved). The relationship is an association rather than composition because `TestExecutionResults` objects have an independent existence once saved to disk - they can be loaded even if the `Coordinator` that created them no longer exists.

**2. TestExecutionResults → TestResult (Composition, 1-to-many)**
- **Type:** Composition (one-to-many)
- **Direction:** TestExecutionResults contains TestResult objects
- **Label:** "contains"
- **Description:** This is a composition relationship because `TestResult` objects are integral parts of a `TestExecutionResults` object. When a `TestExecutionResults` is created, it owns a collection of `TestResult` objects. When the `TestExecutionResults` is serialized, all its `TestResult` objects are serialized with it. If a `TestExecutionResults` object is deleted, its associated `TestResult` objects are conceptually deleted as well (though in practice, they're just part of the serialized data). This relationship ensures that test results are always associated with their execution context.

**3. TestExecutionResults → TestSuite (Association, many-to-1, by reference)**
- **Type:** Association (many-to-one, by string reference)
- **Direction:** TestExecutionResults references TestSuite by storing its title
- **Label:** "references"
- **Description:** Each `TestExecutionResults` object stores the title of the `TestSuite` that was executed, creating a reference to that suite. This is a many-to-one relationship because multiple execution result sets can reference the same test suite (e.g., first submission results and resubmission results might both use the same suite). The relationship uses a string reference (the suite title) rather than a direct object reference, which is appropriate for serialization - when results are saved and loaded later, the suite object might not exist in memory, but we can still identify which suite was used by its title.

---

## 5. Updated Sequence Diagrams

### 5.1 Save Execution Results Sequence
**Implementation Status:** **NEW SEQUENCE - Added in Version 2**

**Participants:** Ui, Coordinator, TestExecutionResults, File System

**Description:** This sequence diagram illustrates the interaction flow when a professor saves test execution results. The sequence begins when the professor clicks the save button in the UI, and ends when the save operation is confirmed.

**Sequence Steps:**
1. **Ui → Coordinator:** The UI calls `Coordinator.saveTestExecutionResults()`, passing the `TestExecutionResults` object to be saved and the `File` object representing where to save it. The UI has already created the `TestExecutionResults` object using the current results and execution metadata.

2. **Coordinator → TestExecutionResults:** The Coordinator receives the `TestExecutionResults` object. At this point, the object is fully constructed with all its data (results list, metadata, etc.).

3. **Coordinator → File System:** The Coordinator creates an `ObjectOutputStream` connected to the specified file and calls `writeObject()` on the `TestExecutionResults` object. This triggers Java's serialization mechanism, which recursively serializes the `TestExecutionResults` object and all its nested `TestResult` objects into a byte stream that is written to the file.

4. **File System → Coordinator:** The file system completes the write operation and the `ObjectOutputStream` is closed. If the operation was successful, control returns to the Coordinator. If an error occurred (e.g., disk full, permission denied), an exception is thrown.

5. **Coordinator → Ui:** The Coordinator returns control to the UI, either with a success confirmation or an exception that the UI handles by displaying an error message to the professor.

**Key Points:**
- The entire object graph (TestExecutionResults + all TestResult objects) is serialized atomically
- If any part of the serialization fails, the entire operation fails (no partial saves)
- The file is written in binary format, not human-readable text

---

### 5.2 Compare Results Sequence
**Implementation Status:** **NEW SEQUENCE - Added in Version 2**

**Participants:** Ui, Coordinator, TestExecutionResults (x2), File System

**Description:** This sequence diagram shows the interaction flow when a professor compares two result files. The sequence involves loading two separate result files, calculating success rates for each, and displaying the comparison.

**Sequence Steps:**
1. **Ui → Coordinator:** The UI initiates the comparison by calling `Coordinator.loadTestExecutionResults()` with the first file selected by the professor. This is the first of two file loads required for comparison.

2. **Coordinator → File System:** The Coordinator creates an `ObjectInputStream` connected to the first file and calls `readObject()`, which deserializes the `TestExecutionResults` object from the file, reconstructing it exactly as it was when saved.

3. **File System → Coordinator:** The deserialized `TestExecutionResults` object is returned to the Coordinator, which then returns it to the UI. The UI stores this first result set temporarily.

4. **Ui → Coordinator:** After the professor selects the second file, the UI calls `Coordinator.loadTestExecutionResults()` again, this time with the second file.

5. **Coordinator → File System:** The Coordinator deserializes the second `TestExecutionResults` object from the second file, following the same process as steps 2-3.

6. **File System → Coordinator:** The second `TestExecutionResults` object is returned to the Coordinator and then to the UI. Now the UI has both result sets in memory.

7. **Coordinator → TestExecutionResults (calculation):** The UI calls a helper method `calculateSuccessRates()` for each `TestExecutionResults` object. This method iterates through all results, groups them by student, counts passed tests, and calculates success rates as fractions. It also handles special cases (compile errors, missing students).

8. **Coordinator → Ui:** The UI receives the calculated success rates for both result sets (as Maps from student name to success rate string). The UI then formats and displays these in a side-by-side comparison view.

**Key Points:**
- Both result files must be successfully loaded before comparison can occur
- The comparison logic is performed in memory after both files are loaded
- The calculation handles edge cases (missing students, compile errors) during the success rate computation phase

---

## 6. State Diagram Modifications

### New States to Add:

**1. Results Saved State**
- **Description:** This state is entered after a professor successfully saves test execution results to a serialized file. The system has persisted the execution session to disk, and the professor can now close the application knowing the results are preserved. From this state, the professor can return to the Main Menu, load other results, or exit the application.
- **Entry Condition:** A `TestExecutionResults` object has been successfully serialized to a file
- **Exit Transitions:** Can return to Main Menu, can load other results, or can exit application

**2. Results Loaded State**
- **Description:** This state is entered when a professor loads a previously saved result file. The system has deserialized a `TestExecutionResults` object and is displaying it in a read-only text area. The professor can review the results but cannot modify them. This state allows professors to review past execution sessions without needing to re-run test suites.
- **Entry Condition:** A `TestExecutionResults` object has been successfully deserialized from a file and is being displayed
- **Exit Transitions:** Can return to Results Manager, can load another result file, or can navigate to Main Menu

**3. Comparison View State**
- **Description:** This state is entered when a professor is viewing a side-by-side comparison of two result sets. The system has loaded both result files, calculated success rates for each student in both sets, and is displaying them in a comparison view. This is the core state for evaluating student improvement between submission rounds.
- **Entry Condition:** Two `TestExecutionResults` objects have been loaded and their success rates have been calculated and displayed
- **Exit Transitions:** Can return to Results Manager to load different files for comparison, or can navigate to Main Menu

### New Transitions to Add:

**1. Execute Test Suite → Save Results → Results Saved State**
- **Description:** After executing a test suite and viewing the results, the professor can choose to save those results. When they click "Save Results (Serialized)" and the save operation completes successfully, the system transitions to the Results Saved State. This transition represents the persistence of execution data.

**2. Main Menu → Load Results → Results Loaded State**
- **Description:** From the Main Menu, the professor can navigate to Results Manager and select "Load Saved Results". After selecting a file and successfully loading it, the system transitions to the Results Loaded State, where the results are displayed.

**3. Main Menu → Compare Results → Comparison View State**
- **Description:** From the Main Menu, the professor can navigate to Results Manager and select "Compare Two Result Files". After selecting both files and successfully loading and processing them, the system transitions to the Comparison View State.

**Impact on Existing State Diagram:**
The existing state diagram from Version 1 remains largely unchanged. The new states are additional branches that extend from the Main Menu state and the Results Screen state. The core execution flow (Welcome → Folder Selection → Main Menu → Test Manager/Execute) remains the same, ensuring that existing functionality is preserved.

---

## 7. Data Flow Diagram Updates

### New Data Stores:

**Serialized Results Files (.ser)**
- **Type:** Persistent data store (file system)
- **Description:** This is a new data store that holds serialized `TestExecutionResults` objects. These files are created when professors save execution results and can be read back later to restore complete execution sessions. The files are stored in binary format (Java serialization format) and are not human-readable, but they preserve the complete object structure including all nested `TestResult` objects and all metadata. These files serve as the persistent memory of the system, allowing execution results to survive application restarts and enabling comparison between different execution sessions.
- **Location:** User-specified file system location (professors choose where to save)
- **Format:** Java serialized object format (binary)
- **Lifetime:** Indefinite - files persist until explicitly deleted by the user

### New Data Flows:

**1. Test Results → Serialization → Serialized Results Files**
- **Source:** In-memory `TestResult` objects (from `Coordinator.lastExecutionResults`)
- **Process:** The `Coordinator.saveTestExecutionResults()` method takes the list of `TestResult` objects along with execution metadata and creates a `TestExecutionResults` object. This object is then serialized using Java's `ObjectOutputStream`, which converts the object graph into a byte stream.
- **Destination:** Serialized Results Files (.ser files on disk)
- **Direction:** Write operation (data flows from memory to disk)
- **Trigger:** Professor clicks "Save Results (Serialized)" button
- **Data Content:** Complete `TestExecutionResults` object including all `TestResult` objects, test suite title, root folder path, code path, and total test cases count

**2. Serialized Results Files → Deserialization → Test Results**
- **Source:** Serialized Results Files (.ser files on disk)
- **Process:** The `Coordinator.loadTestExecutionResults()` method reads the file using Java's `ObjectInputStream`, which deserializes the byte stream back into a `TestExecutionResults` object, reconstructing all nested `TestResult` objects.
- **Destination:** In-memory `TestExecutionResults` object (which contains `List<TestResult>`)
- **Direction:** Read operation (data flows from disk to memory)
- **Trigger:** Professor clicks "Load Saved Results" and selects a file
- **Data Content:** Complete `TestExecutionResults` object with all its data restored exactly as it was when saved

**3. Serialized Results Files (x2) → Comparison Logic → Comparison Display**
- **Source:** Two separate Serialized Results Files (typically first submission and resubmission)
- **Process:** Both files are deserialized into `TestExecutionResults` objects. Then, for each object, the system calculates success rates by:
  - Grouping results by student name
  - Counting passed tests for each student
  - Calculating fraction: passed / total test cases
  - Handling special cases (compile errors, missing students)
- **Destination:** Comparison Display (side-by-side text areas in UI)
- **Direction:** Read and transform operation (data flows from disk, through calculation logic, to display)
- **Trigger:** Professor clicks "Compare Two Result Files" and selects both files
- **Data Content:** Success rate strings (e.g., "3/5", "COMPILE ERROR", "No submission") for each student from both result sets, formatted for side-by-side display

### Impact on Existing Data Flows:
All existing data flows from Version 1 remain unchanged. The new data flows are additional paths that extend the system's capabilities without interfering with existing functionality. The original flow of Test Cases → Execution → Test Results → Display still works exactly as before, with the new serialization flows providing an additional persistence layer on top.

---

## 8. Key Design Decisions

### 8.1 Serialization Approach
**Decision:** Use Java's built-in `Serializable` interface for object persistence.

**Rationale:** The requirements explicitly suggested using object serialization, stating "if you use object serialization, this can be done very easily." Java's `Serializable` interface provides a straightforward mechanism for converting object graphs into byte streams that can be written to files and later reconstructed. This approach requires minimal code - classes simply need to implement `Serializable` and the Java runtime handles the rest.

**Alternatives Considered:** 
- JSON serialization: Would require additional libraries and manual serialization code
- XML serialization: More verbose and requires parsing libraries
- Custom binary format: Would require implementing serialization logic from scratch

**Trade-offs:** 
- **Pros:** Simple to implement, handles nested objects automatically, preserves object structure exactly
- **Cons:** Binary format is not human-readable, version compatibility requires careful management of `serialVersionUID`

**Implementation Impact:** This decision meant that only `TestResult` and `TestExecutionResults` needed to implement `Serializable`, and the `Coordinator` class needed simple save/load methods using `ObjectOutputStream` and `ObjectInputStream`. The entire implementation was straightforward and required minimal changes to existing code.

---

### 8.2 Success Rate Calculation and Display Format
**Decision:** Calculate success rate as (number of passed tests) / (total test cases in suite) and display as a fraction (e.g., "3/5") rather than as a percentage or decimal.

**Rationale:** The requirements specifically stated that "the success rate is defined as the number of test cases passed divided by the total number of test cases in the test suite" and that "the display would be such fraction, not as a single number or percentage." This format provides clear information about both the numerator (tests passed) and denominator (total tests), making it easy to understand the student's performance at a glance.

**Example:** A student who passed 3 out of 5 tests is displayed as "3/5" rather than "60%" or "0.6". This makes it immediately clear that there were 5 tests total and the student passed 3 of them.

**Implementation Details:** The calculation is performed in the `calculateSuccessRates()` helper method, which groups results by student, counts the number of results with status "PASSED", and divides by the total number of test cases in the suite (stored in `TestExecutionResults.totalTestCases`).

---

### 8.3 Handling Missing Students in Comparison
**Decision:** When comparing two result sets, if a student exists in one set but not the other, display "No submission" for that student on the side where they are missing.

**Rationale:** In a resubmission scenario, it's common for some students to provide a resubmission while others do not. Simply omitting the student from the comparison would be confusing - the professor wouldn't know if the student was missing from the first submission or the resubmission. By explicitly displaying "No submission", the system clearly communicates that the student did not provide a submission for that particular round.

**Example:** If "Alice" appears in the first submission results with "3/5" but doesn't appear in the resubmission results, the comparison would show:
- First Submission: Alice | 3/5
- Second Submission: Alice | No submission

This makes it immediately clear that Alice did not submit a resubmission.

**Implementation:** The comparison logic creates a set of all unique student names from both result sets, then for each student, looks up their success rate in each set. If a student is not found in a particular set, "No submission" is used as the value.

---

### 8.4 Compile Error Handling in Success Rate Display
**Decision:** If all test cases for a student resulted in compilation errors, display "COMPILE ERROR" instead of a fraction like "0/5".

**Rationale:** Displaying "0/5" for a student whose code doesn't compile would be misleading - it suggests the code ran but failed all tests, when in reality the code never ran at all. The "COMPILE ERROR" message provides more meaningful feedback, clearly indicating that the issue was compilation failure rather than test case failure. This helps professors quickly identify students who need help with basic compilation issues versus those whose code runs but has logical errors.

**Example:** If a student's code fails to compile for all 5 test cases, the display shows "COMPILE ERROR" rather than "0/5", making it clear that the problem is at the compilation stage.

**Implementation:** The `calculateSuccessRates()` method checks if all valid results for a student have status "COMPILE ERROR". If so, it returns "COMPILE ERROR" instead of calculating a fraction. This check happens before the success rate calculation, ensuring that compile errors are handled as a special case.

---

### 8.5 Multiple File Support (Requirement #5)
**Decision:** No changes needed - existing implementation already satisfied the requirement.

**Rationale:** Analysis of the Version 1 codebase revealed that the system already handled multiple Java files correctly:
- `ListOfPrograms.findEntryPointFile()` already searched through all Java files to find the one containing `public static void main(`
- `Program.compile()` already used `javac` on the main file, which automatically compiles all dependent files in the same directory (standard Java compiler behavior)

**Verification:** The requirement states "if you use the command 'javac' on the file containing the main method, all other required files in the same folder will be compiled." This is exactly what the existing `Program.compile()` method does - it calls `javac` on the main file, and Java's compiler handles dependencies automatically.

**Impact:** This decision meant that Requirement #5 required zero implementation effort - it was already fully supported. This demonstrates good forward-thinking in the Version 1 design, as the architecture was flexible enough to handle multiple-file submissions without modification.

---

## 9. Summary of Changes

### Overview
Version 2 represents a focused enhancement to the existing system, adding persistence and comparison capabilities while leveraging much of the existing architecture. The changes were designed to be minimal yet comprehensive, ensuring that the new features integrate seamlessly with the Version 1 foundation.

### Quantitative Summary
- **New Classes:** 1 (`TestExecutionResults`)
- **Modified Classes:** 2 (`TestResult`, `Coordinator`)
- **New Use Cases:** 4 (Save Results, Load Results, Compare Results, Execute on Different Folder - though the last was already supported)
- **New Relationships:** 3 (Coordinator-TestExecutionResults, TestExecutionResults-TestResult, TestExecutionResults-TestSuite)
- **Interface Implementations:** 1 (`TestResult` implements `Serializable`)
- **New Methods:** 4 in Coordinator (save/load results, get execution metadata)

### What Was Already There
The analysis revealed that several requirements were already satisfied by Version 1:
- **Requirement #2 (Execute on Different Folder):** The system already supported changing the root folder and re-executing test suites. No new implementation was needed.
- **Requirement #5 (Multiple File Support):** The `ListOfPrograms` class already searched through all Java files to find the main method, and the `Program` class's compilation process already handled multiple files correctly through Java's automatic dependency compilation.

### What Was Added
The new functionality focused on three main areas:
1. **Persistence:** The ability to save and reload complete execution sessions using object serialization
2. **Visualization:** The ability to view previously saved results in a simple text-based interface
3. **Comparison:** The ability to compare success rates between two different execution sessions, with special handling for edge cases