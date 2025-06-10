import {
    List,
    Datagrid,
    TextField,
    Edit,
    SimpleForm,
    TextInput,
    Create,
    required,
    Show,
    SimpleShowLayout,
    ReferenceManyField,
    ReferenceField,
    NumberInput,
    NumberField,
    DateField
} from 'react-admin';

export const StateList = () => (
    <List>
        <Datagrid rowClick="show">
            <TextField source="id" />
            <TextField source="name" />
            <TextField source="description" />
            <NumberField source="workflow_priority" />
        </Datagrid>
    </List>
);

export const StateEdit = () => (
    <Edit>
        <SimpleForm>
            <TextInput source="id" disabled />
            <TextInput source="name" validate={required()} />
            <TextInput source="description" multiline />
            <NumberInput source="workflow_priority" />
        </SimpleForm>
    </Edit>
);

export const StateCreate = () => (
    <Create>
        <SimpleForm>
            <TextInput source="name" validate={required()} />
            <TextInput source="description" multiline />
            <NumberInput source="workflow_priority" />
        </SimpleForm>
    </Create>
);

export const StateShow = () => (
    <Show>
        <SimpleShowLayout>
            <TextField source="id" />
            <TextField source="name" />
            <TextField source="description" />
            <NumberField source="workflow_priority" />
            <DateField source="creation_ts" label="Created At" />
            
            <ReferenceManyField label="Tasks in this state" reference="todolist" target="state.id">
                <Datagrid>
                    <TextField source="title" />
                    <TextField source="priority" />
                    <ReferenceField source="user.id" reference="users">
                        <TextField source="username" />
                    </ReferenceField>
                </Datagrid>
            </ReferenceManyField>
            
            <ReferenceManyField label="Sprints in this state" reference="sprints" target="state.id">
                <Datagrid rowClick="show">
                    <TextField source="name" />
                    <DateField source="dueDate" />
                    <ReferenceField source="project.id" reference="projects">
                        <TextField source="name" />
                    </ReferenceField>
                </Datagrid>
            </ReferenceManyField>
        </SimpleShowLayout>
    </Show>
);