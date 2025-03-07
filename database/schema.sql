DROP TABLE todo CASCADE CONSTRAINTS;
DROP TABLE sprints CASCADE CONSTRAINTS;
DROP TABLE projects CASCADE CONSTRAINTS;
DROP TABLE users CASCADE CONSTRAINTS;
DROP TABLE teams CASCADE CONSTRAINTS;
DROP TABLE states CASCADE CONSTRAINTS;


CREATE TABLE states (
    id NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY PRIMARY KEY,
    name VARCHAR2(255),
    description VARCHAR2(1000),
    workflow_priority NUMBER DEFAULT 0,
    creation_date TIMESTAMP DEFAULT SYSTIMESTAMP,
    deleted NUMBER(1) DEFAULT 0,
    CONSTRAINT uk_state_name UNIQUE (name),
    CONSTRAINT uk_workflow_priority UNIQUE (workflow_priority)
);

CREATE TABLE teams (
    id NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY PRIMARY KEY,
    name VARCHAR2(255),
    description VARCHAR2(1000),
    creation_date TIMESTAMP DEFAULT SYSTIMESTAMP,
    deleted NUMBER(1) DEFAULT 0,
    CONSTRAINT uk_team_name UNIQUE (name)
);

CREATE TABLE users (
    id NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY PRIMARY KEY,
    phone_number VARCHAR2(100),
    password VARCHAR2(255),
    name VARCHAR2(255),
    role VARCHAR2(255),
    team_id NUMBER,
    deleted NUMBER(1) DEFAULT 0,
    CONSTRAINT uk_username UNIQUE (phone_number),
    CONSTRAINT fk_users_team FOREIGN KEY (team_id) REFERENCES teams(id)
);

CREATE TABLE projects (
    id NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY PRIMARY KEY,
    name VARCHAR2(255),
    description VARCHAR2(1000),
    creation_date TIMESTAMP DEFAULT SYSTIMESTAMP,
    due_date TIMESTAMP,
    state_id NUMBER,
    deleted NUMBER(1) DEFAULT 0,
    CONSTRAINT fk_projects_state FOREIGN KEY (state_id) REFERENCES states(id)
);

CREATE TABLE sprints (
    id NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY PRIMARY KEY,
    name VARCHAR2(255),
    creation_date TIMESTAMP DEFAULT SYSTIMESTAMP,
    due_date TIMESTAMP,
    state_id NUMBER,
    project_id NUMBER,
    deleted NUMBER(1) DEFAULT 0,
    CONSTRAINT fk_sprints_project FOREIGN KEY (project_id) REFERENCES projects(id),
    CONSTRAINT fk_sprints_state FOREIGN KEY (state_id) REFERENCES states(id)
);

CREATE TABLE todo (
    id NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY PRIMARY KEY,
    title VARCHAR2(255),
    description VARCHAR2(1000),
    creation_date TIMESTAMP DEFAULT SYSTIMESTAMP,
    due_date TIMESTAMP,
    state_id NUMBER,
    sprint_id NUMBER,
    user_id NUMBER,
    project_id NUMBER,
    story_points NUMBER DEFAULT 0,
    priority VARCHAR2(10) DEFAULT 'Low',
    deleted NUMBER(1) DEFAULT 0,
    done NUMBER(1) DEFAULT 0,
    CONSTRAINT uk_todo_title UNIQUE (title),
    CONSTRAINT fk_todo_project FOREIGN KEY (project_id) REFERENCES projects(id),
    CONSTRAINT fk_todo_sprint FOREIGN KEY (sprint_id) REFERENCES sprints(id),
    CONSTRAINT fk_todo_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_todo_state FOREIGN KEY (state_id) REFERENCES states(id)
);