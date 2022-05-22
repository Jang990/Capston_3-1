<template>
  <v-app id="inspire">
      <v-content>
        <v-container fluid fill-height>
            <v-layout align-center justify-center>
              <v-flex xs12 sm8 md4>
                  <v-card class="elevation-12">
                    <v-toolbar dark color="primary">
                      <v-toolbar-title>Esummary 로그인</v-toolbar-title>
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
import {SET_LOGIN_CHECK, SET_INITIAL_DATA, CRAWL_SUBJECT} from '../store'
import { mapState } from 'vuex';
import axios from "axios"
const api = axios.create({baseURL: 'http://localhost:38080'});


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
      this.loading = true;
      let successLogin = false;
      await api.post('/vueLoginCheck', null, {params: {
        id: this.id,
        password: this.password
      }}).then((response) => {
        successLogin = response.data;
        // this.$emit('checkUser', response.data);
      });

      if(successLogin) {
        await api.get('/getInitSubject').then((response) => {
          //주의하라 (response) => {} 이렇게 화살표 함수를 사용해야 this를 사용할때 원하는 값이 나온다. 스코프를 이해해라.
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
          console.log(error);
        });
        this.loading = false;
        this.$store.commit(SET_LOGIN_CHECK, true);
        this.$store.dispatch(CRAWL_SUBJECT);
      }
      this.loading = false;
    }
  },
};
</script>

<style></style>