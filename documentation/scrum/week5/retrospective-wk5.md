# Sprint retrospective: Week 5

- **Scrum Master:** Mathanrajan Sundarrajan
- **Note Taker:** Piyush Deshmukh

## Tasks

- **All team members:**
    - [x] Code coverage

- **Frontend:**
    - [x] Link backend and frontend of Code Coverage feature
    - [x] Create Skeleton UI for code coverage

- **Backend:**
    - [x] Implement backend functionality for code coverage
    - [x] Research traceability between test code and source code

- **Jorge:**
    - [x] Improve highlighting for test duplication.
    - [x] Implement back-end for coverage diff view feature


- **Cristian:**
    - [x] Create local data structure for Checklist Tree for efficient UI updates
    - [x] Add description for assertion suggestion in the Settings tab
    - [x] Show description when hovering over create checklist button (Gutter Icon)
    - [x] Right click to edit/remove checklist drop-down menu

- **Pavlos:**
    - [x] Improve Assertion Suggestion
    - [x] Add "isTestClass" endpoint to the TestAnalyzer service
    - [x] Restrict Checklist Generation for Test methods
    - [x] Make current checklist strategies configurable
    - [x] Integration testing for checklist generation

- **Piyush:**
    - [x] Modify default generated checklist items according to client feedback
    - [x] Implement back-end for coverage diff view feature

- **Mathanrajan:**
    - [x] Show updated UI after performing action
    - [ ] ~~Show clicking animation on CopyPaste tree buttons~~*
    - [x] Add clear UI buttons
    - [x] Highlight the respective source code of the selected checklist item(s).


\* This feature was later decided as not required.

## Main Problems Encountered/ Highlights
- **All team members:**
    - All of us contributed for implementing the code coverage feature. The backend team worked on getting the coverage information from the CoverageManager and the frontend team worked on developing all the UI components required to show the coverage information.

- **UI Team (Cristian, Mathanrajan):**
    - We worked together to make the skeleton UI for code coverage. Most of the time here was spent of figuring out how to use the existing methods and talking with the backend team to find the ideal way to share our information. Once we figured that out, implementing the feature didn't take that much time.
    - **Mathanrajan**: I worked mainly on improving the user experience on existing features. For example, showing the updated UI shows the user that something had changed after their action and adding clear UI buttons allows the user to clear information which they find unnecessary/outdated. I also worked with the backend to figure out how we were going to implement the logic for code coverage and what information needs to be passed between backend and frontend. The thing that took most of the time this sprint was figuring out how to use the existing features to create the diff view as we planned for our code coverage difference. Along with that, figuring out how to add our own lines on the gutter similar to that of code coverage took equally long. Most of that work was done before actually implementing the features on code so because of that, the actual time spent was generally less that the expected time. After we figured out the basics of the functionalities we were using and implementing, the whole process became much faster. For the "Show clicking animation on CopyPaste tree buttons" issue, I found that there isn't any given UI states for "isSelected", "isRollover". Because of this, it is not possible to add any special UI for the buttons. Moreover, the JButtons in IntelliJ only add a focus border (which is in blue) when the user clicks on it. Adding that border would make the button look weird as the background is already blue as the user selected the node. We had a meeting as a team to and decided not to implement this feature in the end.
    - **Cristian**: I worked on the local Tree structure which represents a solution for loading the UI for checklist much more quicker and efficiently. Unfortunately, in the middle of the development, I have found out that the initial layout will have some consistency issues. Thus, after discussing the issue with all team members we came out with a solution that required more time for development. This was the reason for postponing the issue by one day (underestimated the difficulty of developing this data structure). Furthermore, I have also worked on other 2 issues regarding some refinements for the UI (adding the description for the intention actions and gutter icons), for which everything was very smooth in the developing process. Lastly, I developed a first version of the functionality of deleting and editing the checklist local tree and checklist UI tree.


- **Backend**
    - **Coverage (Piyush, Jorge)**
        - This week was a particularly demanding one. Since we had no documentation regarding working with coverage, we spent most of the sprint learning about the IntelliJ coverage module. We started by looking at existing plugins, which was helpful for getting the basics down. Next we implemented saving the previous coverage state. This was difficult as we encountered consistency issues, specifically we were not listening to the correct event for re-updating our data. Once they were resolved we investigated into into editor highlighting, which we needed for both the diff view (in the form of full line highlighters) and for the integrated buffer view (in the form of gutter highlighters).
        - In order to implement the feature we had to slightly modify the requirement and assume the code does not change in between coverage runs. This is because the coverage data is given in terms of lines which is very sensitive to code changes, and re-running tests is not straightforward in the intellij API, and additionally would be very slow for large test suites.
    - **Pavlos**
        - In this week I worked mainly to refactor the method analyzer service. This proved to be a bit more difficult than I expected as the code was in pretty bad shape. In the end I managed to do it by breaking the god class into multiple classes. Moreover I fixed some bugs and overall improved it. Also this week I worked on writing integrations test for the method strategy which was quicker than expected.

## What can we improve?

- In regards to the coverage feature, we made the mistake of reaching the design conclusion quite late. If we had decided this earlier we could have saved a lot of time.
- We have to analyze much more in detail some implementation parts in terms of the UI since, it can look easier to implement those at first glance, but in reality, it can take a lot of time. For instance, this week's issue for implementing the local data structure, we have found during the middle of the developing process that it can not solve some of the consistency issues. That costs a lot of time and energy, by deleting most of the code written during the first days of the sprint.

## State of the Project

- We have a working Code Coverage feature, both backend and frontend wise. It still requires some refinements UI wise, but the core logic is completely done.
- A local data structure for checklist has been added. Using this data structure provides a state which can we use to update the UI effeciently
- Almost all of our must-have requirements are completed (with the exeption of test-traceability). Thus we will spend most of our remaining time implementing should-haves and could haves, as well as refining the user interface.



