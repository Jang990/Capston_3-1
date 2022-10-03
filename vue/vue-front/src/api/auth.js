import http from './http';

// 로그인 api
export async function login(studentId, password) {
    return http.post('/authenticate', {
        studentId: studentId,
        password: password
    });
}