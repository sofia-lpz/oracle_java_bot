-- Delete data from tables without dropping them
DELETE FROM TODOUSER.todo;
DELETE FROM TODOUSER.sprints;
DELETE FROM TODOUSER.projects;
DELETE FROM TODOUSER.users;
DELETE FROM TODOUSER.teams;
DELETE FROM TODOUSER.states;

-- Insert data into states table
INSERT INTO TODOUSER.states (name, description, workflow_priority, creation_date, deleted) VALUES ('TODO', 'Tasks that are not yet ready for development', 1, DEFAULT, 0);
INSERT INTO TODOUSER.states (name, description, workflow_priority, creation_date, deleted) VALUES ('IN_PROGRESS', 'Tasks currently being worked on', 2, DEFAULT, 0);
INSERT INTO TODOUSER.states (name, description, workflow_priority, creation_date, deleted) VALUES ('COMPLETED', 'Tasks that have been finished', 3, DEFAULT, 0);

-- Insert data into teams table FIRST
INSERT INTO TODOUSER.teams (name, description, creation_date, deleted)
VALUES ('Alpha Team', 'Frontend development team', SYSTIMESTAMP - INTERVAL '180' DAY(3), 0);

-- Insert data into users table AFTER teams exist
INSERT INTO TODOUSER.users (phone_number, password, name, role, team_id, deleted)
VALUES ('5551234567', 'password123', 'John Smith', 'Developer',
       (SELECT id FROM TODOUSER.teams WHERE name = 'Alpha Team'), 0);

INSERT INTO TODOUSER.users (phone_number, password, name, role, team_id, deleted)
VALUES ('5552345678', 'password123', 'Sarah Johnson', 'Project Manager',
       (SELECT id FROM TODOUSER.teams WHERE name = 'Alpha Team'), 0);

INSERT INTO TODOUSER.users (phone_number, password, name, role, team_id, deleted)
VALUES ('5556789012', 'password123', 'Jessica Miller', 'Designer',
       (SELECT id FROM TODOUSER.teams WHERE name = 'Alpha Team'), 0);

-- Insert data into projects table AFTER states exist
INSERT INTO TODOUSER.projects (name, description, creation_date, due_date, state_id, deleted)
VALUES ('Web Portal Redesign', 'Complete redesign of the customer web portal',
       SYSTIMESTAMP - INTERVAL '90' DAY(3), SYSTIMESTAMP + INTERVAL '90' DAY(3),
       (SELECT id FROM TODOUSER.states WHERE name = 'IN_PROGRESS'), 0);

-- Create sprint for the project AFTER projects exist
INSERT INTO TODOUSER.sprints (name, creation_date, due_date, state_id, project_id, deleted)
VALUES ('Sprint 1', SYSTIMESTAMP - INTERVAL '85' DAY(3), SYSTIMESTAMP - INTERVAL '55' DAY(3),
       (SELECT id FROM TODOUSER.states WHERE name = 'COMPLETED'),
       (SELECT id FROM TODOUSER.projects WHERE name = 'Web Portal Redesign'), 0);

-- Todo 1: Design login page
INSERT INTO TODOUSER.todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, priority, deleted, done)
VALUES ('Design login page', 'Create mockups for the new login page design',
       SYSTIMESTAMP - INTERVAL '80' DAY(3), SYSTIMESTAMP - INTERVAL '75' DAY(3),
       (SELECT id FROM TODOUSER.states WHERE name = 'COMPLETED'),
       (SELECT id FROM TODOUSER.sprints WHERE name = 'Sprint 1'),
       (SELECT id FROM TODOUSER.users WHERE name = 'Jessica Miller'),
       (SELECT id FROM TODOUSER.projects WHERE name = 'Web Portal Redesign'),
       3, 'Medium', 0, 1);

-- Todo 2: Implement login functionality
INSERT INTO TODOUSER.todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, priority, deleted, done)
VALUES ('Implement login functionality', 'Develop the login page and authentication system',
       SYSTIMESTAMP - INTERVAL '75' DAY(3), SYSTIMESTAMP - INTERVAL '70' DAY(3),
       (SELECT id FROM TODOUSER.states WHERE name = 'COMPLETED'),
       (SELECT id FROM TODOUSER.sprints WHERE name = 'Sprint 1'),
       (SELECT id FROM TODOUSER.users WHERE name = 'John Smith'),
       (SELECT id FROM TODOUSER.projects WHERE name = 'Web Portal Redesign'),
       5, 'High', 0, 1);

-- Todo 3: Create user profile page
INSERT INTO TODOUSER.todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, priority, deleted, done)
VALUES ('Create user profile page', 'Design and implement the user profile page',
       SYSTIMESTAMP - INTERVAL '50' DAY(3), SYSTIMESTAMP - INTERVAL '45' DAY(3),
       (SELECT id FROM TODOUSER.states WHERE name = 'COMPLETED'),
       (SELECT id FROM TODOUSER.sprints WHERE name = 'Sprint 1'),
       (SELECT id FROM TODOUSER.users WHERE name = 'Jessica Miller'),
       (SELECT id FROM TODOUSER.projects WHERE name = 'Web Portal Redesign'),
       5, 'Medium', 0, 1);

-- Todo 4: API Integration
INSERT INTO TODOUSER.todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, priority, deleted, done)
VALUES ('API Integration', 'Integrate external APIs for data exchange',
       SYSTIMESTAMP - INTERVAL '30' DAY(3), SYSTIMESTAMP - INTERVAL '20' DAY(3),
       (SELECT id FROM TODOUSER.states WHERE name = 'IN_PROGRESS'),
       (SELECT id FROM TODOUSER.sprints WHERE name = 'Sprint 1'),
       (SELECT id FROM TODOUSER.users WHERE name = 'John Smith'),
       (SELECT id FROM TODOUSER.projects WHERE name = 'Web Portal Redesign'),
       4, 'High', 0, 0);

-- Todo 5: Documentation Update
INSERT INTO TODOUSER.todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, priority, deleted, done)
VALUES ('Documentation Review', 'Update project documentation with latest changes',
       SYSTIMESTAMP - INTERVAL '10' DAY(3), SYSTIMESTAMP, 
       (SELECT id FROM TODOUSER.states WHERE name = 'TODO'),
       (SELECT id FROM TODOUSER.sprints WHERE name = 'Sprint 1'),
       (SELECT id FROM TODOUSER.users WHERE name = 'Sarah Johnson'),
       (SELECT id FROM TODOUSER.projects WHERE name = 'Web Portal Redesign'),
       2, 'Low', 0, 0);

-- Todo 6: Bug Fixing
INSERT INTO TODOUSER.todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, priority, deleted, done)
VALUES ('Bug Fixing', 'Fix reported bugs in the login system',
       SYSTIMESTAMP - INTERVAL '15' DAY(3), SYSTIMESTAMP - INTERVAL '5' DAY(3),
       (SELECT id FROM TODOUSER.states WHERE name = 'IN_PROGRESS'),
       (SELECT id FROM TODOUSER.sprints WHERE name = 'Sprint 1'),
       (SELECT id FROM TODOUSER.users WHERE name = 'John Smith'),
       (SELECT id FROM TODOUSER.projects WHERE name = 'Web Portal Redesign'),
       3, 'High', 0, 0);
