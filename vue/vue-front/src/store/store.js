import Vue from 'vue';
import Vuex from 'vuex';
import axios from "axios"
Vue.use(Vuex);

export const Auth_setToken = 'auth/setToken';

export const User_setUserInfo = 'user/setUserInfo';
export const User_setStudentId = 'user/setStudentId';
export const User_getStudentId = 'user/showStudentId';
export const User_showNickname = 'user/showNickname';


export const Popup_showSignUpSuccess = 'popup/showSuccess'; // signup 성공 팝업 띄우기
export const Popup_closedSignup = 'popup/closedSignup'; // signup 성공 팝업 닫기

export const Subject_setSubjectInitData = 'subject/setSubjectInitData'; // subjectCard 형식 초기화 및 교수, 과목이름 데이터 삽입

export const SET_LOGIN_CHECK = 'SET_LOGIN_CHECK'; // 로그인 완료 true 로그인->메인페이지 전환할 때 해야함
export const SET_LOGIN_FLAG = 'SET_LOGIN_FLAG'; // 로그인 - 회원가입 화면 전환을 위한 플레그 설정  true: 로그인 false: 회원가입

export const LOGIN_CHECK_AND_CALCULATE_TO_DO_NUMBER = 'LoginCheckANDcalculateToDoNumber'; // 사용자 프로필 정보 최신화(미제출 과제, 제출한 과제 개수 등등) - 프로필 mounted에서 진행

export const SET_INITIAL_DATA = 'SET_INITIAL_DATA'; // subjectCard 형식 초기화 및 교수, 과목이름 데이터 삽입
export const SET_STUDENT_ID = 'SET_STUDENT_ID'; // 사용자 ID 설정
export const CRAWL_SUBJECT = 'crawlSubject'; // 사용자 과목 크롤링
export const SET_CRAWL_NOTICE_DATA = 'SET_CRAWL_NOTICE_DATA'; // 크롤링한 공지 데이터 세팅
export const SET_CRAWL_TASK_DATA = 'SET_CRAWL_TASK_DATA'; // 크롤링한 과제 데이터 세팅
export const SET_CRAWL_LECTURES_DATA = 'SET_CRAWL_LECTURES_DATA'; // 크롤링한 수업 데이터 세팅
export const SET_SUBJECT_COUNT = 'SET_SUBJECT_COUNT'; // 크롤링한 데이터 카운트 매기기

export const SET_DB_NOTICE_DATA = 'SET_DB_NOTICE_DATA';
export const SET_DB_TASK_DATA = 'SET_DB_TASK_DATA';
export const SET_DB_LECTURES_DATA = 'SET_DB_LECTURES_DATA';
export const INCREASE_COMPLETED_TASK = 'INCREASE_COMPLETED_TASK';
export const INCREASE_INCOMPLETED_TASK = 'INCREASE_INCOMPLETED_TASK';
export const LOAD_DB_SUBJECT = 'loadDBSubject';

const api = axios.create({baseURL: 'http://localhost:8080'}); // 이거는 api에 js로 대체하고 지워버릴것이다
import * as crawlingApi from '@/api/crawling';

import auth from './auth';
import user from './user';
import subject from './subject';
import popup from './popup';

export default new Vuex.Store({
    modules: {
        auth,
        user,
        subject,
        popup,
    },
    state: {
        //로그인 - 회원가입 전환을 위한 플레그 true:로그인페이지 false: 회원가입페이지
        loginFlag: true,

        loginCheck: false,
        loginLoading: false,
        studentName: '',
        studentNumber: '',
        subjectCardData: [],
        
        completedTask: 0,
        incompletedTask: 0,
        showCompleted: 0,
        showIncompleted: 0,

        //강의
        completedLecture: 0,
        incompletedLecture: 0,
        showCompletedLecture: 0,
        showIncompletedLecture: 0,
    }, //Vue에 data와 비슷
    getters: {
        showTotalTask(state) {
            return state.showCompleted + state.showIncompleted;
        },
        showTotalLecture(state) {
            return state.showCompletedLecture + state.showIncompletedLecture;
        },
    },//Vue의 computed와 비슷 
    //완전히 같은게 아니라 비슷하다는 것 명심
    mutations: {
        [SET_LOGIN_FLAG](state, flag) {
            state.loginFlag = flag;
        },
        [SET_LOGIN_CHECK](state, loginCheck) {
            state.loginCheck = loginCheck;
        },
        [SET_STUDENT_ID](state, studentId) {
            state.studentNumber = studentId;
        },
        //subject.js로 이전 준비 완료
        [SET_INITIAL_DATA](state, {subjectCardData: cardData /*studentName: name, studentNumber: number*/}) {
            // state.studentName = name;
            // state.studentNumber = number;
            // state.subjectCardData = cardData;
            let objs = cardData;
            for (let i = 0; i < objs.length; i++) {
                const element = objs[i];
                element['isCrawling'] = [true, true, true];
                element['lectures'] = [];
                element['notice'] = [];
                element['task'] = [];
                element['cntCompletedLecture'] = 0;
                element['cntIncompletedLecture'] = 0;
                element['cntCompletedTask'] = 0;
                element['cntIncompletedTask'] = 0;
                element['cntCompletedTotal'] = 0;
                element['cntIncompletedTotal'] = 0;
            }
            // console.log(objs);
            state.subjectCardData = objs;
        },
        [INCREASE_INCOMPLETED_TASK](state) {
            state.incompletedTask += 1;
        },
        [INCREASE_COMPLETED_TASK](state) {
            state.completedTask += 1;
        },
        [SET_SUBJECT_COUNT](state, {cardIndex: index, counts: data}) {
            state.subjectCardData[index].cntCompletedLecture = data.cntCompletedLecture;
            state.subjectCardData[index].cntIncompletedLecture = data.cntIncompletedLecture;
            state.subjectCardData[index].cntCompletedTask = data.cntCompletedTask;
            state.subjectCardData[index].cntIncompletedTask = data.cntIncompletedTask;
            state.subjectCardData[index].cntCompletedTotal = data.cntCompletedTotal;
            state.subjectCardData[index].cntIncompletedTotal = data.cntIncompletedTotal;

            state.completedLecture += data.cntCompletedLecture;
            state.incompletedLecture += data.cntIncompletedLecture;

            state.completedTask += data.cntCompletedTask;
            state.incompletedTask += data.cntIncompletedTask;
        },
        [SET_CRAWL_LECTURES_DATA](state, {cardIndex: index, lecturesData: data}) {
            if(data == null) {
                Vue.set(state.subjectCardData[index].isCrawling, 0, false);
                return;
            }

            if(data.length != 0) {
                for(let i = 0; i < data.length; i++) {
                    // console.log(data[i]);
                    state.subjectCardData[index].lectures[i] = { 
                        lectureWeekId: data[i].lectureWeekId, 
                        title: data[i].title, 
                        endDate: data[i].endDate, 
                        startDate: data[i].startDate, 
                        lectures: data[i].lectures, 
                        cntCompleted: data[i].cntCompleted, 
                        cntIncompleted: data[i].cntIncompleted, 
                        learningState: data[i].studyingState,
                    };
                }
                
            }
            else {
                state.subjectCardData[index].lectures = [];
            }
            Vue.set(state.subjectCardData[index].isCrawling, 0, false);
            // console.log(state.subjectCardData[index].lectures);
        },
        [SET_CRAWL_NOTICE_DATA](state, {cardIndex: index, noticeData: data}) {
            if(data == null) {
                Vue.set(state.subjectCardData[index].isCrawling, 1, false);
                return;
            }
            if(data.length != 0) {
                for(let i = 0; i < data.length; i++) {
                    state.subjectCardData[index].notice[i] = { 
                        noticeId: data[i].noticeId, 
                        title: data[i].title, 
                        description: data[i].description, 
                        author: data[i].author, 
                        createDate: data[i].createDate
                    };
                }
            }
            else {
                state.subjectCardData[index].notice = [];
            }
            Vue.set(state.subjectCardData[index].isCrawling, 1, false);
        },
        [SET_CRAWL_TASK_DATA](state, {cardIndex: index, taskData: data}) {
            if(data == null) {
                Vue.set(state.subjectCardData[index].isCrawling, 2, false);
                return;
            }

            if(data.length != 0) {
                for(let i = 0; i < data.length; i++) {
                    state.subjectCardData[index].task[i] = { 
                        taskId: data[i].taskId, 
                        title: data[i].title, 
                        description: data[i].description, 
                        startDate: data[i].startDate, 
                        endDate: data[i].endDate.toString(), 
                        notSubmittedNum: data[i].notSubmittedNum, 
                        submissionNum: data[i].submissionNum, 
                        totalNum: data[i].totalNum, 
                        submitYN: data[i].submitYN, 
                        
                        //여기는 TaskTable에 제출현황을 보여주기위해 추가함
                        submittedState: data[i].submissionNum / data[i].totalNum* 100,
                    };
                    
                    // if(state.subjectCardData[index].task[i].submitYN == 'Y') {
                    //     state.incompletedTask += 1;
                    // } else if(state.subjectCardData[index].task[i].submitYN == 'N') {
                    //     state.completedTask += 1;
                    // }
                }
            }
            else {
                state.subjectCardData[index].task = [];
            }
            Vue.set(state.subjectCardData[index].isCrawling, 2, false);
            // console.log(state.subjectCardData[index].task);
        },
        [SET_DB_LECTURES_DATA](state, {cardIndex: index, lecturesData: data}) {
            if(data.length != 0) {
                for(let i = 0; i < data.length; i++) {
                    state.subjectCardData[index].notice[i] = { 
                        noticeId: data[i].noticeId, 
                        title: data[i].title, 
                        description: data[i].description, 
                        author: data[i].author, 
                        createDate: data[i].createDate
                    };
                }
            }
            else {
                state.subjectCardData[index].notice = [];
            }
        },
        [SET_DB_NOTICE_DATA](state, {cardIndex: index, noticeData: data}) {
            if(data.length != 0) {
                for(let i = 0; i < data.length; i++) {
                    state.subjectCardData[index].notice[i] = { 
                        noticeId: data[i].noticeId, 
                        title: data[i].title, 
                        description: data[i].description, 
                        author: data[i].author, 
                        createDate: data[i].createDate
                    };
                }
            }
            else {
                state.subjectCardData[index].notice = [];
            }
        },
        [SET_DB_TASK_DATA](state, {cardIndex: index, taskData: data}) {
            if(data.length != 0) {
                for(let i = 0; i < data.length; i++) {
                    state.subjectCardData[index].task[i] = { 
                        taskId: data[i].taskId, 
                        title: data[i].title, 
                        description: data[i].description, 
                        startDate: data[i].startDate, 
                        endDate: data[i].endDate.toString(), 
                        notSubmittedNum: data[i].notSubmittedNum, 
                        submissionNum: data[i].submissionNum, 
                        totalNum: data[i].totalNum, 
                        submitYN: data[i].submitYN, 
                        
                        //여기는 TaskTable에 제출현황을 보여주기위해 추가함
                        submittedState: data[i].submissionNum / data[i].totalNum* 100,
                    };
                }
            }
            else {
                state.subjectCardData[index].task = [];
            }
        },
    }, //state를 동기적으로 수정할 때 사용
    //state를 바꿀때 바로 바꾸지말고 mutations를 통해 바꾸길 권장
    actions: {
        async [LOAD_DB_SUBJECT](context) {
            //각 과목들에 대한 db데이터 불러오기
            for(let i = 0; i < this.state.subjectCardData.length; i++) {
                await api.post('/getSubjectInDB', null, {params: {
                    subjectId: this.state.subjectCardData[i].subjectId,
                }}).then((response) => {
                    console.log(response.data);
                    context.commit([SET_CRAWL_LECTURES_DATA], {cardIndex: i, lecturesData: response.data.lecture});
                    context.commit([SET_CRAWL_NOTICE_DATA], {cardIndex: i, noticeData: response.data.notice});
                    context.commit([SET_CRAWL_TASK_DATA], {cardIndex: i, taskData: response.data.task});
                    context.commit([SET_SUBJECT_COUNT], {cardIndex: i, counts: response.data.subjectCounts});
                });
            }

            // for(let i = 0; i < this.state.subjectCardData.length; i++) {
            //     await api.post('/lectureDB', null, {params: {
            //         subjectId: this.state.subjectCardData[i].subjectId,
            //     }}).then((response) => {
            //         console.log(response.data);
            //         context.commit([SET_DB_LECTURES_DATA], {cardIndex: i, lecturesData: response.data});
            //     });
            //     await api.post('/taskDB', null, {params: {
            //         subjectId: this.state.subjectCardData[i].subjectId,
            //     }}).then((response) => {
            //         console.log(response.data);
            //         context.commit([SET_DB_NOTICE_DATA], {cardIndex: i, noticeData: response.data});
            //     });
            //     await api.post('/noticeDB', null, {params: {
            //         subjectId: this.state.subjectCardData[i].subjectId,
            //     }}).then((response) => {
            //         console.log(response.data);
            //         context.commit([SET_DB_TASK_DATA], {cardIndex: i, taskData: response.data});
            //     });

            // }
        },
        async [CRAWL_SUBJECT](context) {
            //각 과목들에 대한 크롤링 진행
            for(let i = 0; i < this.state.subjectCardData.length; i++) {
                crawlingApi.getAllSubjectInfoList({subjectId: this.state.subjectCardData[i].subjectId, cardIdx: i}); 
            }
        },
        [LOGIN_CHECK_AND_CALCULATE_TO_DO_NUMBER](context) {
            //로그인 후 프로필에 해야할 일 최신화.
            // context.commit(SET_LOGIN_CHECK, true);
            let timer = setInterval(()=>{
                if(this.state.completedTask != this.state.showCompleted) this.state.showCompleted += 1;
                if(this.state.incompletedTask != this.state.showIncompleted) this.state.showIncompleted += 1;
                
                if(this.state.completedLecture != this.state.showCompletedLecture) this.state.showCompletedLecture += 1;
                if(this.state.incompletedLecture != this.state.showIncompletedLecture) this.state.showIncompletedLecture += 1;
                
                // console.log('완료과제 : ' + this.state.completedTask+' == '+ this.state.showCompleted + ', 미완료과제: ' + this.state.incompletedTask+' == '+ this.state.showIncompleted + ', ' + this.state.subjectCardData[this.state.subjectCardData.length-1].isCrawling[2]);
                if(this.state.subjectCardData[this.state.subjectCardData.length-1] !== undefined &&
                    !this.state.subjectCardData[this.state.subjectCardData.length-1].isCrawling[2] && 
                    this.state.completedTask == this.state.showCompleted && 
                    this.state.incompletedTask == this.state.showIncompleted &&
                    this.state.completedLecture == this.state.showCompletedLecture && 
                    this.state.incompletedLecture == this.state.showIncompletedLecture) {
                    clearInterval(timer);
                }
            }, 100);
        }
    }, // 비동기를 사용할 때, 또는 여러 뮤테이션을 연달아 실행할 때
});
