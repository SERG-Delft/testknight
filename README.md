# TestBuddy

The repository includes the TestBuddy's plugin code as well as the code of the telemetry server used to collect
usage information.

<!-- Plugin description -->
## What is TestBuddy?

TestBuddy is an all in one plugin for IntelliJ IDEA that helps you write automatic tests for your codebase.
Specifically, TestBuddy extends your IDE to allow you to design test-suites based on structural and side-effect
analysis of your code as well as inspect how your test-suite evolves.`

## Key Features:

* Easily duplicate an existing test method and change the values of often-changed code elements such as constructor arguments and literals.
 
<img src="https://jorgerom.eu/assets/testKnight/duplicate-test.gif" width="600">

* Navigate between the test cases within a file

<img src="https://jorgerom.eu/assets/testKnight/test-list.gif" width="600">

* Generate testing checklists based on structural and black-box testing

<img src="https://jorgerom.eu/assets/testKnight/testing-checklist.png" width=600>

* See the newly covered lines
  
<img src="https://jorgerom.eu/assets/testKnight/diff-cov.png" width=600>

* Get assertion suggestion based on side-effect analysis

<img src="https://jorgerom.eu/assets/testKnight/assertion-suggestions.gif" width=600>

* Trace the production code exercised by tests

<img src="https://jorgerom.eu/assets/testKnight/traceability.gif" width=600>

TestBuddy is designed with the user in mind.
We have strived to create a tool that is intuitive to use and unintrusive. That means no annoying pop-ups while coding, no error messages and no breaking your flow.
<!-- Plugin description end -->

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
