import http from './http';
import store from '@/store/store';
import { Auth_setToken, SET_LOGIN_CHECK } from '../store/store';

// 로그인 api
export async function login(studentId, password) {
    const response = await http.post('/authenticate', {
        studentId: studentId,
        password: password
    });

    console.log(`발급받은 토큰: ${response.data.token}`);

    if(response.status === 200) { // 200일 경우에만 토큰 저장
        // auth에 setToken을 실행한다.
        store.commit(Auth_setToken, response.data.token);

        // 로그인 체크 flag를 true로 바꿈 -> 메인페이지로 이동
        store.commit(SET_LOGIN_CHECK, true);
    }

    return response;
}