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
    ReferenceField,
    ReferenceInput,
    SelectInput,
    PasswordInput,
    NumberInput
} from 'react-admin';

export const UserList = () => (
    <List>
        <Datagrid rowClick="show">
            <TextField source="id" />
            <TextField source="name" />
            <TextField source="phoneNumber" />
            <TextField source="role" />
            <ReferenceField source="team.id" reference="teams" link="show">
                <TextField source="teamName" />
            </ReferenceField>
        </Datagrid>
    </List>
);

export const UserEdit = () => (
    <Edit>
        <SimpleForm>
            <TextInput source="id" disabled />
            <TextInput source="name" validate={required()} />
            <TextInput source="phoneNumber" validate={required()} />
            <TextInput source="role" />
            <NumberInput source="telegramChatId" />
            <ReferenceInput source="team.id" reference="teams">
                <SelectInput optionText="teamName" />
            </ReferenceInput>
            <PasswordInput source="password" />
        </SimpleForm>
    </Edit>
);

export const UserCreate = () => (
    <Create>
        <SimpleForm>
            <TextInput source="name" validate={required()} />
            <TextInput source="phoneNumber" validate={required()} />
            <TextInput source="role" defaultValue="USER" />
            <NumberInput source="telegramChatId" />
            <ReferenceInput source="team.id" reference="teams">
                <SelectInput optionText="teamName" />
            </ReferenceInput>
            <PasswordInput source="password" validate={required()} />
        </SimpleForm>
    </Create>
);

export const UserShow = () => (
    <Show>
        <SimpleShowLayout>
            <TextField source="id" />
            <TextField source="name" />
            <TextField source="phoneNumber" />
            <TextField source="role" />
            <TextField source="telegramChatId" />
            <ReferenceField source="team.id" reference="teams" link="show">
                <TextField source="teamName" />
            </ReferenceField>
        </SimpleShowLayout>
    </Show>
);