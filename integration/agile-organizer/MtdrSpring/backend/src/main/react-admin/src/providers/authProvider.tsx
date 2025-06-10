import { AuthProvider } from 'react-admin';

const authProvider: AuthProvider = {
    login: ({ username, password }) => {
        const request = new Request('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ phoneNumber: username, password }),
            headers: new Headers({ 'Content-Type': 'application/json' }),
        });
        return fetch(request)
            .then(response => {
                if (response.status < 200 || response.status >= 300) {
                    throw new Error(response.statusText);
                }
                return response.json();
            })
            .then(auth => {
                localStorage.setItem('token', auth.token);
                localStorage.setItem('permissions', auth.role || 'user');
                return { redirectTo: '/' };
            })
            .catch(() => {
                throw new Error('Network error or invalid credentials');
            });
    },
    
    logout: () => {
        const request = new Request('/auth/logout', {
            method: 'POST',
            headers: new Headers({
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }),
        });

        return fetch(request)
            .then(() => {
                localStorage.removeItem('token');
                localStorage.removeItem('permissions');
                return '/login';
            })
            .catch(() => {
                localStorage.removeItem('token');
                localStorage.removeItem('permissions');
                return '/login';
            });
    },
    
    checkError: (error) => {
        const status = error.status;
        if (status === 401 || status === 403) {
            localStorage.removeItem('token');
            localStorage.removeItem('permissions');
            return Promise.reject({ message: 'Unauthorized' });
        }
        return Promise.resolve();
    },
    
    checkAuth: () => {
        return localStorage.getItem('token') ? Promise.resolve() : Promise.reject({ message: 'Login required' });
    },
    
    getPermissions: () => {
        const role = localStorage.getItem('permissions');
        return role ? Promise.resolve(role) : Promise.reject();
    },
    
    getIdentity: () => {
        // You might want to fetch actual user data from a /api/users/me endpoint
        return Promise.resolve({
            id: 'user',
            fullName: 'Logged User',
        });
    }
};

export default authProvider;