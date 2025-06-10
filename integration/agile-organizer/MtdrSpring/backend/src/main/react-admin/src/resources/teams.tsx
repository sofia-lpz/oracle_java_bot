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
    SingleFieldList,
    ChipField,
    DateField
} from 'react-admin';

export const TeamList = () => (
    <List>
        <Datagrid rowClick="show">
            <TextField source="id" />
            <TextField source="teamName" />
            <TextField source="teamDescription" />
        </Datagrid>
    </List>
);

export const TeamEdit = () => (
    <Edit>
        <SimpleForm>
            <TextInput source="id" disabled />
            <TextInput source="teamName" validate={required()} />
            <TextInput source="teamDescription" multiline />
        </SimpleForm>
    </Edit>
);

export const TeamCreate = () => (
    <Create>
        <SimpleForm>
            <TextInput source="teamName" validate={required()} />
            <TextInput source="teamDescription" multiline />
        </SimpleForm>
    </Create>
);

export const TeamShow = () => (
    <Show>
        <SimpleShowLayout>
            <TextField source="id" />
            <TextField source="teamName" />
            <TextField source="teamDescription" />
            <DateField source="creation_ts" label="Created At" />
            
            <ReferenceManyField label="Team Members" reference="users" target="team.id">
                <SingleFieldList>
                    <ChipField source="username" />
                </SingleFieldList>
            </ReferenceManyField>
            
            <ReferenceManyField label="Projects" reference="projects" target="team.id">
                <SingleFieldList>
                    <ChipField source="name" />
                </SingleFieldList>
            </ReferenceManyField>
        </SimpleShowLayout>
    </Show>
);