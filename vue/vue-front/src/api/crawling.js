import http from './http';
import store from '@/store/store';
import { SET_CRAWL_LECTURES_DATA, SET_CRAWL_NOTICE_DATA, SET_CRAWL_TASK_DATA, SET_INITIAL_DATA, SET_SUBJECT_COUNT, Subject_setSubjectInitData } from '../store/store';

// 이러닝 기본 수업 정보(과목명, 교수명, 타입) 크롤링
export async function basicSubjectList() {
    const response = await http.post('/inhatc/login-info').then((response)=>{
        // 크롤링을 한 뒤에 기본 틀을 만들어줌
        store.commit(SET_INITIAL_DATA, {subjectCardData: response.data.subjectCardData}); 
    });

    return response;
}

export async function getAllSubjectInfoList({subjectId, cardIdx}) {
    const response = http.post(`/inhatc/subject/${subjectId}/all`).then((response) => {
        store.commit([SET_CRAWL_LECTURES_DATA], {cardIndex: cardIdx, lecturesData: response.data.lecture});
        store.commit([SET_CRAWL_NOTICE_DATA], {cardIndex: cardIdx, noticeData: response.data.notice});
        store.commit([SET_CRAWL_TASK_DATA], {cardIndex: cardIdx, taskData: response.data.task});
        store.commit([SET_SUBJECT_COUNT], {cardIndex: cardIdx, counts: response.data.subjectCounts});
    });

    return response;
}