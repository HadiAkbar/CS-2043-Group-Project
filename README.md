# CS-2043 Group Project - Automated Testing Tool

A JavaFX-based automated testing application designed to help professors and instructors efficiently grade student programming assignments. The tool enables creation of test cases, execution of test suites on multiple student submissions, and comparison of results across different submission rounds.

---

## Table of Contents

- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Documentation](#documentation)
- [Group Members](#group-members)
- [Contributions](#contributions)

---

## Features

### Test Management
- **Create and manage test cases** with custom input data, expected outputs, and test types
- **Build test suites** by combining multiple test cases
- **Edit and delete** test cases and suites as needed

### Test Execution
- **Batch testing** of multiple student submissions simultaneously
- **Automatic compilation** and execution of Java programs
- **Support for nested folder structures** with configurable code paths
- **Automatic detection** of main method entry points

### Results Management
- **View detailed test results** grouped by student
- **Side-by-side comparison** of expected vs actual outputs
- **Save results** in both text and serialized formats
- **Load and review** previously saved test results

### Comparison Tools
- **Compare two result files** side-by-side (e.g., initial submissions vs resubmissions)
- **Success rate calculation** displayed as fractions (e.g., "3/5")
- **Special case handling** for compile errors and missing submissions

---

## Requirements

- **Java Development Kit (JDK)** 8 or higher
- **JavaFX** (included with JDK 8+, or separate installation for JDK 11+)
- **BlueJ** (optional, for development)

---

## Installation

1. **Clone or download** this repository
2. **Ensure Java and JavaFX are installed** on your system
3. **Navigate to the project directory**

### Running the Application

**Option 1: Using BlueJ**
- Open the project in BlueJ
- Compile all classes
- Right-click on `Main` and select "void main(String[] args)"

**Option 2: Command Line**
```bash
# Compile all Java files
javac *.java

# Run the application
java Main
```

---

## Usage

### Quick Start

1. **Launch the application** - You'll see the welcome screen
2. **Select student submissions folder** - Choose the root directory containing all student submission folders
3. **Access Test Manager** - Create or manage test cases and test suites
4. **Execute Test Suite** - Run tests on all student submissions
5. **View and save results** - Review results and save them for later comparison

### Detailed Walkthrough

For a comprehensive step-by-step guide, see the [Complete Application Walkthrough](Documentation/WalkthroughSubmission4.MD) in the Documentation folder.

### Key Workflows

#### Creating a Test Suite
1. Navigate to **Test Manager**
2. Select or create a test directory
3. Create test cases with input data and expected outputs
4. Create a test suite and add test cases to it
5. Save the suite

#### Testing Student Submissions
1. From the main menu, click **Execute Test Suite**
2. (Optional) Specify a code path if student code is in a subfolder
3. Click **Execute Test Suite** to run tests
4. Review results in the results screen

#### Comparing Resubmissions
1. Test initial submissions and save results
2. Change root folder to resubmissions folder
3. Execute the same test suite on resubmissions
4. Save the new results
5. Use **Results Manager** → **Compare Two Result Files** to view side-by-side comparison

---

## Project Structure

```
CS-2043-Group-Project/
├── Main.java                 # Application entry point
├── Ui.java                   # User interface controller
├── Coordinator.java          # Core business logic orchestrator
├── Program.java              # Represents a student program
├── TestCase.java             # Individual test case model
├── TestSuite.java            # Test suite model
├── TestResult.java           # Test execution result model
├── TestExecutionResults.java # Complete execution session results
├── ListOfPrograms.java       # Manages collection of student programs
├── ListOfTestCases.java      # Manages collection of test cases
├── ListOfTestSuites.java     # Manages collection of test suites
├── Documentation/            # Project documentation
│   ├── WalkthroughSubmission4.MD
│   ├── Ver2_Requirements_Modifications.md
│   └── WorkFlowExplanation.txt
└── test/                     # Test programs and test data
    ├── Programs/
    ├── test-cases/
    └── test-suites/
```

---

## Documentation

- **[Complete Application Walkthrough](Documentation/WalkthroughSubmission4.MD)** - Detailed step-by-step usage guide
- **[Version 2 Requirements & Modifications](Documentation/Ver2_Requirements_Modifications.md)** - Technical documentation of Version 2 features
- **[Workflow Explanation](Documentation/WorkFlowExplanation.txt)** - Overview of application workflow

---

## Group Members - Group 3

- **Hadi Akbar**
- **Christian Dennis**
- **Prabhas Ghana Dulam**
- **Rudolph Stephen**

---

## Contributions

**Submission 4 Diagrams:** Mainly completed by Prabhas Ghana Dulam

---

## Notes

- Test cases are stored in `.testcase` files in the `test-cases/` directory
- Test suites are stored in `.suite` files in the `test-suites/` directory
- Serialized results are saved as `.ser` files for programmatic loading and comparison
- Text results are saved as `.txt` files for human-readable output
- The application automatically creates required subdirectories if they don't exist

---

**Course:** CS-2043  
**Project Type:** Group Project  
**Version:** 2.0 / Submission 4
