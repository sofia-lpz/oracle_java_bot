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
    ReferenceManyField,
    SingleFieldList,
    ChipField
} from 'react-admin';

export const ProjectList = () => (
    <List>
        <Datagrid rowClick="show">
            <TextField source="id" />
            <TextField source="name" />
            <TextField source="description" />
            <DateField source="startDate" />
            <DateField source="endDate" />
            <ReferenceField source="stateId" reference="states" link="show">
                <TextField source="name" />
            </ReferenceField>
            <ReferenceField source="teamId" reference="teams" link="show">
                <TextField source="name" />
            </ReferenceField>
        </Datagrid>
    </List>
);

export const ProjectEdit = () => (
    <Edit>
        <SimpleForm>
            <TextInput source="id" disabled />
            <TextInput source="name" validate={required()} />
            <TextInput source="description" multiline />
            <DateInput source="endDate" label="Due Date" />
            <ReferenceInput source="stateId" reference="states">
                <SelectInput optionText="name" />
            </ReferenceInput>
            <ReferenceInput source="teamId" reference="teams">
                <SelectInput optionText="name" />
            </ReferenceInput>
        </SimpleForm>
    </Edit>
);

export const ProjectCreate = () => (
    <Create>
        <SimpleForm>
            <TextInput source="name" validate={required()} />
            <TextInput source="description" multiline />
            <DateInput source="endDate" label="Due Date" />
            <ReferenceInput source="stateId" reference="states">
                <SelectInput optionText="name" />
            </ReferenceInput>
            <ReferenceInput source="teamId" reference="teams">
                <SelectInput optionText="name" />
            </ReferenceInput>
        </SimpleForm>
    </Create>
);

export const ProjectShow = () => (
    <Show>
        <SimpleShowLayout>
            <TextField source="id" />
            <TextField source="name" />
            <TextField source="description" />
            <DateField source="startDate" />
            <DateField source="endDate" label="Due Date" />
            <ReferenceField source="stateId" reference="states" link="show">
                <TextField source="name" />
            </ReferenceField>
            <ReferenceField source="teamId" reference="teams" link="show">
                <TextField source="name" />
            </ReferenceField>
            
            <ReferenceManyField label="Sprints" reference="sprints" target="projectId">
                <SingleFieldList>
                    <ChipField source="name" />
                </SingleFieldList>
            </ReferenceManyField>
        </SimpleShowLayout>
    </Show>
);