export default {
    namespaced: true,
    state: {
        success: false,
        text: '',
    },
    mutations: {
        // 회원가입 성공 팝업 띄우기
        showSuccess(state, {text}) {
            state.success = true;
            state.text = text;
        },
        closedSignup(state) {
            state.success = false;
        }
    },
    actions: {

    },
    getters: {

    },
}