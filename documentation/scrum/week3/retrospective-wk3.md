# Sprint retrospective: Week 3

- **Scrum Master:** Piyush Deshmukh
- **Note Taker:** Jorge Romeu

## Tasks
- **All team members**
    - [x] Decide on heuristics for highlighting in copy-paste
    - [x] Testing of newly implemented features
- **UI Team (Cristian, Mathan)**
    - [x] Create "Load Tests" action
    - [x] Refine tool-window UI
    - [x] Create "Copy Tests" action
- **Backend Team (Jorge, Pavlos, Piyush)**
    - [x] Side effect detection
    - [x] Implement basic highlighting for copy-paste
    - [x] Implement goto functionality
    - [x] Fix test method detection bug
    - [x] Fix gradle recognition of project test methods
    - [x] Implement foundations for checklist feature



## Main Problems Encountered/ Highlights
- **All team members:**
    - It was generally difficult to understand and find proper documentation.
- **UI Team (Cristian, Mathan):**
    - The test case copy paste UI had an consistent update issue due to the way how references to test method are currently stored. We found that this can be fixed by calling the update test case UI action everytime the test code has significant changes.
    - The current UI for test case copy paste only shows one test class’ methods, it might not work if there are multiple test suites in the same file.
    - Last week, we were not satisfied by the way the checklist UI was designed. We preferred if it would be a tree structure with fixed spaces and dropdowns. However, checklist items were split to have equal space by default which looked unintuitive when we added new checklist items.

- **Backend Team (Jorge, Pavlos, Piyush):**
    - Jetbrains documentation was not up to date regarding on using the PSI system for Java files.
      It took us a while to understand what was going on but since we had anticipated issues like that we were still on time.
    - Piyush - There were no relevant hits when searching for key tasks such as scrolling to center user view on a specific part of the code. Thus, I had to resort to looking up source code of various plugins which partly used this functionality. However, this takes a lot of time compared to simple documentation as it is required to understand the plugin structure and design of the plugin being researched. In most cases, project documentation is rare so looking up multiple open-source plugins is the best way forward. We had anticipated the documentation issue for plugins. With growing experience working on plugins each week, we hope we can research these plugins faster in the future.
    - Pavlos - This week I focused mainly on implementing side effect detection. This problem proved to have many different aspects, some more difficult than others. Specifically detecting whether a method argument is mutated was proven to be more difficult and expensive (both in terms of development time and computational time) so after discussing with the client we set it aside to work on it in a later week. The final version of side effect detection however can detect thrown exceptions and class field mutations which is already a good start for our purposes.
    - Jorge - This week I focused on implementing test duplication templates. The main problem here was figuring out how to create and use templates dynamically, which was not covered in the official docs, but once I had this what was left was adapting the existing test-duplication code to use templates rather than just a naive duplication.


## What can we improve?

- We need to improve on managing tasks in a sprint. Most of the tasks very completed a few days before the end of the sprint and the rest of the time was used for research, refining code and documentation.
- From the point above, we need to derive a plan not only for each sprint but also a rough division of tasks over all sprints in general. This enables us to have a vision of the time we have for each feature and also will keep us on track for the bonus features which we planned.

## State of the Project

- We have an almost complete version (both frontend and backend) of the copy-paste feature. The UI has been laid out for the checklist feature. All the bugs detected at the end of the first sprint have been fixed.
