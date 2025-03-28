-- Drop tables with CASCADE CONSTRAINTS
DROP TABLE TODOUSER.todo CASCADE CONSTRAINTS;
DROP TABLE TODOUSER.sprints CASCADE CONSTRAINTS;
DROP TABLE TODOUSER.projects CASCADE CONSTRAINTS;
DROP TABLE TODOUSER.users CASCADE CONSTRAINTS;
DROP TABLE TODOUSER.teams CASCADE CONSTRAINTS;
DROP TABLE TODOUSER.states CASCADE CONSTRAINTS;

-- Create states table
CREATE TABLE TODOUSER.states (
    id NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY PRIMARY KEY,
    name VARCHAR2(255),
    description VARCHAR2(1000),
    workflow_priority NUMBER DEFAULT 0,
    creation_date TIMESTAMP DEFAULT SYSTIMESTAMP,
    deleted NUMBER(1) DEFAULT 0,
    CONSTRAINT uk_state_name UNIQUE (name),
    CONSTRAINT uk_workflow_priority UNIQUE (workflow_priority)
);

-- Create teams table
CREATE TABLE TODOUSER.teams (
    id NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY PRIMARY KEY,
    name VARCHAR2(255),
    description VARCHAR2(1000),
    creation_date TIMESTAMP DEFAULT SYSTIMESTAMP,
    deleted NUMBER(1) DEFAULT 0,
    CONSTRAINT uk_team_name UNIQUE (name)
);

-- Create users table
CREATE TABLE TODOUSER.users (
    id NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY PRIMARY KEY,
    phone_number VARCHAR2(100),
    password VARCHAR2(255),
    avatar_url VARCHAR2(255),  -- Fixed missing comma
    name VARCHAR2(255),
    role VARCHAR2(255),
    team_id NUMBER,
    deleted NUMBER(1) DEFAULT 0,
    CONSTRAINT uk_username UNIQUE (phone_number),
    CONSTRAINT fk_users_team FOREIGN KEY (team_id) REFERENCES TODOUSER.teams(id)
);

-- Create projects table
CREATE TABLE TODOUSER.projects (
    id NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY PRIMARY KEY,
    name VARCHAR2(255),
    description VARCHAR2(1000),
    creation_date TIMESTAMP DEFAULT SYSTIMESTAMP,
    due_date TIMESTAMP,
    state_id NUMBER,
    deleted NUMBER(1) DEFAULT 0,
    CONSTRAINT fk_projects_state FOREIGN KEY (state_id) REFERENCES TODOUSER.states(id)
);

-- Create sprints table
CREATE TABLE TODOUSER.sprints (
    id NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY PRIMARY KEY,
    name VARCHAR2(255),
    creation_date TIMESTAMP DEFAULT SYSTIMESTAMP,
    due_date TIMESTAMP,
    state_id NUMBER,
    project_id NUMBER,
    deleted NUMBER(1) DEFAULT 0,
    CONSTRAINT fk_sprints_project FOREIGN KEY (project_id) REFERENCES TODOUSER.projects(id),
    CONSTRAINT fk_sprints_state FOREIGN KEY (state_id) REFERENCES TODOUSER.states(id)
);

-- Create todo table
CREATE TABLE TODOUSER.todo (
    id NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY PRIMARY KEY,
    title VARCHAR2(255),
    description VARCHAR2(1000),
    creation_date TIMESTAMP DEFAULT SYSTIMESTAMP,
    estimated_hours NUMBER DEFAULT 0,
    real_hours NUMBER DEFAULT 0,
    story_points NUMBER DEFAULT 0,
    due_date TIMESTAMP,
    state_id NUMBER,
    sprint_id NUMBER,
    user_id NUMBER,
    project_id NUMBER,
    priority VARCHAR2(10) DEFAULT 'Low',
    deleted NUMBER(1) DEFAULT 0,
    done NUMBER(1) DEFAULT 0,
    CONSTRAINT uk_todo_title UNIQUE (title),
    CONSTRAINT fk_todo_project FOREIGN KEY (project_id) REFERENCES TODOUSER.projects(id),
    CONSTRAINT fk_todo_sprint FOREIGN KEY (sprint_id) REFERENCES TODOUSER.sprints(id),
    CONSTRAINT fk_todo_user FOREIGN KEY (user_id) REFERENCES TODOUSER.users(id),
    CONSTRAINT fk_todo_state FOREIGN KEY (state_id) REFERENCES TODOUSER.states(id)
);
