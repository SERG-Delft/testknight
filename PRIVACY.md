# Privacy statement

TestKnight optionally offers a telemetry feature that enables us, researchers, to understand how the tool is used. 
Once telemetry is enabled, TestKnight collects data about its usage. 
In this document, we describe what data we collect, how we store it, and how we process it.

_Last update:_ June 22nd, 2021

## Data collection

TestKnight collects:

- Whenever a test case is duplicated
- Whenever a checklist is generated
- Whenever items of the checklist are marked as completed, renamed, removed
- Whenever the differential coverage window is shown
- Whenever assertions are suggested

## Data storage

The collected information is sent to a server hosted on-premise at TU Delft, in the Netherlands. 
Only the developers of the project have access to it. 

## Privacy

TestKnight only stores tuples of (user id, timestamp, action). 

- The user id is a randomly generated UUID. TestKnight does not know who you are and does not store any information that makes it possible for someone to link the random user id to your profile.
- The action is a simple string indicating the action (e.g., "TEST CASE DUPLICATED", "CHECK LIST GENERATED", "ASSERTIONS GENERATED"). TestKnight does not store any information related to your source code and project. More specifically, we do not store any source code, concrete items in the checklist, or coverage information.

## Data usage

This information may be used for research purposes and may be made available publicly to other researchers.

## Right to be forgotten

If you decide you do not want your data in our dataset anymore, simply send us an e-mail, and we will delete it from our servers. Note that we are also not able to know your random user id. We may need to ask you further information for the deletion to happen.
