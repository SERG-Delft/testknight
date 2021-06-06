# Sprint agenda week 7

- **Scrum Master**: Pavlos Makridis
- **Note Taker**: Cristian Botocan

## Tasks

- All team members:
    - Improve README
        - Add running instructions
        - Small tutorials for core features
        - Remove code smells requirement
    - Restructure the Repo
        - Make it multi-module (1 pipeline for the plugin and another for the server)
        - If needed, optimize pipelines (remove any duplicate stages)
        - Add coverage tools (JaCoCo / Kotlin equivalent)
        - restructure the frontend directory structure
    - Research on GitHub migration 
        - GitHub CI/CD
    - Create a logo 
        - Ask client if they have a logo or if they want us to make one
    - Update Documentation 
        - Wiki for existing services
        - IntelliJ SDK Educational Document

- **Jorge**:
    - Make coverage in buffer be automatic
    - Develop the client 
    - Implement Usage Data Tracking

- Cristian:
    - Develop the client 
    - Refine UI
        - icons in intentions
        - Change "CopyPaste" to "TestList"
        - Remove the extra borders present in the side panel
        - double click for goto in test list
    - Handling exception
        - Fix the exception thrown when generating a checklist without opening the panel
        - Make sure too catch any exceptions thrown by backend and notify the user
    - Welcome popup
        - should ask the user if they want to share their data (should link to the agreement)
    - Connect the checklist to the Test Method Generation Service 

- Pavlos:
    - Implement Server with the basic endpoint(s)
    - Implement request validation in the server
    - Setup and connect the server with the database

- Piyush:
    - Test backend logic
    - Change greying out strategy for diff button
    - Research on publishing the plugin
    - Extend coverageDataService to ease linking statistics information

- Mathan:
    - Add coverage statistics information to the TestBuddy side panel
    - Persisting checklist tree functionality
    - Change greying out strategy for diff button
