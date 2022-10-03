// 로그인 api
export async function login(studentId, password) {
    return http.post('/inhatc/login-info', {
        studentId: studentId,
        password: password
    });
}