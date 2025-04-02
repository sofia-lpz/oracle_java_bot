-- Delete data from tables without dropping them
DELETE FROM TODOUSER.todo;
DELETE FROM TODOUSER.sprints;
DELETE FROM TODOUSER.projects;
DELETE FROM TODOUSER.users;
DELETE FROM TODOUSER.teams;
DELETE FROM TODOUSER.states;

-- Insert data into teams table
INSERT INTO TODOUSER.teams (name, description, creation_date, deleted)
VALUES ('JOES', 'development team', SYSTIMESTAMP - INTERVAL '180' DAY, 0);

INSERT INTO TODOUSER.users (phone_number, password, avatar_url, name, role, team_id, deleted) 
VALUES ('1234567890', 'password123', 'avatar1.jpg', 'andres-villaa', 'Frontend', 
        (SELECT id FROM TODOUSER.teams WHERE name = 'JOES'), 0);

INSERT INTO TODOUSER.users (phone_number, password, avatar_url, name, role, team_id, deleted) 
VALUES ('2345678901', 'password123', 'avatar2.jpg', 'sofia-lpz', 'Backend',
        (SELECT id FROM TODOUSER.teams WHERE name = 'JOES'), 0);

INSERT INTO TODOUSER.users (phone_number, password, avatar_url, name, role, team_id, deleted) 
VALUES ('3456789012', 'password123', 'avatar3.jpg', 'rintintingoesbrrr', 'Integration',
        (SELECT id FROM TODOUSER.teams WHERE name = 'JOES'), 0);

INSERT INTO TODOUSER.users (phone_number, password, avatar_url, name, role, team_id, deleted) 
VALUES ('4567890123', 'password123', 'avatar4.jpg', 'JimenaCarmona2', 'Scrum-Master',
        (SELECT id FROM TODOUSER.teams WHERE name = 'JOES'), 0);


INSERT INTO TODOUSER.states (name, description, workflow_priority, creation_date, deleted)
VALUES ('TODO', 'Tasks that are not yet ready for development', 1, SYSTIMESTAMP, 0);

INSERT INTO TODOUSER.states (name, description, workflow_priority, creation_date, deleted)
VALUES ('IN_PROGRESS', 'Tasks currently being worked on', 2, SYSTIMESTAMP, 0);

INSERT INTO TODOUSER.states (name, description, workflow_priority, creation_date, deleted)
VALUES ('COMPLETED', 'Tasks that have been finished', 3, SYSTIMESTAMP, 0);

-- Insert data into projects table
INSERT INTO TODOUSER.projects (name, description, creation_date, due_date, state_id, deleted)
VALUES ('JOES ORACLE BOT', 'Complete redesign of the customer web portal',
       SYSTIMESTAMP - INTERVAL '90' DAY, SYSTIMESTAMP + INTERVAL '90' DAY,
       (SELECT id FROM TODOUSER.states WHERE name = 'IN_PROGRESS'), 0);

-- Insert sprint
INSERT INTO TODOUSER.sprints (name, creation_date, due_date, state_id, project_id, deleted)
VALUES ('Sprint 1', SYSTIMESTAMP - INTERVAL '85' DAY, SYSTIMESTAMP - INTERVAL '55' DAY,
       (SELECT id FROM TODOUSER.states WHERE name = 'COMPLETED'),
       (SELECT id FROM TODOUSER.projects WHERE name = 'JOES'), 0);

-- Insert sprint
-- Insert Sprint 1

INSERT INTO TODOUSER.sprints (name, creation_date, due_date, state_id, project_id, deleted)
VALUES ('Sprint 1', SYSTIMESTAMP - INTERVAL '85' DAY, SYSTIMESTAMP - INTERVAL '55' DAY,
       (SELECT id FROM TODOUSER.states WHERE name = 'COMPLETED'),
       (SELECT id FROM TODOUSER.projects WHERE name = 'JOES ORACLE BOT'), 0);

-- Insert Sprint 2
INSERT INTO TODOUSER.sprints (name, creation_date, due_date, state_id, project_id, deleted)
VALUES ('Sprint 2', SYSTIMESTAMP - INTERVAL '54' DAY, SYSTIMESTAMP - INTERVAL '30' DAY,
       (SELECT id FROM TODOUSER.states WHERE name = 'COMPLETED'),
       (SELECT id FROM TODOUSER.projects WHERE name = 'JOES ORACLE BOT'), 0);

-- Insert Sprint 3
INSERT INTO TODOUSER.sprints (name, creation_date, due_date, state_id, project_id, deleted)
VALUES ('Sprint 3', SYSTIMESTAMP - INTERVAL '29' DAY, SYSTIMESTAMP - INTERVAL '15' DAY,
       (SELECT id FROM TODOUSER.states WHERE name = 'COMPLETED'),
       (SELECT id FROM TODOUSER.projects WHERE name = 'JOES ORACLE BOT'), 0);

-- Insert Sprint 4
INSERT INTO TODOUSER.sprints (name, creation_date, due_date, state_id, project_id, deleted)
VALUES ('Sprint 4', SYSTIMESTAMP - INTERVAL '14' DAY, SYSTIMESTAMP + INTERVAL '10' DAY,
       (SELECT id FROM TODOUSER.states WHERE name = 'COMPLETED'),
       (SELECT id FROM TODOUSER.projects WHERE name = 'JOES ORACLE BOT'), 0);

-- Insert Sprint 5
INSERT INTO TODOUSER.sprints (name, creation_date, due_date, state_id, project_id, deleted)
VALUES ('Sprint 5', SYSTIMESTAMP + INTERVAL '11' DAY, SYSTIMESTAMP + INTERVAL '40' DAY,
       (SELECT id FROM TODOUSER.states WHERE name = 'COMPLETED'),
       (SELECT id FROM TODOUSER.projects WHERE name = 'JOES ORACLE BOT'), 0);


-- Sprint 1 tasks (from Image 2)
INSERT INTO TODOUSER.todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, estimated_hours, real_hours, priority, deleted, done)
VALUES ('Diseño de base de datos', 'Diseño de estructura de base de datos del proyecto',
        TO_TIMESTAMP('2025-03-13', 'YYYY-MM-DD'), TO_TIMESTAMP('2024-02-16', 'YYYY-MM-DD'),
        (SELECT id FROM TODOUSER.states WHERE name = 'COMPLETED'),
        (SELECT id FROM TODOUSER.sprints WHERE name = 'Sprint 1'),
        (SELECT id FROM TODOUSER.users WHERE name = 'andres-villaa'),
        (SELECT id FROM TODOUSER.projects WHERE name = 'JOES ORACLE BOT'),
        1, 2, 2, 'High', 0, 1);

INSERT INTO TODOUSER.todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, estimated_hours, real_hours, priority, deleted, done)
VALUES ('Especificación', 'Documentación de especificaciones técnicas',
        TO_TIMESTAMP('2025-03-13', 'YYYY-MM-DD'), TO_TIMESTAMP('2024-02-16', 'YYYY-MM-DD'),
        (SELECT id FROM TODOUSER.states WHERE name = 'COMPLETED'),
        (SELECT id FROM TODOUSER.sprints WHERE name = 'Sprint 1'),
        (SELECT id FROM TODOUSER.users WHERE name = 'andres-villaa'),
        (SELECT id FROM TODOUSER.projects WHERE name = 'JOES ORACLE BOT'),
        1, 7, 6, 'High', 0, 1);

INSERT INTO TODOUSER.todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, estimated_hours, real_hours, priority, deleted, done)
VALUES ('Elicitacion', 'Proceso de recolección de requisitos del sistema',
        TO_TIMESTAMP('2025-03-13', 'YYYY-MM-DD'), TO_TIMESTAMP('2024-02-16', 'YYYY-MM-DD'),
        (SELECT id FROM TODOUSER.states WHERE name = 'COMPLETED'),
        (SELECT id FROM TODOUSER.sprints WHERE name = 'Sprint 1'),
        (SELECT id FROM TODOUSER.users WHERE name = 'andres-villaa'),
        (SELECT id FROM TODOUSER.projects WHERE name = 'JOES ORACLE BOT'),
        1, 6, 4, 'Low', 0, 1);

-- Sprint 2 tasks (from Image 2)
INSERT INTO TODOUSER.todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, estimated_hours, real_hours, priority, deleted, done)
VALUES ('Desarrollo de base de datos', 'Implementación de la base de datos según diseño',
        TO_TIMESTAMP('2025-03-13', 'YYYY-MM-DD'), TO_TIMESTAMP('2024-02-23', 'YYYY-MM-DD'),
        (SELECT id FROM TODOUSER.states WHERE name = 'COMPLETED'),
        (SELECT id FROM TODOUSER.sprints WHERE name = 'Sprint 2'),
        (SELECT id FROM TODOUSER.users WHERE name = 'JimenaCarmona2'),
        (SELECT id FROM TODOUSER.projects WHERE name = 'JOES ORACLE BOT'),
        1, 3, 3, 'High', 0, 1);

INSERT INTO TODOUSER.todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, estimated_hours, real_hours, priority, deleted, done)
VALUES ('Análisis', 'Análisis de requerimientos y funcionalidades',
        TO_TIMESTAMP('2025-03-13', 'YYYY-MM-DD'), TO_TIMESTAMP('2024-02-23', 'YYYY-MM-DD'),
        (SELECT id FROM TODOUSER.states WHERE name = 'COMPLETED'),
        (SELECT id FROM TODOUSER.sprints WHERE name = 'Sprint 2'),
        (SELECT id FROM TODOUSER.users WHERE name = 'andres-villaa'),
        (SELECT id FROM TODOUSER.projects WHERE name = 'JOES ORACLE BOT'),
        1, 2, 4, 'High', 0, 1);

INSERT INTO TODOUSER.todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, estimated_hours, real_hours, priority, deleted, done)
VALUES ('Diseño de backend', 'Definición de arquitectura de backend y APIs',
        TO_TIMESTAMP('2025-03-13', 'YYYY-MM-DD'), TO_TIMESTAMP('2024-02-23', 'YYYY-MM-DD'),
        (SELECT id FROM TODOUSER.states WHERE name = 'COMPLETED'),
        (SELECT id FROM TODOUSER.sprints WHERE name = 'Sprint 2'),
        (SELECT id FROM TODOUSER.users WHERE name = 'rintintingoesbrrr'),
        (SELECT id FROM TODOUSER.projects WHERE name = 'JOES ORACLE BOT'),
        1, 4, 3, 'Low', 0, 1);

INSERT INTO TODOUSER.todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, estimated_hours, real_hours, priority, deleted, done)
VALUES ('Diseño de front-end', 'Diseño de interfaces de usuario y experiencia',
        TO_TIMESTAMP('2025-03-13', 'YYYY-MM-DD'), TO_TIMESTAMP('2024-02-23', 'YYYY-MM-DD'),
        (SELECT id FROM TODOUSER.states WHERE name = 'COMPLETED'),
        (SELECT id FROM TODOUSER.sprints WHERE name = 'Sprint 2'),
        (SELECT id FROM TODOUSER.users WHERE name = 'andres-villaa'),
        (SELECT id FROM TODOUSER.projects WHERE name = 'JOES ORACLE BOT'),
        1, 5, 5, 'Low', 0, 1);

INSERT INTO TODOUSER.todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, estimated_hours, real_hours, priority, deleted, done)
VALUES ('Diseño del plan de trabajo', 'Planificación de actividades y cronograma',
        TO_TIMESTAMP('2025-03-13', 'YYYY-MM-DD'), TO_TIMESTAMP('2024-02-23', 'YYYY-MM-DD'),
        (SELECT id FROM TODOUSER.states WHERE name = 'COMPLETED'),
        (SELECT id FROM TODOUSER.sprints WHERE name = 'Sprint 2'),
        (SELECT id FROM TODOUSER.users WHERE name = 'andres-villaa'),
        (SELECT id FROM TODOUSER.projects WHERE name = 'JOES ORACLE BOT'),
        1, 3, 3, 'Low', 0, 1);

-- Sprint 3 tasks (from Image 1)
INSERT INTO TODOUSER.todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, estimated_hours, real_hours, priority, deleted, done)
VALUES ('Desarrollo de back-end', 'Implementación de servicios de backend',
        TO_TIMESTAMP('2025-03-13', 'YYYY-MM-DD'), TO_TIMESTAMP('2024-03-02', 'YYYY-MM-DD'),
        (SELECT id FROM TODOUSER.states WHERE name = 'IN_PROGRESS'),
        (SELECT id FROM TODOUSER.sprints WHERE name = 'Sprint 3'),
        (SELECT id FROM TODOUSER.users WHERE name = 'rintintingoesbrrr'),
        (SELECT id FROM TODOUSER.projects WHERE name = 'JOES ORACLE BOT'),
        1, 10, 7, 'Low', 0, 0);

INSERT INTO TODOUSER.todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, estimated_hours, real_hours, priority, deleted, done)
VALUES ('Diseño de plan de pruebas', 'Estrategia y casos de prueba del sistema',
        TO_TIMESTAMP('2025-03-13', 'YYYY-MM-DD'), TO_TIMESTAMP('2024-03-02', 'YYYY-MM-DD'),
        (SELECT id FROM TODOUSER.states WHERE name = 'COMPLETED'),
        (SELECT id FROM TODOUSER.sprints WHERE name = 'Sprint 3'),
        (SELECT id FROM TODOUSER.users WHERE name = 'andres-villaa'),
        (SELECT id FROM TODOUSER.projects WHERE name = 'JOES ORACLE BOT'),
        1, 4, 2, 'Medium', 0, 1);

INSERT INTO TODOUSER.todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, estimated_hours, real_hours, priority, deleted, done)
VALUES ('Desarrollo de front-end', 'Implementación de interfaces de usuario',
        TO_TIMESTAMP('2025-03-13', 'YYYY-MM-DD'), TO_TIMESTAMP('2024-03-02', 'YYYY-MM-DD'),
        (SELECT id FROM TODOUSER.states WHERE name = 'IN_PROGRESS'),
        (SELECT id FROM TODOUSER.sprints WHERE name = 'Sprint 3'),
        (SELECT id FROM TODOUSER.users WHERE name = 'andres-villaa'),
        (SELECT id FROM TODOUSER.projects WHERE name = 'JOES ORACLE BOT'),
        1, 10, 5, 'Low', 0, 0);

-- Sprint 4 tasks (from Image 1)
INSERT INTO TODOUSER.todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, estimated_hours, real_hours, priority, deleted, done)
VALUES ('Despliegue', 'Despliegue del sistema en ambiente de producción',
        TO_TIMESTAMP('2025-03-13', 'YYYY-MM-DD'), TO_TIMESTAMP('2024-03-09', 'YYYY-MM-DD'),
        (SELECT id FROM TODOUSER.states WHERE name = 'COMPLETED'),
        (SELECT id FROM TODOUSER.sprints WHERE name = 'Sprint 4'),
        (SELECT id FROM TODOUSER.users WHERE name = 'andres-villaa'),
        (SELECT id FROM TODOUSER.projects WHERE name = 'JOES ORACLE BOT'),
        1, 7, 9, 'Medium', 0, 1);

-- No Sprint tasks (from Image 1)
INSERT INTO TODOUSER.todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, estimated_hours, real_hours, priority, deleted, done)
VALUES ('Pruebas unitarias', 'Desarrollo de pruebas unitarias para componentes',
        TO_TIMESTAMP('2025-03-13', 'YYYY-MM-DD'), TO_TIMESTAMP('2025-03-27', 'YYYY-MM-DD'),
        (SELECT id FROM TODOUSER.states WHERE name = 'TODO'),
        NULL,
        (SELECT id FROM TODOUSER.users WHERE name = 'sofia-lpz'),
        (SELECT id FROM TODOUSER.projects WHERE name = 'JOES ORACLE BOT'),
        1, 5, 0, 'Low', 0, 0);

INSERT INTO TODOUSER.todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, estimated_hours, real_hours, priority, deleted, done)
VALUES ('Pruebas de integracion', 'Verificación de integración entre componentes',
        TO_TIMESTAMP('2025-03-13', 'YYYY-MM-DD'), TO_TIMESTAMP('2025-03-27', 'YYYY-MM-DD'),
        (SELECT id FROM TODOUSER.states WHERE name = 'TODO'),
        NULL,
        (SELECT id FROM TODOUSER.users WHERE name = 'rintintingoesbrrr'),
        (SELECT id FROM TODOUSER.projects WHERE name = 'JOES ORACLE BOT'),
        1, 7, 0, 'Medium', 0, 0);

INSERT INTO TODOUSER.todo (title, description, creation_date, due_date, state_id, sprint_id, user_id, project_id, story_points, estimated_hours, real_hours, priority, deleted, done)
VALUES ('Pruebas de accesibilidad', 'Verificación de accesibilidad del sistema',
        TO_TIMESTAMP('2025-03-13', 'YYYY-MM-DD'), TO_TIMESTAMP('2025-03-27', 'YYYY-MM-DD'),
        (SELECT id FROM TODOUSER.states WHERE name = 'TODO'),
        NULL,
        (SELECT id FROM TODOUSER.users WHERE name = 'andres-villaa'),
        (SELECT id FROM TODOUSER.projects WHERE name = 'JOES ORACLE BOT'),
        1, 5, 0, 'Low', 0, 0);
