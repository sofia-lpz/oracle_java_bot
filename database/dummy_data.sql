-- Insert data into states table
INSERT INTO states (name, description, workflow_priority, deleted) VALUES ('TODO', 'Tasks that are not yet ready for development', 1, 0);
INSERT INTO states (name, description, workflow_priority, deleted) VALUES ('IN_PROGRESS', 'Tasks currently being worked on', 2, 0);
INSERT INTO states (name, description, workflow_priority, deleted) VALUES ('COMPLETED', 'Tasks that have been finished', 3, 0);

-- Insert data into roles table
INSERT INTO roles (name, deleted) VALUES ('Developer', 0);
INSERT INTO roles (name, deleted) VALUES ('Project Manager', 0);
INSERT INTO roles (name, deleted) VALUES ('Designer', 0);

-- Insert data into teams table FIRST
INSERT INTO teams (name, description, creation_date, deleted) 
VALUES ('Alpha Team', 'Frontend development team', SYSTIMESTAMP - INTERVAL '180' DAY, 0);

-- Insert data into users table AFTER teams exist
-- Using subqueries to get the actual team_id and role_id
-- Insert data into users table AFTER teams exist
-- Modified to match the User entity structure
INSERT INTO users (id, number, password, phone_number, name, description, workflow_priority, due_date, state, role_id, team_id, deleted) 
VALUES (1, '001', 'password123', 5551234567, 'John Smith', 'Senior Developer', 1, 
       SYSTIMESTAMP + INTERVAL '30' DAY, 'IN_PROGRESS',
       (SELECT id FROM roles WHERE name = 'Developer'), 
       (SELECT id FROM teams WHERE name = 'Alpha Team'), 0);

INSERT INTO users (id, number, password, phone_number, name, description, workflow_priority, due_date, state, role_id, team_id, deleted) 
VALUES (2, '002', 'password123', 5552345678, 'Sarah Johnson', 'Team lead', 2, 
       SYSTIMESTAMP + INTERVAL '30' DAY, 'IN_PROGRESS',
       (SELECT id FROM roles WHERE name = 'Project Manager'), 
       (SELECT id FROM teams WHERE name = 'Alpha Team'), 0);

INSERT INTO users (id, number, password, phone_number, name, description, workflow_priority, due_date, state, role_id, team_id, deleted) 
VALUES (3, '003', 'password123', 5556789012, 'Jessica Miller', 'UI/UX specialist', 1, 
       SYSTIMESTAMP + INTERVAL '30' DAY, 'IN_PROGRESS',
       (SELECT id FROM roles WHERE name = 'Designer'), 
       (SELECT id FROM teams WHERE name = 'Alpha Team'), 0);

-- Insert data into projects table AFTER states exist
INSERT INTO projects (name, description, creation_date, due_date, state_id, deleted) 
VALUES ('Web Portal Redesign', 'Complete redesign of the customer web portal', 
       SYSTIMESTAMP - INTERVAL '90' DAY, SYSTIMESTAMP + INTERVAL '90' DAY, 
       (SELECT id FROM states WHERE name = 'IN_PROGRESS'), 0);

-- Create sprint for the project AFTER projects exist
INSERT INTO sprints (name, creation_date, due_date, state_id, project_id, deleted)
VALUES ('Sprint 1', SYSTIMESTAMP - INTERVAL '85' DAY, SYSTIMESTAMP - INTERVAL '55' DAY, 
       (SELECT id FROM states WHERE name = 'COMPLETED'), 
       (SELECT id FROM projects WHERE name = 'Web Portal Redesign'), 0);

-- Todo 1: Design login page
INSERT INTO todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, priority, deleted, done) 
VALUES ('Design login page', 'Create mockups for the new login page design', 
       SYSTIMESTAMP - INTERVAL '80' DAY, SYSTIMESTAMP - INTERVAL '75' DAY, 
       (SELECT id FROM states WHERE name = 'COMPLETED'), 
       (SELECT id FROM sprints WHERE name = 'Sprint 1'), 
       (SELECT id FROM users WHERE name = 'Jessica Miller'), 
       (SELECT id FROM projects WHERE name = 'Web Portal Redesign'), 
       3, 'Medium', 0, 1);

-- Todo 2: Implement login functionality
INSERT INTO todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, priority, deleted, done) 
VALUES ('Implement login functionality', 'Develop the login page and authentication system', 
       SYSTIMESTAMP - INTERVAL '75' DAY, SYSTIMESTAMP - INTERVAL '70' DAY, 
       (SELECT id FROM states WHERE name = 'COMPLETED'), 
       (SELECT id FROM sprints WHERE name = 'Sprint 1'), 
       (SELECT id FROM users WHERE name = 'John Smith'), 
       (SELECT id FROM projects WHERE name = 'Web Portal Redesign'), 
       5, 'High', 0, 1);

-- Todo 3: Create user profile page
INSERT INTO todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, priority, deleted, done) 
VALUES ('Create user profile page', 'Design and implement the user profile page', 
       SYSTIMESTAMP - INTERVAL '50' DAY, SYSTIMESTAMP - INTERVAL '45' DAY, 
       (SELECT id FROM states WHERE name = 'COMPLETED'), 
       (SELECT id FROM sprints WHERE name = 'Sprint 1'), 
       (SELECT id FROM users WHERE name = 'Jessica Miller'), 
       (SELECT id FROM projects WHERE name = 'Web Portal Redesign'), 
       5, 'Medium', 0, 1);

-- Create another sprint
INSERT INTO sprints (name, creation_date, due_date, state_id, project_id, deleted)
VALUES ('Sprint 2', SYSTIMESTAMP - INTERVAL '40' DAY, SYSTIMESTAMP - INTERVAL '10' DAY, 
       (SELECT id FROM states WHERE name = 'IN_PROGRESS'), 
       (SELECT id FROM projects WHERE name = 'Web Portal Redesign'), 0);

-- Todo 4: Add notification system
INSERT INTO todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, priority, deleted, done) 
VALUES ('Add notification system', 'Implement real-time notification functionality', 
       SYSTIMESTAMP - INTERVAL '35' DAY, SYSTIMESTAMP - INTERVAL '25' DAY, 
       (SELECT id FROM states WHERE name = 'IN_PROGRESS'), 
       (SELECT id FROM sprints WHERE name = 'Sprint 2'), 
       (SELECT id FROM users WHERE name = 'John Smith'), 
       (SELECT id FROM projects WHERE name = 'Web Portal Redesign'), 
       8, 'High', 0, 0);

-- Todo 5: Optimize page loading speed
INSERT INTO todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, priority, deleted, done) 
VALUES ('Optimize page loading speed', 'Improve performance of all web portal pages', 
       SYSTIMESTAMP - INTERVAL '20' DAY, SYSTIMESTAMP - INTERVAL '10' DAY, 
       (SELECT id FROM states WHERE name = 'IN_PROGRESS'), 
       (SELECT id FROM sprints WHERE name = 'Sprint 2'), 
       (SELECT id FROM users WHERE name = 'John Smith'), 
       (SELECT id FROM projects WHERE name = 'Web Portal Redesign'), 
       3, 'Low', 0, 0);

-- Create future sprint
INSERT INTO sprints (name, creation_date, due_date, state_id, project_id, deleted)
VALUES ('Sprint 3', SYSTIMESTAMP - INTERVAL '5' DAY, SYSTIMESTAMP + INTERVAL '25' DAY, 
       (SELECT id FROM states WHERE name = 'TODO'), 
       (SELECT id FROM projects WHERE name = 'Web Portal Redesign'), 0);

-- Todo 6: Implement multi-factor authentication
INSERT INTO todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, priority, deleted, done) 
VALUES ('Implement multi-factor authentication', 'Add support for 2FA', 
       SYSTIMESTAMP - INTERVAL '2' DAY, SYSTIMESTAMP + INTERVAL '10' DAY, 
       (SELECT id FROM states WHERE name = 'TODO'), 
       (SELECT id FROM sprints WHERE name = 'Sprint 3'), 
       (SELECT id FROM users WHERE name = 'John Smith'), 
       (SELECT id FROM projects WHERE name = 'Web Portal Redesign'), 
       5, 'Medium', 0, 0);

-- Commit the transaction
COMMIT;