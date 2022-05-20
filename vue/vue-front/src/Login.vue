<template>
  <v-app id="inspire">
      <v-content>
        <v-container fluid fill-height>
            <v-layout align-center justify-center>
              <v-flex xs12 sm8 md4>
                  <v-card class="elevation-12">
                    <v-toolbar dark color="primary">
                        <v-toolbar-title>ESummary 로그인</v-toolbar-title>
                    </v-toolbar>
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
import {SET_LOGIN_CHECK, SET_INITIAL_DATA} from './store'
import { mapState } from 'vuex';
import axios from "axios"
const api = axios.create({baseURL: 'http://localhost:38080'});


export default {
  name: 'Login',
  data() {
    return {
      id: '',
      password: '',
    };
  },
  props: {
      source: String,
  },
  computed: {
  },
  methods: {
    async checkLog(){
      await api.post('/vueLoginCheck', null, {params: {
        id: this.id,
        password: this.password
      }}).then((response) => {
        this.$store.commit(SET_LOGIN_CHECK, response.data);
        // this.$emit('checkUser', response.data);
      });

      if(this.$store.state.loginCheck) {
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
      }
    }
  },
};
</script>

<style></style>