import http from './http';
import store from '@/store/store';
import { SET_DB_LECTURES_DATA, SET_DB_NOTICE_DATA, SET_DB_TASK_DATA, SET_INITIAL_DATA } from '../store/store';

// 현재 로그인한 사용자의 기본 수업 정보 전부 가져오기
export async function getUserSubjectInfo({cardIdx, studentId, subjectId}) {
    const response = await http.get(`/users/${studentId}/subject/${subjectId}/all`).then((response) => {
        store.commit([SET_DB_LECTURES_DATA], {cardIndex: cardIdx, lecturesData: response.data.lecture});
        store.commit([SET_DB_NOTICE_DATA], {cardIndex: cardIdx, noticeData: response.data.notice});
        store.commit([SET_DB_TASK_DATA], {cardIndex: cardIdx, taskData: response.data.task});
        // store.commit([SET_SUBJECT_COUNT], {cardIndex: cardIdx, counts: response.data.subjectCounts});
    });


    return response;
}

// 사용자가 가지고 있는 subject 기본 정보 가져오기
export async function basicSubjectList({studentId}) {
    let returnResponse;
    await http.get(`/users/${studentId}/subject`).then((response)=>{
        // 크롤링을 한 뒤에 기본 틀을 만들어줌
        returnResponse = response;
        store.commit(SET_INITIAL_DATA, {subjectCardData: response.data.subjectCardData}); 
    });

    return returnResponse;
}