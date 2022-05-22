import Vue from 'vue';
import Vuex from 'vuex';
import axios from "axios"
Vue.use(Vuex);

export const SET_WINNER = 'SET_WINNER';
export const SET_LOGIN_CHECK = 'SET_LOGIN_CHECK';
export const SET_INITIAL_DATA = 'SET_INITIAL_DATA';
export default new Vuex.Store({
    state: {
        loginCheck: false,
        studentName: '',
        studentNumber: '',
        subjectCardData: [],
    }, //Vue에 data와 비슷
    getters: {

    },//Vue의 computed와 비슷 
    //완전히 같은게 아니라 비슷하다는 것 명심
    mutations: {
        [SET_WINNER](state, winner) {
            state.winner = winner;
        },
        [SET_LOGIN_CHECK](state, loginCheck) {
            state.loginCheck = loginCheck;
        },
        [SET_INITIAL_DATA](state, {studentName: name, studentNumber: number, subjectCardData: cardData}) {
            state.studentName = name;
            state.studentNumber = number;
            // state.subjectCardData = cardData;
            let objs = cardData;
            for (let i = 0; i < objs.length; i++) {
                const element = objs[i];
                element['isCrawling'] = true;
            }
            state.subjectCardData = objs;
        },
        
    }, //state를 동기적으로 수정할 때 사용
    //state를 바꿀때 바로 바꾸지말고 mutations를 통해 바꾸길 권장
    actions: {

    }, // 비동기를 사용할 때, 또는 여러 뮤테이션을 연달아 실행할 때
});
