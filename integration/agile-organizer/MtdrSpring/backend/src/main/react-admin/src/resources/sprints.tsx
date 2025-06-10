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
    ReferenceManyField
} from 'react-admin';

export const SprintList = () => (
    <List>
        <Datagrid rowClick="show">
            <TextField source="id" />
            <TextField source="name" />
            <DateField source="dueDate" />
            <ReferenceField source="state.id" reference="states" link="show">
                <TextField source="name" />
            </ReferenceField>
            <ReferenceField source="project.id" reference="projects" link="show">
                <TextField source="name" />
            </ReferenceField>
        </Datagrid>
    </List>
);

export const SprintEdit = () => (
    <Edit>
        <SimpleForm>
            <TextInput source="id" disabled />
            <TextInput source="name" validate={required()} />
            <DateInput source="dueDate" />
            <ReferenceInput source="state.id" reference="states">
                <SelectInput optionText="name" />
            </ReferenceInput>
            <ReferenceInput source="project.id" reference="projects">
                <SelectInput optionText="name" />
            </ReferenceInput>
        </SimpleForm>
    </Edit>
);

export const SprintCreate = () => (
    <Create>
        <SimpleForm>
            <TextInput source="name" validate={required()} />
            <DateInput source="dueDate" />
            <ReferenceInput source="state.id" reference="states">
                <SelectInput optionText="name" />
            </ReferenceInput>
            <ReferenceInput source="project.id" reference="projects">
                <SelectInput optionText="name" />
            </ReferenceInput>
        </SimpleForm>
    </Create>
);

export const SprintShow = () => (
    <Show>
        <SimpleShowLayout>
            <TextField source="id" />
            <TextField source="name" />
            <DateField source="dueDate" />
            <DateField source="creation_ts" label="Created At" />
            <ReferenceField source="state.id" reference="states" link="show">
                <TextField source="name" />
            </ReferenceField>
            <ReferenceField source="project.id" reference="projects" link="show">
                <TextField source="name" />
            </ReferenceField>
            
            <ReferenceManyField label="Tasks" reference="todolist" target="sprint.id">
                <Datagrid>
                    <TextField source="title" />
                    <TextField source="description" />
                    <ReferenceField source="state.id" reference="states">
                        <TextField source="name" />
                    </ReferenceField>
                </Datagrid>
            </ReferenceManyField>
        </SimpleShowLayout>
    </Show>
);