import http from './http';
import store from '@/store/store';
import { SET_DB_LECTURES_DATA, SET_DB_NOTICE_DATA, SET_DB_TASK_DATA } from '../store/store';

// 현재 로그인한 사용자의 기본정보 가져오기
export async function getUserSubjectInfo({cardIdx, studentId, subjectId}) {
    const response = await http.get(`/users/${studentId}/subject/${subjectId}/all`).then((response) => {
        store.commit([SET_DB_LECTURES_DATA], {cardIndex: cardIdx, lecturesData: response.data.lecture});
        store.commit([SET_DB_NOTICE_DATA], {cardIndex: cardIdx, noticeData: response.data.notice});
        store.commit([SET_DB_TASK_DATA], {cardIndex: cardIdx, taskData: response.data.task});
        // store.commit([SET_SUBJECT_COUNT], {cardIndex: cardIdx, counts: response.data.subjectCounts});
    });


    return response;
}