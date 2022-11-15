import http from './http';
import store from '@/store/store';
import { SET_CRAWL_LECTURES_DATA } from '../store/store';

// 이러닝 기본 수업 정보(과목명, 교수명, 타입) 크롤링
export async function loadPrevMessage(subjectId) {
    let returnResponse;
    await http.get(`/chat/${subjectId}`).then((response)=>{
        // 크롤링을 한 뒤에 기본 틀을 만들어줌
        returnResponse = response;
        // store.commit(SET_INITIAL_DATA, {subjectCardData: response.data.subjectCardData}); 
    });
    console.log(returnResponse);
    return returnResponse;
}