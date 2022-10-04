<template>
    <v-card class="elevation-12">
        <v-toolbar dark color="primary">
            <v-toolbar-title>Esummary 로그인</v-toolbar-title>
        </v-toolbar>
        <v-progress-linear color="deep-purple" height="10" :indeterminate="loading" :active="loading"></v-progress-linear>
        <v-card-text>
            <v-form>
                <v-text-field v-model="id" prepend-icon="mdi-account" name="login" label="Login" type="text"></v-text-field>
                <v-text-field v-model="password" id="password" prepend-icon="mdi-lock" name="password" label="Password"
                    type="password"></v-text-field>
            </v-form>
        </v-card-text>
        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="warning" to="/" @click="clickSignUp">Sign Up</v-btn>
            <v-btn color="primary" to="/" @click="checkLog">Login</v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
import {SET_INITIAL_DATA, CRAWL_SUBJECT, LOGIN_CHECK_AND_CALCULATE_TO_DO_NUMBER, LOAD_DB_SUBJECT} from '@/store/store'
import { mapState } from 'vuex';
import axios from "axios"
import * as authApi from '@/api/auth';
import * as crawlingApi from '@/api/crawling';
import { SET_LOGIN_FLAG } from '../../../store/store';

const api = axios.create({baseURL: 'http://localhost:8080'});


export default {
    name: 'LoginCard',
    data() {
        return {
            id: '',
            password: '',
            loading: false,
        };
    },
    props: {
        source: String,
    },
    computed: {
    },
    methods: {
        async checkLog() {
            /*
            로그인 과정
            1. 로그인 버튼 클릭 - 토큰 발급받기
            2. 메인페이지로 화면 전환 
            3. 이러닝에서 기본 수업정보 가져오기 (여기까지 동기)
            4. 사용자 정보 가져오기 - 닉네임 등등
            4. DB에서 수업 세부 정보 가져오기 (여기부터 비동기)
            5. 전체 수업정보 크롤링 
            */
            this.loading = true; // 부모 로딩바 동작

            // 1. 로그인
            const response = await authApi.login(this.id, this.password);
            if (response.status !== 200) {
                // 서버로부터 200을 받지 못함
                this.loading = false;
                return;
            }

            const token = this.$store.state.auth.token; // 토큰 가져오기 테스트를 위한 변수 - 삭제할 것

            if (token === null) {
                // 서버로부터 받은 토큰이 없음
                console.log("받은 토큰 없음");
                this.loading = false;
                return;
            }

            // 2. 기본 수업정보 크롤링
            await crawlingApi.basicSubjectList().status;

            this.loading = false;
            // this.$store.dispatch(LOGIN_CHECK_AND_CALCULATE_TO_DO_NUMBER);

            // this.$store.dispatch(LOAD_DB_SUBJECT); //주석해제 함
            // this.$store.commit(SET_LOGIN_CHECK, true); //LOGIN_CHECK_AND_CALCULATE_TO_DO_NUMBER 여기서 설정해서 사라져도 됨
            // this.$store.dispatch(CRAWL_SUBJECT);
        },
        clickSignUp() {
            this.$store.commit(SET_LOGIN_FLAG, false);
        },
    },
};
</script>