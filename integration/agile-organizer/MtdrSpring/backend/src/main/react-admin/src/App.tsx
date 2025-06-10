import {
  Admin,
  Resource,
} from "react-admin";

import dataProvider from "./providers/dataProvider";
import authProvider from './providers/authProvider';

// Import your custom components when you create them
import { UserList, UserEdit, UserCreate, UserShow } from "./resources/users";
import { TeamList, TeamEdit, TeamCreate, TeamShow } from "./resources/teams";
import { ProjectList, ProjectEdit, ProjectCreate, ProjectShow } from "./resources/projects";
import { SprintList, SprintEdit, SprintCreate, SprintShow } from "./resources/sprints";
import { ToDoItemList, ToDoItemEdit, ToDoItemCreate, ToDoItemShow } from "./resources/todoitems";
import { StateList, StateEdit, StateCreate, StateShow } from "./resources/states";

import Charts from "./pages/Charts";
import Chatbot from "./pages/Chatbot";
import Dashboard from "./pages/Dashboard";

const ChatPage = () => (
  <div className="p-6">
    <h1 className="text-2xl font-bold mb-4">Support Chat</h1>
  </div>
);

const App = () => (
  <Admin
    dataProvider={dataProvider}
    authProvider={authProvider}
    // layout={ SolarLayout }
  >
    {permissions => (
      <>
        {/* If you want to keep permissions-based access */}
        {permissions === 'admin' && (
          <Resource
            name="users"
            list={UserList}
            edit={UserEdit}
            create={UserCreate}
            show={UserShow}
            options={{ label: 'Users' }}
          />
        )}

        <Resource
          name="dashboard"
          options={{ label: 'Dashboard' }}
          list={Dashboard}
        />

        <Resource
          name="charts"
          options={{ label: 'Charts' }}
          list={Charts}
        />  

        <Resource
          name="chatbot"
          options={{ label: 'Chatbot' }}
          list={Chatbot}
        />    
        <Resource
          name="projects"
          list={ProjectList}
          edit={ProjectEdit}
          create={ProjectCreate}
          show={ProjectShow}
          options={{ label: 'Projects' }}
        />

        <Resource
          name="teams"
          list={TeamList}
          edit={TeamEdit}
          create={TeamCreate}
          show={TeamShow}
          options={{ label: 'Teams' }}
        />

        <Resource
          name="sprints"
          list={SprintList}
          edit={SprintEdit}
          create={SprintCreate}
          show={SprintShow}
          options={{ label: 'Sprints' }}
        />

        <Resource
          name="todolist"
          list={ToDoItemList}
          edit={ToDoItemEdit}
          create={ToDoItemCreate}
          show={ToDoItemShow}
          options={{ label: 'Tasks' }}
        />

        <Resource
          name="states"
          list={StateList}
          edit={StateEdit}
          create={StateCreate}
          show={StateShow}
          options={{ label: 'States' }}
        />
      </>
    )}
  </Admin>
);

export default App;