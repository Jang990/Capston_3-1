<template>
  <div id="app">
    <v-app>
      <template v-if="!loginCheck">
        <!-- <login-component @checkUser="checkUser"></login-component> -->
        <login-component></login-component>
      </template>
      <!--라우터 없이 만들음. 라우터 공부하고 템플릿없애고 다시 만들기.-->
      <template v-else>
      <div>
        <h1 left>{{studentName}}님 반갑습니다.</h1>
        <h1 left>학번: {{studentNumber}}</h1>
      </div>

      <template v-if="subjectCardData.length > 0">
        <!-- <subject-card v-for="(card, i) in subjectCardData" 
          :key="i" v-bind:card="card">
        </subject-card> -->
        <v-container>
          <v-row dense>
            <v-col
              v-for="(card, i) in subjectCardData"
              :key="i"
              cols="12"
              class="mx-auto"
            >
              <subject-card v-bind:card="card" v-bind:studentNumber="studentNumber"></subject-card>
            </v-col>
          </v-row>
        </v-container>
      </template>
      <div v-else>조회된 과목이 없습니다!</div>
    </template> 
    </v-app>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import store from './store';
import axios from "axios";
import SubjectCard from "./components/subjectCards/SubjectCard";
import LoginComponent from "./components/Login";

export default {
  name: 'App',
  store,
  components: {SubjectCard, LoginComponent,},
  computed: {
    ...mapState(['loginCheck', 'studentName', 'studentNumber', 'subjectCardData']),
  },
  data() {
    return {
      // loginCheck: false,
      // studentName: '',
      // studentNumber: '',
      // subjectCardData: [],
    }
  },
  methods: {
  },
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
  margin-bottom: 60px;
}
</style>
