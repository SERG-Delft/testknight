# Sprint retrospective: Week 6

- **scrum master:** Jorge Romeu
- **note taker:** Pavlos Makridis

## Tasks

- **Jorge & Piyush:**
    - [x] Implement the traceability between tests and source code requirement.

- **Mathan & Cristian:**
    - [x] Create a TestBuddy settings panel.
    - [ ] Make the testing checklists persistent.

- **Cristian:**
    - [x] Extend the checklist tree with add and edit functionality.

- **Pavlos:**
    - [x] Design the client-server interface for telemetry.
    - [ ] Create an action to generate a test case skeleton given a checklist item.

- **Piyush:**
    - [x] Test the backend logic for code coverage diff.
    - [x] Detect changes in the source code in between coverage runs

- **Mathan:**
    - [ ] Add coverage statistics information to the TestBuddy side panel.

- **Jorge:**
    - [x] Link the new settings panel to the backend logic.

## Main Problems Encountered/ Highlights

- **All team members:** This last sprint was quite tough for all us. During this week we had to finish of all of our must-have requirements which we did
as well as prepare 2 presentations (one for the clients and supervisors and one for TU Write). Those presentations took a lot of time
and combined with 2 bug issues that arose mid-way and took one day each and some problems we did not anticipate really put pressure on
us. In the end we still managed to complete most of our issues (only 2 were left undone) but we learned to avoid cramming issues in
weeks that include a lot of presentation/report making.


- **UI Team (Cristian, Mathanrajan):**

    - **Mathanrajan**: This week was quite tight for me. I underestimated the time it would take me to prepare for the presentation (both client and TW presentations).
Because of that, I couldn't finish one of my issues for this week. One of the issues I worked on this week was making a working settings panel which saves the settings persistently. This issue had some unexpected problems. One of the problems were Color objects weren't getting serialized properly in XML format. Because of that,
we had to store it as a string using ColorUtil.toHex function. Similarly, the string can be converted back to Color using
ColorUtil.fromHex. One of the more interesting problem we faced for this issue was after we made the merge request for this issue.
For some reason, one of the inbuilt kotlin methods couldn't be found and searching about the error suggested that kotlin was out of date (?).
In order to fix that, we changed the non working method with getters and setters instead.

    - **Cristian**: This week I worked on the Checklist tree functionality for adding and editing the items. It was a hard issue since there is not enough documentation regarding how the tree cell editor works. Thus, I have to change the code bunch of times. Moreover, I was trying to give a solution that can be also technically correct in terms of extending the current architecture. I think that I spent more time finding this solution instead of applying the simple one from the first time. Those things can explain the delay of 5 hours for this issue.
Additionally, I also worked on the Persisting checklist tree functionality. I have written the beginning of the functionality, but the main problem encountered here is that we can not serialize a custom object directly. Thus, we have to create our own conversions functions (toString/fromString), which makes this issue much more complex. Hence, we thought that it is a good idea to postpone it to the next week.


- **Backend Team (Jorge, Piyush and Pavlos)**

    - **Jorge** This week I spent most of my time working with the traceability between test and source code feature. Additionally I spent time working on the backend side of the configuration. I also worked on resolving some minor bugs which presentd themselves in creating the demo. Overall It was a relatively straightforward week but a qute demanding one. The main thing I could have improved on was planning. The backend of the configuration depended on the frontend side which we estimated to finish on wednesday but instead finished on saturday. Hence I ended up not writing as much code as I should have in between thursday and saturday since I had already finished traceability but I was still waiting on the settings panel.

    - **Piyush** The implementation was quite stressful this week. I started off working on tracability feature, moved to the testing feature and finally took up the common backend issue of greying out the diff button. The testing issue was quite tricky since it involved multiple classes crucial for the coverage feature. Additionally, all of these involved working with existing IntelliJ objects that could not be instantiated (CoverageSuitesBundle,ProjectData,ClassData etc). Thus, the only way to properly test these was using mockK and modelling the return of each of their method calls. This was quite cumbersome to set up. Additionally, there were many issues about resolving the state for the diff view and I was left with little time to work on this because of the midterm meeting. Hence, I implemented it towards the end of the week under a deadline pressure which was quite stressful. However, I am glad that I finished all the issues I was involved with this week despite the midterm report.

    - **Pavlos** This week I had to mainly work on two tasks. First of all I had to design the telemetry feature this required a lot of thinking and talking with the clients to understand the exact requirements for it. It took a while to do but it was completed successfully. The second thing I had to do was implement the test method generation. We thought that this was a simple task but it proved to be quite the challenge. To implement it we had to remodel the way we represented the checklist. This basically meant that we had to make make refactorings in all of the generation strategies and test classes. This was in the end done but it took 2 and a half days on its own.


## What can we improve?

For the future, I think that it is better to go directly for the simpler solution, instead of trying to find a better one which is more complex. We can create refinement issues, in which we can come up with much more efficient solutions. Additionally in the start of a sprint we should take more time to estimate the time spent with an issue. This week we underestimated the amount of time needed to implement some issues such as the settings panel and test skeleton generation.

## State of the Project

We have now completed all of the must have requirements. In future weeks we will work on polishing the existing requirements and also implementing the should haves and could have features as well as the newly introduced requirements such as telemetry, migrating to github, etc.
