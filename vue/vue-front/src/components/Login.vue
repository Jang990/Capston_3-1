<template>
  <v-app id="inspire">
      <v-content>
        <v-container fluid fill-height>
            <v-layout align-center justify-center>
              <v-flex xs12 sm8 md4>
                  <v-card class="elevation-12">
                    <v-toolbar dark color="primary">
                      <v-toolbar-title>Esummary 로그인??</v-toolbar-title>
                    </v-toolbar>
                    <v-progress-linear
                        color="deep-purple"
                        height="10"
                        :indeterminate="loading"
                        :active="loading"
                    ></v-progress-linear>
                    <v-card-text>
                        <v-form>
                          <v-text-field
                              v-model="id"
                              prepend-icon="mdi-account"
                              name="login"
                              label="Login"
                              type="text"
                          ></v-text-field>
                          <v-text-field
                              v-model="password"
                              id="password"
                              prepend-icon="mdi-lock"
                              name="password"
                              label="Password"
                              type="password"
                          ></v-text-field>
                        </v-form>
                    </v-card-text>
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="primary" to="/" @click="checkLog">Login</v-btn>
                    </v-card-actions>
                  </v-card>
              </v-flex>
            </v-layout>
        </v-container>
      </v-content>
  </v-app>
</template>

<script>
import {SET_INITIAL_DATA, CRAWL_SUBJECT, LOGIN_CHECK_AND_CALCULATE_TO_DO_NUMBER, LOAD_DB_SUBJECT} from '@/store/store'
import { mapState } from 'vuex';
import axios from "axios"
import * as authApi from '@/api/auth';

const api = axios.create({baseURL: 'http://localhost:8080'});


export default {
  name: 'Login',
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
    async checkLog(){
      /*
      로그인 과정
      1. 로그인 버튼 클릭
      2. 이러닝에서 기본 수업정보 가져오기 (여기까지 동기)
      3. DB에서 수업정보 가져오기 (여기부터 비동기)
      3. 전체 수업정보 크롤링 
      */
      this.loading = true; // 로딩바 동작
      let token = null; // 서버로부터 발급 받은 토큰

      // 로그인
      await authApi.login(this.id, this.password)
      .then((response) => {
        //전역에서 사용하도록 토큰 세팅이 필요함 - 나중에 vue 공부 다시 하고 진행
        token = response.data.token;
        console.log(`토큰 확인: ${token}`);
        

        // this.$emit('checkUser', response.data);
      })
      .catch((error) => {
          // 로그인 실패 시
          //주의하라 (response) => {} 이렇게 화살표 함수를 사용해야 this를 사용할때 원하는 값이 나온다. 스코프를 이해해라.
      });
      
      if(token === null) {
        // 서버로부터 받은 토큰이 없음
        console.log("받은 토큰 없음");
        this.loading = false;
        return; 
      }
      
      //크롤링
      await api.post('api/inhatc/login-info', {}, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}` //임시로 박아넣은 것이다. 수정해라
        },
        withCredentials: true,
        crossDomain: true, 
        credentials: "include",
      }) // 토큰확인하면서 연결까지 되도록 해놨다.
      .then((response) => {
        // 하지만 여기 response를 받아서 값을 세팅하는 부분은 안했다.
        this.$store.commit(
          SET_INITIAL_DATA, 
          {
            studentName: response.data.name, 
            studentNumber: response.data.studentNumber, 
            subjectCardData: response.data.subjectCardData
          }
        );
      })
      .catch(function (error) {
        // 로그인 실패시
        console.log("여기가 실행된다.");
        console.log(error);
      });
      // await api.get('/getInitSubject').then((response) => {
      //   //주의하라 (response) => {} 이렇게 화살표 함수를 사용해야 this를 사용할때 원하는 값이 나온다. 스코프를 이해해라.
      //   this.$store.commit(
      //     SET_INITIAL_DATA, 
      //     {
      //       studentName: response.data.name, 
      //       studentNumber: response.data.studentNumber, 
      //       subjectCardData: response.data.subjectCardData
      //     }
      //   );
      // })
      // .catch(function (error) {
      //   console.log(error);
      // });
      this.loading = false;
      this.$store.dispatch(LOGIN_CHECK_AND_CALCULATE_TO_DO_NUMBER);
      
      this.$store.dispatch(LOAD_DB_SUBJECT); //주석해제 함
      // this.$store.commit(SET_LOGIN_CHECK, true); //LOGIN_CHECK_AND_CALCULATE_TO_DO_NUMBER 여기서 설정해서 사라져도 됨
      this.$store.dispatch(CRAWL_SUBJECT);
      this.loading = false;
    }
  },
};
</script>

<style></style>