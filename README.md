# TestBuddy

<!-- Plugin description -->
The IDE plugin that helps you during testing.
<!-- Plugin description end -->

Some of the features in the plugin:

- **Easy copy and paste:** Have you noticed how much you copy and paste when writing test code? This is indeed a common behavior among developers, especially when they want to reuse the skeleton of the previous test code to write the next one. TestBuddy will offer an easy way for developers to create a new test case based on a previous test case. TestBuddy will then highlight the main parts that the developer needs to change, e.g., all the input values and assertions.
  
- **Code coverage history:** Developers tend to make use of code coverage reports to look for what to test next. However, for now, the process is a bit cumbersome: IntelliJ users have to click at "Run with Coverage" at the right test suite, then, look at the coverage information. And when they run it again, they cannot easily see what changed in between the two runs. TestBuddy will run the coverage report in background and will report to users what has changed between one run and the other.
  
- **Traceability between test and production:** When facing a failing test, a common behavior by the developers is to understand precisely what the test code exercises in the production code. TestBuddy will highlight precisely what lines are covered in the production code by a single test.
  
- **Assertion suggestion:** When testing object-oriented systems, a method may modify several attributes/fields of the class. TestBuddy will ensure that developers are asserting all the fields that were modified by the method under test.
  
- **Testing checklist:** It is common for developers to forget to test for special cases. For example, a method that receives a string may benefit from tests that exercise the code when an empty string is passed. A method that contains a loop may benefit from tests that exercise the loop 0, 1, and many times. TestBuddy will generate a simple checklist, based on the source code, to remember developers about those test cases.
  
- **Test code smells:** Writing maintainable test code can be challenging. TestBuddy will remind developers about test code best practices and warn them whenever a test smell appears, e.g., no assertions or too many assertions.
