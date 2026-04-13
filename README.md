# Automation Testing Project for E-Learning Website

This repository contains a Java automation testing project built with Selenium WebDriver and TestNG for the website `https://elearning.plt.pro.vn/`.

The project covers both admin and user flows, uses the Page Object Model, and reads most test data from JSON files.

## Features

- Automates login flows for admin and user accounts
- Tests student management (`HocVien`)
- Tests course management (`KhoaHoc`)
- Tests assignment/exam management (`BaiTap`)
- Tests end-user learning flow, forum interaction, and video conference access
- Uses JSON files to store test input data
- Generates TestNG reports in `test-output/`

## Tech Stack

- Java 11
- Selenium WebDriver 4.40.0
- TestNG
- WebDriverManager
- JSON Simple
- Eclipse project structure

## Project Structure

```text
src/
  Admin/
    Pages/
    Testcases/
  User/
    Pages/
    Testcases/
  Login/
  General/
  Test/
  Utils/

resources/   -> main JSON test data and upload files
data/        -> additional JSON data
lib/         -> local jar dependencies
bin/         -> compiled output
test-output/ -> TestNG reports
video/       -> video artifacts if generated
```

## Main Test Suites

These classes are the main entry points for the full flows:

- `src/Test/TestHocVien.java` - full student management flow
- `src/Test/TestKhoaHoc.java` - full course management flow
- `src/Test/TestBaiTap.java` - full assignment flow
- `src/Test/TestUser.java` - full user flow

Other smaller test classes are available in:

- `src/Login/`
- `src/General/Testcases/`
- `src/Admin/Testcases/`
- `src/User/Testcases/`

## Prerequisites

Before running the project, make sure you have:

- Java JDK 11 installed
- Google Chrome installed
- Eclipse IDE for Java Developers
- TestNG plugin installed in Eclipse
- Internet access to the target website

## Important Notes Before Using This Repo

This project is runnable, but there are a few things a GitHub user should update after cloning:

1. The current `.classpath` uses absolute Windows paths from the original machine.
2. Some tests contain hardcoded login credentials in Java files and JSON files.
3. Some uploaded files use machine-specific paths, especially in `resources/khoahoc.json`.
4. A few upload flows use Java `Robot`, so the desktop must stay active while the test is running.

If you plan to publish this repository publicly, replace the real test credentials before pushing.

## Setup

### 1. Clone the repository

```bash
git clone <your-repository-url>
cd Lop0102_Nhom3_POL_AT_ver1.5
```

### 2. Import into Eclipse

1. Open Eclipse
2. Select `File > Import > Existing Projects into Workspace`
3. Choose the cloned project folder
4. Finish the import

### 3. Fix the library references

Because `.classpath` currently points to the original local machine, you may need to re-attach the jars:

1. Right click the project
2. Select `Build Path > Configure Build Path`
3. Remove broken library entries if Eclipse shows them as missing
4. Add all jars from:
   - `lib/`
   - `lib/selenium-java-4.40.0/`
5. Make sure `TestNG` is available in the project libraries

### 4. Update test credentials

Review and replace account data in these files before running:

- `resources/loginData.json`
- `src/Login/LoginAdmin.java`
- `src/Login/LoginCustomer.java`
- `src/Test/TestHocVien.java`
- `src/Test/TestKhoaHoc.java`
- `src/Test/TestBaiTap.java`

If your environment uses different test users, update those values first.

### 5. Update file upload paths

Some upload tests depend on files inside `resources/`.

Most tests already build paths from `System.getProperty("user.dir")`, but `resources/khoahoc.json` currently contains an absolute local path in `anhBia`.

Change it to a valid path on your machine before running the course creation flow.

Example:

```json
[
  {
    "tenKhoaHoc": "Test nhóm 3",
    "moTa": "Khóa học kiểm thử tự động Selenium",
    "anhBia": "C:\\path\\to\\your\\project\\resources\\anh1.png"
  }
]
```

## How to Run the Tests

### Option 1: Run from Eclipse

This is the recommended way for this project.

1. Open one of the main suite classes in `src/Test/`
2. Right click the file
3. Choose `Run As > TestNG Test`

Recommended starting points:

- `TestHocVien`
- `TestKhoaHoc`
- `TestBaiTap`
- `TestUser`

### Option 2: Run individual test classes

You can also run smaller classes such as:

- `LoginAdmin`
- `LoginCustomer`
- `LoginTestJSON`
- any class inside `src/Admin/Testcases/`
- any class inside `src/User/Testcases/`

## Test Data Files

Main data files used by the automation:

- `resources/loginData.json` - login accounts
- `resources/hocvien.json` - student data
- `resources/khoahoc.json` - course data
- `resources/them_xoa_bai_tap_tong_hop.json` - full assignment flow data
- `resources/them_bai_tap_moi.json` - create assignment data
- `resources/userflow.json` - end-user flow data
- `data/` - additional JSON files for specific flows

If you want to test with your own data, update these JSON files first.

## Output and Reports

After running TestNG tests, check:

- `test-output/` for HTML reports
- Eclipse Console for step-by-step logs
- `video/` if your environment creates video recordings

## Known Limitations

- No Maven or Gradle build file yet, so dependency setup is manual
- `.classpath` is not portable across machines in its current form
- Some credentials are hardcoded
- Some flows depend on fixed UI locators and `Thread.sleep`
- File upload by `Robot` can fail if the machine loses focus or runs headless

## Recommended Improvements

If you want to make this repository easier for GitHub users, the next good upgrades would be:

- move credentials and base URL into a config file
- replace absolute file paths with relative project paths
- migrate dependencies to Maven or Gradle
- add a `testng.xml` suite file
- add screenshots on failure
- replace `Thread.sleep` with explicit waits where possible

## Author Usage Tip

If someone clones this project and wants the fastest successful setup, the safest path is:

1. Import into Eclipse
2. Reconnect the jars from `lib/`
3. Install TestNG
4. Replace credentials
5. Fix the upload path in `resources/khoahoc.json`
6. Run one of the classes inside `src/Test/`

Add your preferred license here, for example MIT, Apache-2.0, or a private academic/project license.
