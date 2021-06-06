# Sprint retrospective week <NUM>

- **Scrum Master:** Pavlos Makridis
- **Note Taker:** Cristian Botocan

## Tasks

- **All team members:**
  - [x] Improve README
      - Add running instructions
      - Small tutorials for core features
      - Remove code smells requirement
    - [x] Restructure the Repo
        - Make it multi-module (1 pipeline for the plugin and another for the server)
        - If needed, optimize pipelines (remove any duplicate stages)
        - Add coverage tools (JaCoCo / Kotlin equivalent)
        - restructure the frontend directory structure
    - [x] Research on GitHub migration
        - GitHub CI/CD
    - [x] Create a logo
        - Ask client if they have a logo or if they want us to make one
    - [x] Update Documentation
        - Wiki for existing services
        - IntelliJ SDK Educational Document

- **Jorge:**
  - [x] Make coverage in buffer be automatic
  - [x] Develop the client
  - [x] Implement Usage Data Tracking

- **Cristian:**
  - [x] Develop the client
  - [x] Refine UI
    - icons in intentions
    - Change "CopyPaste" to "TestList"
    - Remove the extra borders present in the side panel
    - double click for goto in test list
  - [x] Handling exception
    - Fix the exception thrown when generating a checklist without opening the panel
    - Make sure too catch any exceptions thrown by backend and notify the user
  - [x] Welcome popup
    - should ask the user if they want to share their data (should link to the agreement)
  - [x] Connect the checklist to the Test Method Generation Service


- **Pavlos:**
  - [x] Implement Server with the basic endpoint(s)
  - [x] Implement request validation in the server
  - [x] Setup and connect the server with the database
    
- **Piyush:**
  - [x] Test backend logic
  - [x] Change greying out strategy for diff button
  - [x] Research on publishing the plugin
  - [x] Extend coverageDataService to ease linking statistics information


- **Mathan:**
  - [x] Add coverage statistics information to the TestBuddy side panel
  - [x] Persisting checklist tree functionality
  - [x] Change greying out strategy for diff button
    
## Main Problems Encountered

- **All team members:**
  - This week although busy, went very smoothly. We did not meet any major challenges. One interesting situation that arose 
  however, is that the name of the project ("TestBuddy") is taken by another testing tool. This means that we have to change names. This also prevented us from creating a logo this week. Next sprint, in cooperation with the clients we plan to find 
a new name, make the logo and perform all of the required changes in our documentation and code. 

- **Jorge:**
  - This week was relatively straightforward. Implementing the client and usage data logging took time but did not have any major obstacles. Regarding the automatic integrated view, I spent a lot of time trying to implement this in a very complex way (by extending the coverage engine) but ended up going for the simplest solution. 
    In hind-sight I should have decided on this simpler solution from the beginning since it does not really have any significant disadvantages over the alternative.

- **Cristian:**
  - For the first part of this week, I was busy with integrating checklist UI and test method generation. Afterward, I had to refine the UI according to the feedback which we received from the clients. 
    For this issue, I had also to do a little bit of research by reading the UI IntelliJ Guidelines. However, during the testing and reviewing phase, I have realized that I can implement more refinements (eg. logos for the actions, move the TestBuddy tool window to the top right side, etc.) Moreover, I worked on the Welcome popup which has to be shown after the installation process. Lastly, I had to implement a Handler that notifies the user in case of TestBuddy Exceptions. 
    Furthermore, for some of the exceptions, I have to change the code in such a way that the plugin avoids them.

- **Pavlos:**
  - This week's task were fairly simple for me, so I would not say I encountered any major issues. I used the designs we did in the previous week to implement the 
  telemetry server. The server is a simple Spring application so no suprises there. This proved once again to me that it is important to spend time designing thoroughly 
    before implementing. After I was done with that I moved on to write Wiki entries and other documentation.

- **Piyush:**
  -This week, I worked on testing, researching on deploying the plugin and modifying the diff coverage service after discussing with the frontend team. 
  Modifying diff coverage was quite straightforward to code. However, this required some careful thinking to adapt the existing architecture to fit in this feature without too much modification. 
  To summarise, we noticed that greying out wasn't the most optimal way of indicating that the diff view may not be accurate to the user.
  This is because the grey out would mean adding a check to the update UI method which is called twice a second. 
  To prevent workflow intrusion and to keep computations to a minimum, we switched to a timestamp based approach. This involves giving an indication to the user that source code was modified which happens when the button is clicked instead of dynamic (twice a second). I was mostly held off with testing. It took longer than I expected because I was working under the assumption that I had the final source code that I could write tests on. However, issues this week conflicted with mine and I had to scrap away some fully tested classes that I wrote. However, I was able to tests services (other than newly modified ones) for coverage over 90% in general and even improved coverage of settings to 88%. The modified classes can be tested during the cleanup next week. Overall, I was satisfied with the progress this week but I feel that I wasted some time on writing tests which I didn't use eventually. 
  I can focus on better issue planning in the future especially for test issues to avoid conflicts.

- **Mathan:**
  -This week, I worked on making a coverage statistics panel and making checklist persistent. 
  Making the checklist persistent turned out to be not as direct as we thought because there was an issue were child classes were loaded as null. 
  It took a lot of research to find out possible ways to overcome this issue and we finally found that adding a @XCollection with a list of all child classes. The other main issue I worked on is the coverage tab statistics panel. Initially we were planning to see if we could extend the existing coverage statistics table but it seemed to be more complicated than we thought. So instead, we made our own table with the coverage information we had. The final issue I worked on is the grey out diff view option. This issue was relatively easy once we found out that we can implement it using modificationStamp on virtual files. 
  But it took quite some research time to finally decide that modificationStamp would be a good approach.

## What can we improve?
We can improve a bit on our global planning so that we know, if more than of us affects the same file. 
That way we can improve on foreseeing merge conflicts and catching them earlier.

## State of the Project
All must-have, could-have and should-have requirements have been completed. 
We now only have to do some refinements and testing as well as adding some documentation. 
