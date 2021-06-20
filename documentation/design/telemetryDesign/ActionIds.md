This file includes the mapping between action IDs (stored in the telemetry server's DB) and the actions
that the user performed.

| Action ID            | Description                                                                                     |
|----------------------|-------------------------------------------------------------------------------------------------|
| duplicateTest        | The user used TestKnight to duplicate a test                                                     |
| gotoTest             | The user used TestKnight to quickly navigate to a test                                           |
| suggestAssertion     | The user used TestKnight to get assertion suggestions                                            |
| generateChecklist    | The user used TestKnight to generate a testing checklist                                         |
| splitDiffView        | The user used TestKnight to inspect how the coverage changed between two consecutive test runs.  |
| integratedDiffView   | The user used TestKnight to inspect how the coverage changed between two consecutive test runs.  |
| traceTest            | The user used TestKnight to inspect what specific lines a test covers                            |
| generateTest         | The user used TestKnight to generate a new test method from a checklist item                     |
| itemMarked           | The user marked one of the items in the generated checklist                                     |
| itemDeleted          | The user deleted one of the items in the generated checklist                                    |
| runWithCoverage      | The user run a test suite with coverage                                                         |
| testRun              | The user run a test                                                                             |
| testFail             | A test failed                                                                                   |
| testAdd              | The user added a new test                                                                       |                                                                                 |                                                                                |