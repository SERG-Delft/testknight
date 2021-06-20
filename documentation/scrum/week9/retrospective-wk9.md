# Sprint retrospective week 9

- **Scrum Master**: Cristian Botocan
- **Note Taker**: Mathanrajan Sundarrajan

## Tasks

- All team members:
    - [x] Improve the documentation for the wiki
    - [x] Work on the report
    - [x] Test the final verision of the plugin for the possible bugs
    - [x] Migrating to GitHub
    - [x] Create a fist draft skeleton for the presentation

- **Jorge**:
    - [x] Finish TestKnight testing

- **Cristian**:
    - [x] Refine TestKnight before publishing
    - [x] Update landing page

- **Pavlos**:
    - [x] Deploy the server

- **Piyush**:
    - [x] Refine TestKnight before publishing
    - [x] Finish TestKnight testing

- **Mathan**:
    - [x] Refine TestKnight before publishing
    - [x] Change plugin name in code
    - [x] Improve auto-update feature of Test List
    - [x] Add support for 2021 release

## Main Problems Encountered/ Highlights
- **All team members:**
    - Since we had the deadline for the report, we were involved in writing and proofreading it.
    - We improved the documentation and the wiki of the plugin because we want to deliver a good quality product.
    - We worked together for migrating the product to GitHub since this was one of the last client requirement. Everyone has done research into that and help during the process of migration.
    - We schedule some time for testing the plugin ourselves by trying to find different bugs, but we did not find any additional problems.

- **Jorge:**
    - This week most of the time was spent working on the report. Regarding code, I contributed to increasing the testing coverage and reviewing the refinement issue. This week went quite smoothly although in retrospect work regarding deployment should have started working on the deployment issues while working on the report rather than leaving them to the last minute.

- **Cristian:**
    - This week I was the Scrum master, so I have also to arrange the meetings, create issues, and write the agenda and retrospective. Moreover, I was helping with the refinements for the plugin and I also added the content for the landing page. Lastly, I have help and come up with ideas for the draft of the presentation skeleton.

- **Pavlos:**
    - This week I did not have any code related issues to attend to. For the first half of the week I worked mostly on the report. Then I had to deploy the server. Here I and Jorge did face some issues. Although we managed to create docker images and containers for everything we run into an issue with the ports in the server. This unfortunately we cannot resolve on our own and now have to wait for IT support.

- **Piyush:**
    - This week was mostly spent on finishing up the report as we had the final submission. In the later half of the week, I worked on the refining issue which involved fixing 2 bugs - the checklist classes being reloaded although they were already present in the tool window because of a misplaced break and also a coverage service bug with incorrect test class filtering. After that I proceeded to write tests for the `ChecklistTreeService` delete method which was slightly time consuming because of the large number of attributes it manipulated. Finally, I worked on organising the final presentation which is next week.

- **Mathan:**
    - This week I took care of some final refinements which still needed to be done for the plugin before deployment. I also worked on few sections on the report as we all worked on it together. While working on the final refinements, we noticed that our plugin didn't work on IntelliJ 2021.1 with a cryptic exception being thrown regarding ToolWindow and TabbedPane. I later found that it was due to the constructor for the TabbedPane (the UI component in toolwindow which shows everything) having some incompatibility issue. This issue was immediately fixed and we ensured that all the other features worked as intended in the 2021.1 version as well


## What can we improve?

- In regards to the presentation, I think that we can build upon the first skeleton draft of the presentation.

## State of the Project

- We can say that the project is almost finalised (we have just to wait the reponse from the IT team regarding the server deployment).