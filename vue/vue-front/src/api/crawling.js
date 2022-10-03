import http from './http';
import store from '@/store/store';
import { SET_INITIAL_DATA, Subject_setSubjectInitData } from '../store/store';

// 이러닝 기본 수업 정보(과목명, 교수명, 타입) 크롤링
export async function basicSubjectList() {
    const response = await http.post('/inhatc/login-info');

    if(response.status === 200) { // 200일 경우에만 토큰 저장
        store.commit(SET_INITIAL_DATA, {subjectCardData: response.data.subjectCardData});
        // store.commit(Subject_setSubjectInitData, {subjectCardData: response.data.subjectCardData}); // 아직
    }

    return response;
}