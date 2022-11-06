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
            <v-btn color="primary" to="/" @click="clickTest">Test</v-btn>
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
import * as subjectApi from '@/api/subject';
import { SET_LOGIN_FLAG } from '../../../store/store';

import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'

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

            // 기본 수업정보 크롤링
            const crawlResponse = await crawlingApi.basicSubjectList();
            
            // 만약 크롤링 정보가 없다면 DB에서 가져오기
            // 수정이 필요하다. Error코드를 받아서 처리하는게 좋을 것 같다.
            // 지금은 일단 테스트를 위해 데이터 길이가 0이면 DB에 데이터를 가져온다.
            if(crawlResponse.data.subjectCardData.length === 0) {
                await subjectApi.basicSubjectList({studentId: this.id});
            }

            // 수업 세부 정보 크롤링 - 비동기
            this.$store.dispatch(CRAWL_SUBJECT);
            
            this.loading = false;
            // this.$store.dispatch(LOGIN_CHECK_AND_CALCULATE_TO_DO_NUMBER);
            
            // this.$store.dispatch(LOAD_DB_SUBJECT); //주석해제 함
            // await this.$store.dispatch(CRAWL_SUBJECT);
        },
        clickSignUp() {
            this.$store.commit(SET_LOGIN_FLAG, false);
        },
        clickTest() {
            console.log("테스트");
            const serverURL = "http://localhost:8080/ws/chat"
            let socket = new SockJS(serverURL);
            this.stompClient = Stomp.over(socket);
            console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`)
            this.stompClient.connect(
                {},
                frame => {
                // 소켓 연결 성공
                this.connected = true;
                console.log('소켓 연결 성공', frame);
                // 서버의 메시지 전송 endpoint를 구독합니다.
                // 이런형태를 pub sub 구조라고 합니다.
                this.stompClient.subscribe("/send", res => {
                    console.log('구독으로 받은 메시지 입니다.', res.body);

                    // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
                    this.recvList.push(JSON.parse(res.body))
                });
                },
                error => {
                // 소켓 연결 실패
                console.log('소켓 연결 실패', error);
                this.connected = false;
                }
            );      
        },
    },
};
</script>