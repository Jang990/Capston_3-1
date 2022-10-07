import http from './http';
import store from '@/store/store';
import { User_setUserInfo } from '../store/store';

// 현재 로그인한 사용자의 기본정보 가져오기
export async function getUserInfo() {
    const response = await http.get('/users');
    // console.log(response);
    
    if(response.status === 200) {
        store.commit(User_setUserInfo, 
            {
                studentId: response.data.studentId, 
                nickname: response.data.nickname, 
                createdDate : response.data.createdDate
            }
        );
    }

    return response;
}