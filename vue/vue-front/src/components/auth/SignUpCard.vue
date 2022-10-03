<template>
    <v-card class="elevation-12">
        <v-toolbar dark color="primary">
            <v-toolbar-title>
                <v-btn
                    color="white"
                    icon
                    @click="clickLogin()"
                >
                    <v-icon>mdi-arrow-u-right-top-bold</v-icon>
                </v-btn>
                Esummary 회원가입
            </v-toolbar-title>
        </v-toolbar>
        <v-progress-linear color="deep-purple" height="10" :indeterminate="loading" :active="loading"></v-progress-linear>
        <v-form ref="form" v-model="valid" lazy-validation>
            <v-text-field v-model="studentId" :rules="studentIdRules" label="이러닝 ID" required></v-text-field>
        
            <v-text-field v-model="password" :rules="passwordRules" label="이러닝 Password" required></v-text-field>

            <v-text-field v-model="nickname" :rules="nicknameRules" label="닉네임" required></v-text-field>
        
            <v-checkbox v-model="checkbox" :rules="[v => !!v || '연동을 체크해주세요!']" label="이러닝 연동" required>
            </v-checkbox>
        
            <v-btn :disabled="!valid" color="success" class="mr-4" @click="validate">
                Validate
            </v-btn>
        
            <v-btn color="error" class="mr-4" @click="reset">
                Reset Form
            </v-btn>
        
            <v-btn color="warning" @click="resetValidation">
                Reset Validation
            </v-btn>
        </v-form>
    </v-card>
</template>

<script>
import {SET_LOGIN_FLAG} from '@/store/store'
import { mapState } from 'vuex';
import axios from "axios"
import * as authApi from '@/api/auth';


export default {
    name: 'SignUpCard',
    data() {
        return {
            loading: false,

            valid: true,
            studentId: '',
            studentIdRules: [
                v => !!v || 'ID는 필수 입력사항입니다.',
                v => (v && v.length >= 5) || 'ID는 5자 이상 입력해주세요',
            ],
            password: '',
            passwordRules: [
                v => !!v || 'Password는 필수 입력사항입니다.',
                v => (v && v.length >= 5) || 'Password는 5자 이상 입력해주세요',
                //
            ],
            nickname: '',
            nicknameRules: [
                v => !!v || 'Nickname은 필수 입력사항입니다.',
                v => (v && v.length >= 3 && v.length <= 10) || 'Nickname은 3자 이상 10자 이하로 입력해주세요',
            ],
            checkbox: false,
        };
    },
    computed: {
    },
    methods: {
        clickLogin() {
            this.$store.commit(SET_LOGIN_FLAG, true); //로그인페이지로 전환
        },
        async validate() {
            this.$refs.form.validate()
            this.loading = true;

            // 회원가입
            const response = await authApi.signup(this.studentId, this.password, this.nickname);

            if(response === undefined) {
                //회원가입 실패를 알려주는 로직 추가하기
                this.loading = false;
                resetValidation();
                return;
            }
            
            //회원가입 성공 팝업?을 보여주는 로직 추가하기
            this.$store.commit(SET_LOGIN_FLAG, false);
            loading = false;
        },
        reset() {
            this.$refs.form.reset()
        },
        resetValidation() {
            this.$refs.form.resetValidation()
        },
    },
};
</script>