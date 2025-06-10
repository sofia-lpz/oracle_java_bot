import {
    List,
    Datagrid,
    TextField,
    DateField,
    Edit,
    SimpleForm,
    TextInput,
    Create,
    required,
    Show,
    SimpleShowLayout,
    ReferenceField,
    ReferenceInput,
    SelectInput,
    DateInput,
    NumberInput,
    NumberField,
    BooleanField,
    BooleanInput
} from 'react-admin';

export const ToDoItemList = () => (
    <List>
        <Datagrid rowClick="show">
            <TextField source="id" />
            <TextField source="title" />
            <TextField source="description" />
            <BooleanField source="done" />
            <DateField source="dueDate" />
            <NumberField source="storyPoints" />
            <TextField source="priority" />
            <NumberField source="estimated_hours" />
            <NumberField source="real_hours" />
            <ReferenceField source="user.id" reference="users" link="show">
                <TextField source="name" />
            </ReferenceField>
            <ReferenceField source="sprint.id" reference="sprints" link="show">
                <TextField source="name" />
            </ReferenceField>
            <ReferenceField source="state.id" reference="states" link="show">
                <TextField source="name" />
            </ReferenceField>
        </Datagrid>
    </List>
);

export const ToDoItemEdit = () => (
    <Edit>
        <SimpleForm>
            <TextInput source="id" disabled />
            <TextInput source="title" validate={required()} />
            <TextInput source="description" multiline />
            <BooleanInput source="done" />
            <DateInput source="dueDate" />
            <NumberInput source="storyPoints" />
            <SelectInput source="priority" choices={[
                { id: 'low', name: 'Low' },
                { id: 'medium', name: 'Medium' },
                { id: 'high', name: 'High' }
            ]} />
            <NumberInput source="estimated_hours" />
            <NumberInput source="real_hours" />
            <ReferenceInput source="user.id" reference="users">
                <SelectInput optionText="name" />
            </ReferenceInput>
            <ReferenceInput source="sprint.id" reference="sprints">
                <SelectInput optionText="name" />
            </ReferenceInput>
            <ReferenceInput source="state.id" reference="states">
                <SelectInput optionText="name" />
            </ReferenceInput>
        </SimpleForm>
    </Edit>
);

export const ToDoItemCreate = () => (
    <Create>
        <SimpleForm>
            <TextInput source="title" validate={required()} />
            <TextInput source="description" multiline />
            <BooleanInput source="done" />
            <DateInput source="dueDate" />
            <NumberInput source="storyPoints" />
            <SelectInput source="priority" choices={[
                { id: 'low', name: 'Low' },
                { id: 'medium', name: 'Medium' },
                { id: 'high', name: 'High' }
            ]} />
            <NumberInput source="estimated_hours" />
            <NumberInput source="real_hours" />
            <ReferenceInput source="user.id" reference="users">
                <SelectInput optionText="name" />
            </ReferenceInput>
            <ReferenceInput source="sprint.id" reference="sprints">
                <SelectInput optionText="name" />
            </ReferenceInput>
            <ReferenceInput source="state.id" reference="states">
                <SelectInput optionText="name" />
            </ReferenceInput>
        </SimpleForm>
    </Create>
);

export const ToDoItemShow = () => (
    <Show>
        <SimpleShowLayout>
            <TextField source="id" />
            <TextField source="title" />
            <TextField source="description" />
            <BooleanField source="done" />
            <DateField source="dueDate" />
            <DateField source="creation_ts" label="Created At" />
            <NumberField source="storyPoints" />
            <TextField source="priority" />
            <NumberField source="estimated_hours" />
            <NumberField source="real_hours" />
            <ReferenceField source="user.id" reference="users" link="show">
                <TextField source="name" />
            </ReferenceField>
            <ReferenceField source="sprint.id" reference="sprints" link="show">
                <TextField source="name" />
            </ReferenceField>
            <ReferenceField source="state.id" reference="states" link="show">
                <TextField source="name" />
            </ReferenceField>
            <BooleanField source="deleted" />
        </SimpleShowLayout>
    </Show>
);