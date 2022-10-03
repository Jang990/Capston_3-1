<template>
  <v-app>
        <v-dialog v-model="dialog" persistent max-width="600px" min-width="360px">
            <div>
                <v-tabs v-model="tab" show-arrows background-color="deep-purple accent-4" icons-and-text dark grow>
                    <v-tabs-slider color="purple darken-4"></v-tabs-slider>
                    <v-tab v-for="i in tabs" :key="i">
                        <v-icon large>{{ i.icon }}</v-icon>
                        <div class="caption py-1">{{ i.name }}</div>
                    </v-tab>
                    <v-tab-item>
                        <v-card class="px-4">
                            <v-card-text>
                                <v-form ref="loginForm" v-model="valid" lazy-validation>
                                    <v-row>
                                        <v-col cols="12">
                                            <v-text-field v-model="loginEmail" :rules="loginEmailRules" label="E-mail" required></v-text-field>
                                        </v-col>
                                        <v-col cols="12">
                                            <v-text-field v-model="loginPassword" :append-icon="show1?'eye':'eye-off'" :rules="[rules.required, rules.min]" :type="show1 ? 'text' : 'password'" name="input-10-1" label="Password" hint="At least 8 characters" counter @click:append="show1 = !show1"></v-text-field>
                                        </v-col>
                                        <v-col class="d-flex" cols="12" sm="6" xsm="12">
                                        </v-col>
                                        <v-spacer></v-spacer>
                                        <v-col class="d-flex" cols="12" sm="3" xsm="12" align-end>
                                            <v-btn x-large block :disabled="!valid" color="success" @click="validate"> Login </v-btn>
                                        </v-col>
                                    </v-row>
                                </v-form>
                            </v-card-text>
                        </v-card>
                    </v-tab-item>
                    <v-tab-item>
                        <v-card class="px-4">
                            <v-card-text>
                                <v-form ref="registerForm" v-model="valid" lazy-validation>
                                    <v-row>
                                        <v-col cols="12" sm="6" md="6">
                                            <v-text-field v-model="firstName" :rules="[rules.required]" label="First Name" maxlength="20" required></v-text-field>
                                        </v-col>
                                        <v-col cols="12" sm="6" md="6">
                                            <v-text-field v-model="lastName" :rules="[rules.required]" label="Last Name" maxlength="20" required></v-text-field>
                                        </v-col>
                                        <v-col cols="12">
                                            <v-text-field v-model="email" :rules="emailRules" label="E-mail" required></v-text-field>
                                        </v-col>
                                        <v-col cols="12">
                                            <v-text-field v-model="password" :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'" :rules="[rules.required, rules.min]" :type="show1 ? 'text' : 'password'" name="input-10-1" label="Password" hint="At least 8 characters" counter @click:append="show1 = !show1"></v-text-field>
                                        </v-col>
                                        <v-col cols="12">
                                            <v-text-field block v-model="verify" :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'" :rules="[rules.required, passwordMatch]" :type="show1 ? 'text' : 'password'" name="input-10-1" label="Confirm Password" counter @click:append="show1 = !show1"></v-text-field>
                                        </v-col>
                                        <v-spacer></v-spacer>
                                        <v-col class="d-flex ml-auto" cols="12" sm="3" xsm="12">
                                            <v-btn x-large block :disabled="!valid" color="success" @click="validate">Register</v-btn>
                                        </v-col>
                                    </v-row>
                                </v-form>
                            </v-card-text>
                        </v-card>
                    </v-tab-item>
                </v-tabs>
            </div>
        </v-dialog>
    </v-app>
</template>

<script>
import {SET_INITIAL_DATA, CRAWL_SUBJECT, LOGIN_CHECK_AND_CALCULATE_TO_DO_NUMBER, LOAD_DB_SUBJECT} from '@/store/store'
import { mapState } from 'vuex';
import axios from "axios"
import * as authApi from '@/api/auth';
import * as crawlingApi from '@/api/crawling';

const api = axios.create({baseURL: 'http://localhost:8080'});


export default {
  name: 'Login',
  data() {
    return {
      id: '',
      password: '',
      loading: false,

      // 
      dialog: true,
      tab: 0,
      tabs: [
          {name:"Login", icon:"mdi-account"},
          {name:"Register", icon:"mdi-account-outline"}
      ],
      valid: true,
      
      firstName: "",
      lastName: "",
      email: "",
      password: "",
      verify: "",
      loginPassword: "",
      loginEmail: "",
      loginEmailRules: [
        v => !!v || "Required",
        v => /.+@.+\..+/.test(v) || "E-mail must be valid"
      ],
      emailRules: [
        v => !!v || "Required",
        v => /.+@.+\..+/.test(v) || "E-mail must be valid"
      ],

      show1: false,
      rules: {
        required: value => !!value || "Required.",
        min: v => (v && v.length >= 8) || "Min 8 characters"
      }
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
      1. 로그인 버튼 클릭 - 토큰 발급받기
      2. 메인페이지로 화면 전환 
      3. 이러닝에서 기본 수업정보 가져오기 (여기까지 동기)
      4. 사용자 정보 가져오기 - 닉네임 등등
      4. DB에서 수업 세부 정보 가져오기 (여기부터 비동기)
      5. 전체 수업정보 크롤링 
      */
      this.loading = true; // 로딩바 동작

      // 1. 로그인
      const response = await authApi.login(this.id, this.password);
      if(response.status !== 200) {
        // 서버로부터 200을 받지 못함
        this.loading = false;
        return;
      }

      const token = this.$store.state.auth.token; // 토큰 가져오기 테스트를 위한 변수 - 삭제할 것
      
      if(token === null) {
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
    }
  },
};
</script>

<style></style>