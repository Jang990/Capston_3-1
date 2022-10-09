export default {
    namespaced: true,
    state: {
        studentId: '',
        nickname: '',
        createdDate: '',
    },
    mutations: {
        setUserInfo(state, {studentId, nickname, createdDate}) {
            state.nickname = nickname;
            state.studentId = studentId;
            state.createdDate = createdDate;
        },
        setStudentId(state, studentId) {
            state.studentId = studentId;
        }
    },
    actions: {

    },
    getters: {

    },
}