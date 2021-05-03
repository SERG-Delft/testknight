# Sprint retrospective: week 2

- **Scrum Master:** Pavlos Makridis
- **Note Taker:** Cristian Botocan

## Tasks

- **UI Team (Cristian, Mathan)**
    - [x] Research UI/Actions (automatic) testing
    - [ ] Create "Load Tests" action
    - [x] Create GUI skeleton 
- **Backend Team (Jorge, Pavlos, Piyush)** 
    - [x] Start research on how to detect the important segments of a test method
    - [x] Implement basic copying of tests
    - [x] Design and Implement the LoadTestService Service


## Main Problems Encountered
- **All team members:**
    - It was generally difficult to understand and find proper documentation. 
    This was something we had anticipated but because we also had to write a project plan this week we did not have enough time to adapt to it.
    - As mentioned above we had to write a project plan which made us lose half our sprint.
- **UI Team (Cristian, Mathan):**
    - The documentation was quite hard to read and in the beginning it took some time to find a standard programming structure for UI.
    - We started with using Kotlin UI DSL but that turned out to be hard to work with in regards to dynamic code generation.
    - We had issues regarding small offsets/layout issues for the UI.
    - Testing UI is quite hard and we have to do more research on it, we will test it in the next sprint maybe.
    - There was not enough time to complete the "Create Load Tests action" issue. The basic skeleton is in place so in the next sprint we will be able to finish it quickly. 
- **Backend Team (Jorge, Pavlos, Piyush):** 
    - Jetbrains documentation was not up to date regarding on using the PSI system for Java files. 
    It took us a while to understand what was going on but since we had anticipated issues like that we were still on time.
    - Testing methodology was not clear. Again the documentation provided by JetBrains was unclear as to how to test plugins.
    Thankfully we were able to understand it after a while and implement our tests.  
    - Technical issues. We run into strange technical issues with the IDEs of some of our team members
    not understanding the project structure correctly. We are still experiencing some issues with one specific test class that we believe are either 
    due to the IDE not understanding the code correctly or due to gradle. We will have to fix this soon.


## What can we improve?

- We need to find a way to both write the reports and documentation and code at the same time.
- We need to rethink about splitting the work since it turned out UI is more difficult than expected.

## State of the Project

- We have the basic UI skeleton ready and an initial version of the duplicate tests feature.
