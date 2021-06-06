# TestBuddy

<!-- Plugin description -->
The IDE plugin that helps you during testing.
<!-- Plugin description end -->

The repository includes the TestBuddy's plugin code as well as the code of the telemetry server used to collect
usage information. 

## What is TestBuddy?

TestBuddy is an all in one plugin for the IntelliJ IDEA that helps you write automatic tests for your codebase.
Specifically, TestBuddy extends your IDE to allow you to design test-suites based on structural and side-effect 
analysis of your code as well as inspect how your test-suite evolves.

TestBuddy offers multiple functionalities designed to enhance your testing process. 
Concretely, TestBuddy allows you to:

* Easily duplicate and adapt test-cases
* Generate testing checklists based on structural and black-box testing
* Review your code coverage history
* Get assertion suggestion based on side-effect analysis
* Trace the production code exercised by tests

TestBuddy is designed with the user in mind. 
We have strived to create a tool that is intuitive to use and unintrusive. That means no annoying pop-ups while coding, no error messages and no breaking your flow.


## Some background information
TestBuddy was originally envisioned by the TU Delft Software Engineering Research Group. The project was assigned to 5 Bachelor students at the same university
as part of the CSE2000 course. The original version of it was developed within 2 months in a university GitLab instance and in the end the project was migrated
to GitHub for everyone to see. 

Development wise, the plugin is developed using Kotlin and utilizing the IntelliJ SDK whereas the telemetry server is a simple Spring Boot application. 
Lastly, we have used `Gradle` as a build tool for both the plugin and the server.

## Structure of the repository
This repository consists of 2 main submodules.
* The `/plugin` directory contains all the source code and configuration files of the actual plugin.
* The `/server` directory contains all the source code and configuration files for the telemetry server.

Additionally, the `/documentation` directory includes useful documentation and design documents. Moreover, it includes 2 educational
documents to help you get started with Kotlin and with the IntelliJ SDK.  

## Running Instructions
In this section you can find instructions on how to run the different parts of TestBuddy.

### Running the Plugin
After cloning the repository you can navigate to the `/plugin` directory with your terminal. 
From there you can run `gradle runIde`, this should open an IntelliJ instance with the plugin
already installed.

Alternatively, after cloning the project you can open it with IntelliJ IDEA and from there you can use the 'Gradle' tab 
and run options. From the tab you can go to `plugin -> Tasks -> intellij -> runIde`. Otherwise, you can set the run configuration to `Run Plugin`.

### Running the Server
Similarly to above, after cloning the repository you can navigate to the `/server` directory and run `gradle bootRun`.
Alternatively, you can open the project in IntelliJ IDEA and run the server either by the `gradle` tab by going on 
`server -> Tasks -> application -> bootRun` or by setting `TESTBUDDYTELEMETRYSERVERAPPLICATION` as the run configuration.