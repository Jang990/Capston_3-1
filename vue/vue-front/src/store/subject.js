export default {
    namespaced: true,
    state: {
        subjectCardData: [],
    },
    mutations: {
        setSubjectInitData(state, {subjectCardData: cardData /*studentName: name, studentNumber: number*/}) {
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
    },
    actions: {

    },
    getters: {

    },
}