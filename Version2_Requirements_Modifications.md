# Modifications Required for Submission #1 to Support Version 2 Requirements

## Overview
This document outlines the modifications needed to the requirements and modeling (Submission #1) to support Version 2 features detailed in the submission 4 pdf, with particular focus on handling resubmissions and result comparison. The document describes both what we already implemented in Version 1 and what new components we are to add to meet expectations for Version 2.

---

## 1. New Classes to Add

### 1.1 TestExecutionResults
**Implementation Status:** **NEW CLASS - Added in Version 2**

**Purpose:** Encapsulates a complete test execution session for serialization and later comparison. Stores all test results plus metadata (test suite title, root folder path, code path) needed to understand execution context.

**Attributes:**
- `testSuiteTitle: String` - Name of the executed test suite
- `rootFolderPath: String` - Path to student submissions folder (identifies which submission set)
- `codePath: String` - Optional subfolder path (e.g., "src")
- `results: List<TestResult>` - All test case results for all students
- `totalTestCases: int` - Total test cases in suite (for success rate calculation)

**Relationships:**
- **Composition with TestResult (1-to-many):** Contains list of TestResult objects
- **Association with TestSuite (many-to-1):** References TestSuite by title string
- **Implements Serializable:** Enables object persistence to disk

**Methods:** Constructor plus getters for all attributes. Constructor calculates totalTestCases from unique test case titles.

**Why Needed:** Version 1 only displayed results immediately and saved as text. This class enables programmatic reloading and automated comparison between different execution sessions.

---

## 2. Modified Existing Classes

### 2.1 TestResult
**Implementation Status:** **MODIFIED - Existed in Version 1, enhanced in Version 2**

**What Was Already There:** Stored individual test case results (student name, test case title, status, actual/expected outputs). Used throughout Version 1 for immediate result display.

**What Was Added:** Implemented `Serializable` interface to enable serialization as part of `TestExecutionResults`. Added `serialVersionUID` constant for version control.

**Why Needed:** Since `TestExecutionResults` contains `List<TestResult>`, all nested objects must be serializable for the serialization chain to work.

---

### 2.2 Coordinator
**Implementation Status:** **MODIFIED - Core class from Version 1, extended in Version 2**

**What Was Already There:** Central orchestrator managing test cases, suites, and programs. Had `executeTestSuite()` method returning `List<TestResult>`.

**New Attributes:**
- `lastExecutionCodePath: String` - Stores code path from last execution
- `lastExecutionRootFolder: String` - Stores root folder from last execution

**New Methods:**
- `saveTestExecutionResults(TestExecutionResults, File): void` - Serializes results to `.ser` file using `ObjectOutputStream`
- `loadTestExecutionResults(File): TestExecutionResults` - Deserializes results from `.ser` file using `ObjectInputStream`
- `getLastExecutionCodePath(): String` - Getter for execution metadata
- `getLastExecutionRootFolder(): String` - Getter for execution metadata

**Modified Methods:**
- `executeTestSuite(String codePath)` - Now stores execution metadata (codePath and rootFolder) after execution completes

**Relationships:** One-to-many association with `TestExecutionResults` (creates and manages saved result sets)

---

### 2.3 Program
**Implementation Status:** **NO CHANGES NEEDED**

**Why:** Requirement #5 states that compiling the main file with `javac` automatically compiles dependencies. This is standard Java compiler behavior - the existing `compile()` method already handles multiple files correctly.

---

### 2.4 ListOfPrograms
**Implementation Status:** **NO CHANGES NEEDED**

**Why:** Already searches all Java files for `public static void main(` via `findEntryPointFile()` and `hasMainMethod()` methods. Satisfies requirement #5 completely.

---

## 3. New Use Cases

### 3.1 Save Test Execution Results
**Implementation Status:** **NEW USE CASE - Added in Version 2**

**Actor:** Professor/Instructor  
**Preconditions:** Test suite executed successfully, results screen displayed

**Main Flow:**
1. Professor clicks "Save Results (Serialized)" button
2. System shows file chooser (suggests filename based on suite title)
3. System creates `TestExecutionResults` object with results and metadata
4. System serializes object to `.ser` file using `ObjectOutputStream`
5. System confirms successful save

**Postconditions:** Results persisted to disk as serialized object file

---

### 3.2 Load Test Execution Results
**Implementation Status:** **NEW USE CASE - Added in Version 2**

**Actor:** Professor/Instructor  
**Preconditions:** Previously saved `.ser` file exists

**Main Flow:**
1. Professor clicks "Load Saved Results" from Results Manager
2. System shows file chooser (filters for `.ser` files)
3. System deserializes `TestExecutionResults` object using `ObjectInputStream`
4. System displays results in non-modifiable text area (includes metadata, all results grouped by student, summary)

**Postconditions:** Previously saved results displayed in read-only format

---

### 3.3 Compare Two Result Files
**Implementation Status:** **NEW USE CASE - Added in Version 2**

**Actor:** Professor/Instructor  
**Preconditions:** Two previously saved `.ser` files exist (typically first submission and resubmission)

**Main Flow:**
1. Professor clicks "Compare Two Result Files"
2. System prompts for first result file and loads it
3. System prompts for second result file and loads it
4. System calculates success rates for each student in both sets (passed/total as fraction, e.g., "3/5")
5. System displays side-by-side comparison with aligned student rows

**Special Cases:**
- Missing student in one set → Display "No submission"
- All tests compile errors → Display "COMPILE ERROR" instead of "0/total"

**Postconditions:** Side-by-side comparison displayed showing student improvement

---

### 3.4 Execute Test Suite on Different Folder
**Implementation Status:** **ALREADY SUPPORTED - No new implementation needed**

**Actor:** Professor/Instructor  
**Preconditions:** Test suite selected and configured

**Main Flow:**
1. Professor changes root folder via "Change Root Folder / Back to Start"
2. Professor clicks "Execute Test Suite"
3. System executes same suite on new folder
4. System stores execution metadata for later saving

**Postconditions:** New results generated, can be saved and compared with first submission

**Note:** This capability existed in Version 1. Version 2 added persistence/comparison on top.

---

## 4. Updated Class Diagram Relationships

**New Relationships:**
1. **Coordinator → TestExecutionResults (Association, 1-to-many):** Coordinator creates and manages TestExecutionResults objects. Label: "saves/loads"

2. **TestExecutionResults → TestResult (Composition, 1-to-many):** TestExecutionResults contains TestResult objects. Label: "contains"

3. **TestExecutionResults → TestSuite (Association, many-to-1):** TestExecutionResults references TestSuite by storing its title as string. Label: "references"

---

## 5. Updated Sequence Diagrams

### 5.1 Save Execution Results
**Participants:** Ui, Coordinator, TestExecutionResults, File System

1. Ui → Coordinator: `saveTestExecutionResults(TestExecutionResults, File)`
2. Coordinator → File System: Serialize using `ObjectOutputStream.writeObject()`
3. File System → Coordinator: Write complete
4. Coordinator → Ui: Success confirmation

### 5.2 Compare Results
**Participants:** Ui, Coordinator, TestExecutionResults (x2), File System

1. Ui → Coordinator: `loadTestExecutionResults(firstFile)`
2. Coordinator → File System: Deserialize first object
3. File System → Coordinator: Return first TestExecutionResults
4. Ui → Coordinator: `loadTestExecutionResults(secondFile)`
5. Coordinator → File System: Deserialize second object
6. File System → Coordinator: Return second TestExecutionResults
7. Ui: Calculate success rates for both sets
8. Ui: Display side-by-side comparison

---

## 6. State Diagram Modifications

**New States:**
1. **Results Saved State:** Entered after successfully saving results to `.ser` file
2. **Results Loaded State:** Entered when displaying loaded results in read-only view
3. **Comparison View State:** Entered when displaying side-by-side comparison of two result sets

**New Transitions:**
- Execute Test Suite → Save Results → Results Saved State
- Main Menu → Load Results → Results Loaded State
- Main Menu → Compare Results → Comparison View State

**Impact:** New states extend from Main Menu and Results Screen. Core execution flow unchanged.

---

## 7. Data Flow Diagram Updates

**New Data Store:**
- **Serialized Results Files (.ser):** Persistent binary files storing `TestExecutionResults` objects

**New Data Flows:**
1. **Test Results → Serialization → Serialized Results Files:** `Coordinator.saveTestExecutionResults()` serializes `TestExecutionResults` object to disk
2. **Serialized Results Files → Deserialization → Test Results:** `Coordinator.loadTestExecutionResults()` deserializes object from disk
3. **Serialized Results Files (x2) → Comparison Logic → Comparison Display:** Both files loaded, success rates calculated, displayed side-by-side

**Impact:** New flows extend system without changing existing flows.

---

## 8. Key Design Decisions

**8.1 Serialization Approach:** Use Java's `Serializable` interface (as suggested in requirements). Simple implementation, handles nested objects automatically. Trade-off: binary format not human-readable.

**8.2 Success Rate Format:** Display as fraction (e.g., "3/5") per requirements. Shows both numerator and denominator clearly.

**8.3 Missing Students:** Display "No submission" when student missing from one set. Clearly indicates no resubmission provided.

**8.4 Compile Errors:** Display "COMPILE ERROR" instead of "0/total" when all tests fail to compile. More meaningful than showing zero passed tests.

**8.5 Multiple File Support:** No changes needed. `ListOfPrograms` already searches all files for main method, and `javac` automatically compiles dependencies.

---

## 9. Summary of Changes

**Quantitative Summary:**
- New Classes: 1 (`TestExecutionResults`)
- Modified Classes: 2 (`TestResult`, `Coordinator`)
- New Use Cases: 4 (Save Results, Load Results, Compare Results, Execute on Different Folder - last already supported)
- New Relationships: 3
- Interface Implementations: 1 (`TestResult` implements `Serializable`)
- New Methods: 4 in Coordinator

**What Was Already There:**
- Requirement #2 (Execute on Different Folder): Already supported in Version 1
- Requirement #5 (Multiple File Support): Already handled by `ListOfPrograms` and Java compiler

**What Was Added:**
1. **Persistence:** Save/reload execution sessions via serialization
2. **Visualization:** View saved results in text-based interface
3. **Comparison:** Compare success rates between two execution sessions with edge case handling