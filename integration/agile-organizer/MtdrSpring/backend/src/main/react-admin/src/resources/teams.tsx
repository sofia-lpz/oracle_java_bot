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
            <TextField source="name" />
            <TextField source="description" />
        </Datagrid>
    </List>
);

export const TeamEdit = () => (
    <Edit>
        <SimpleForm>
            <TextInput source="id" disabled />
            <TextInput source="name" validate={required()} />
            <TextInput source="description" multiline />
        </SimpleForm>
    </Edit>
);

export const TeamCreate = () => (
    <Create>
        <SimpleForm>
            <TextInput source="name" validate={required()} />
            <TextInput source="description" multiline />
        </SimpleForm>
    </Create>
);

export const TeamShow = () => (
    <Show>
        <SimpleShowLayout>
            <TextField source="id" />
            <TextField source="name" />
            <TextField source="description" />
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