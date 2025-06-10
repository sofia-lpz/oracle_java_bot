import { fetchUtils, DataProvider } from 'react-admin';
import { stringify } from 'query-string';

const apiUrl = '/api'; // Your Spring Boot API base path
const httpClient = fetchUtils.fetchJson;

const dataProvider: DataProvider = {
    getList: (resource, params) => {
        const { page, perPage } = params.pagination ?? { page: 1, perPage: 10 };
        const { field, order } = params.sort ?? { field: 'id', order: 'ASC' };

        const query = {
            sort: JSON.stringify([field, order]),
            range: JSON.stringify([(page - 1) * perPage, page * perPage - 1]),
            filter: JSON.stringify(params.filter),
        };

        const url = `${apiUrl}/${resource}`;

        return httpClient(url).then(({ headers, json }) => {
            return {
                data: json,
                total: parseInt(json.length, 10)
            };
        });
    },

    getOne: (resource, params) => {
        return httpClient(`${apiUrl}/${resource}/${params.id}`).then(({ json }) => ({
            data: json,
        }));
    },

    getMany: (resource, params) => {
        const query = {
            filter: JSON.stringify({ id: params.ids }),
        };
        const url = `${apiUrl}/${resource}?${stringify(query)}`;

        return httpClient(url).then(({ json }) => ({ data: json }));
    },

    getManyReference: (resource, params) => {
        const { page, perPage } = params.pagination;
        const { field, order } = params.sort;

        const query = {
            sort: JSON.stringify([field, order]),
            range: JSON.stringify([(page - 1) * perPage, page * perPage - 1]),
            filter: JSON.stringify({
                ...params.filter,
                [params.target]: params.id,
            }),
        };

        const url = `${apiUrl}/${resource}?${stringify(query)}`;

        return httpClient(url).then(({ headers, json }) => {
            return {
                data: json,
                total: parseInt(json.length, 10)
            };
        });
    },

    create: (resource, params) => {
        return httpClient(`${apiUrl}/${resource}`, {
            method: 'POST',
            body: JSON.stringify(params.data),
        }).then(({ json }) => ({
            data: json,
        }));
    },

    update: (resource, params) => {
        return httpClient(`${apiUrl}/${resource}/${params.id}`, {
            method: 'PUT',
            body: JSON.stringify(params.data),
        }).then(({ json }) => ({ data: json }));
    },

    delete: (resource, params) => {
        return httpClient(`${apiUrl}/${resource}/${params.id}`, {
            method: 'DELETE',
        }).then(({ json }) => ({ data: json }));
    },

    deleteMany: (resource, params) => {
        // This is not a native operation in many REST APIs
        // For simplicity, we'll make multiple DELETE requests
        const promises = params.ids.map(id =>
            httpClient(`${apiUrl}/${resource}/${id}`, {
                method: 'DELETE',
            })
        );
        return Promise.all(promises).then(() => ({ data: params.ids }));
    },

    updateMany: (resource, params) => {
        // Similar to deleteMany, we'll make multiple PUT requests
        const promises = params.ids.map(id =>
            httpClient(`${apiUrl}/${resource}/${id}`, {
                method: 'PUT',
                body: JSON.stringify(params.data),
            })
        );
        return Promise.all(promises).then(() => ({ data: params.ids }));
    },

    getChat: (text: string) => {
        const url = `${apiUrl}/chat`;

        return httpClient(url, {
            method: 'POST',
            body: JSON.stringify(text)
        }).then(({ json }) => ({
            data: json
        }));
    }
};

export default dataProvider;