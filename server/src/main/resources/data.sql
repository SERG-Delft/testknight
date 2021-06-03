-- Insert default action ids in the actions table (if id does not exist already)
insert ignore into actions (action_Id) values ('duplicateTest');
insert ignore into actions (action_Id) values ('gotoTest');
insert ignore into actions (action_Id) values ('suggestAssertion');
insert ignore into actions (action_Id) values ('generateChecklist');
insert ignore into actions (action_Id) values ('splitDiffView');
insert ignore into actions (action_Id) values ('integratedDiffView');
insert ignore into actions (action_Id) values ('traceTest');
insert ignore into actions (action_Id) values ('generateTest');
insert ignore into actions (action_Id) values ('itemMarked');
insert ignore into actions (action_Id) values ('itemDeleted');
insert ignore into actions (action_Id) values ('runWithCoverage');
insert ignore into actions (action_Id) values ('testRun');
insert ignore into actions (action_Id) values ('testFail');
insert ignore into actions (action_Id) values ('testAdd');