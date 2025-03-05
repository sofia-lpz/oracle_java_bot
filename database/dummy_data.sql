-- Inserting into states table (these are correct)
INSERT INTO states (name) VALUES ('Backlog');
INSERT INTO states (name) VALUES ('In Progress');
INSERT INTO states (name) VALUES ('In Review');
INSERT INTO states (name) VALUES ('Done');
INSERT INTO states (name) VALUES ('Blocked');

-- Inserting into roles table (these are correct)
INSERT INTO roles (name) VALUES ('Administrator');
INSERT INTO roles (name) VALUES ('Project Manager');
INSERT INTO roles (name) VALUES ('Developer');

-- Inserting into teams table (these are correct)
INSERT INTO teams (name) VALUES ('Backend Team');
INSERT INTO teams (name) VALUES ('Frontend Team');
INSERT INTO teams (name) VALUES ('QA Team');
INSERT INTO teams (name) VALUES ('DevOps Team');
INSERT INTO teams (name) VALUES ('Mobile Team');

-- Corrected users table inserts (changed username to phone_number)
INSERT INTO users (phone_number, name, role_id, team_id) 
SELECT '+52-555-001', 'Sofia Moreno', r.id, t.id
FROM roles r, teams t 
WHERE r.name = 'Project Manager' AND t.name = 'Backend Team';

INSERT INTO users (phone_number, name, role_id, team_id) 
SELECT '+52-555-002', 'Omar Sanchez', r.id, t.id
FROM roles r, teams t 
WHERE r.name = 'Team Lead' AND t.name = 'DevOps Team';

INSERT INTO users (phone_number, name, role_id, team_id) 
SELECT '+52-555-003', 'Jimena Carmona', r.id, t.id
FROM roles r, teams t 
WHERE r.name = 'Developer' AND t.name = 'Frontend Team';

INSERT INTO users (phone_number, name, role_id, team_id) 
SELECT '+52-555-004', 'Edgar Villa', r.id, t.id
FROM roles r, teams t 
WHERE r.name = 'QA Engineer' AND t.name = 'QA Team';

-- Projects table inserts (these are correct)
INSERT INTO projects (name, due_date, state_id) 
SELECT 'Sistema de Inventario', TIMESTAMP '2025-12-31 23:59:59', s.id
FROM states s WHERE s.name = 'In Progress';

INSERT INTO projects (name, due_date, state_id) 
SELECT 'Portal de Clientes', TIMESTAMP '2025-06-30 23:59:59', s.id
FROM states s WHERE s.name = 'New';

INSERT INTO projects (name, due_date, state_id) 
SELECT 'API de Pagos', TIMESTAMP '2025-09-30 23:59:59', s.id
FROM states s WHERE s.name = 'In Progress';

INSERT INTO projects (name, due_date, state_id) 
SELECT 'Migracion Cloud', TIMESTAMP '2025-04-30 23:59:59', s.id
FROM states s WHERE s.name = 'Review';

-- Sprints table inserts (these are correct)
INSERT INTO sprints (name, due_date, state_id, project_id) 
SELECT 'Sprint 1', TIMESTAMP '2025-03-31 23:59:59', s.id, p.id
FROM states s, projects p 
WHERE s.name = 'In Progress' AND p.name = 'Sistema de Inventario';

INSERT INTO sprints (name, due_date, state_id, project_id) 
SELECT 'Sprint 2', TIMESTAMP '2025-04-30 23:59:59', s.id, p.id
FROM states s, projects p 
WHERE s.name = 'New' AND p.name = 'Sistema de Inventario';

INSERT INTO sprints (name, due_date, state_id, project_id) 
SELECT 'Sprint 3', TIMESTAMP '2025-03-15 23:59:59', s.id, p.id
FROM states s, projects p 
WHERE s.name = 'In Progress' AND p.name = 'Portal de Clientes';

INSERT INTO sprints (name, due_date, state_id, project_id) 
SELECT 'Sprint 4', TIMESTAMP '2025-04-15 23:59:59', s.id, p.id
FROM states s, projects p 
WHERE s.name = 'New' AND p.name = 'API de Pagos';

INSERT INTO sprints (name, due_date, state_id, project_id) 
SELECT 'Sprint 5', TIMESTAMP '2025-03-20 23:59:59', s.id, p.id
FROM states s, projects p 
WHERE s.name = 'Review' AND p.name = 'Migracion Cloud';

-- Todo table inserts (corrected references to username to phone_number)
INSERT INTO todo (title, description, due_date, state_id, sprint_id, user_id, story_points)
SELECT 'Diseno BD Inventario', 
       'Crear esquema inicial de base de datos para sistema de inventario',
       TIMESTAMP '2025-03-15 23:59:59',
       s.id,
       sp.id,
       u.id,
       5
FROM states s, sprints sp, users u
WHERE s.name = 'In Progress' 
AND sp.name = 'Sprint 1'
AND u.phone_number = '+52-555-001';

INSERT INTO todo (title, description, due_date, state_id, sprint_id, user_id, story_points)
SELECT 'Sistema de Autenticacion',
       'Implementar autenticacion con tokens JWT',
       TIMESTAMP '2025-03-20 23:59:59',
       s.id,
       sp.id,
       u.id,
       8
FROM states s, sprints sp, users u
WHERE s.name = 'New'
AND sp.name = 'Sprint 1'
AND u.phone_number = '+52-555-003';

INSERT INTO todo (title, description, due_date, state_id, sprint_id, user_id, story_points)
SELECT 'Maquetacion Portal',
       'Crear disenos UI/UX para portal de clientes',
       TIMESTAMP '2025-03-10 23:59:59',
       s.id,
       sp.id,
       u.id,
       3
FROM states s, sprints sp, users u
WHERE s.name = 'Review'
AND sp.name = 'Sprint 3'
AND u.phone_number = '+52-555-002';

INSERT INTO todo (title, description, due_date, state_id, sprint_id, user_id, story_points)
SELECT 'Documentacion API',
       'Documentar endpoints de API usando OpenAPI',
       TIMESTAMP '2025-04-10 23:59:59',
       s.id,
       sp.id,
       u.id,
       5
FROM states s, sprints sp, users u
WHERE s.name = 'New'
AND sp.name = 'Sprint API - Integracion'
AND u.phone_number = '+52-555-003';

INSERT INTO todo (title, description, due_date, state_id, sprint_id, user_id, story_points)
SELECT 'Configuracion AWS',
       'Realizar setup inicial de servicios AWS',
       TIMESTAMP '2025-03-15 23:59:59',
       s.id,
       sp.id,
       u.id,
       13
FROM states s, sprints sp, users u
WHERE s.name = 'In Progress'
AND sp.name = 'Sprint 5'
AND u.phone_number = '+52-555-004';