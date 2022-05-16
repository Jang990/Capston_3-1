<template>
  <div id="app">
    <template v-if="!loginCheck">
      <login-component @checkUser="checkUser"></login-component>
    </template>
    <!--라우터 없이 만들음. 라우터 공부하고 템플릿없애고 다시 만들기.-->
    <template v-else>
    <div>
      <h1 left>{{studentName}}님 반갑습니다.</h1>
      <h1 left>학번: {{studentNumber}}</h1>
    </div>

    <template v-if="subjectCardData.length > 0" center>
      <!-- <subject-card v-for="(card, i) in subjectCardData" 
        :key="i" v-bind:card="card">
      </subject-card> -->
      <v-container>
        <v-row dense>
          <v-col
            v-for="(card, i) in subjectCardData"
            :key="i"
            cols="12"
          >
            <subject-card v-bind:card="card"></subject-card>
          </v-col>
        </v-row>
      </v-container>
    </template>
    <div v-else>조회된 과목이 없습니다!</div>
  </template> 
  </div>
</template>

<script>
import axios from "axios"
import SubjectCard from "./SubjectCard";
import LoginComponent from "./Login";
const mainAxios = axios.create({baseURL: 'http://localhost:38080'});

export default {
  name: 'App',
  components: {SubjectCard, LoginComponent,},
  data() {
    return {
      loginCheck: false,
      studentName: '',
      studentNumber: '',
      subjectCardData: [],
    }
  },
  methods: {
    checkUser(check) {
      this.loginCheck = check;
      if(check) {
        mainAxios.get('/getInitSubject').then((response) => {
          //주의하라 (response) => {} 이렇게 화살표 함수를 사용해야 this를 사용할때 원하는 값이 나온다. 스코프를 이해해라.
          this.studentName = response.data.name;
          this.studentNumber = response.data.studentNumber;
          this.subjectCardData = response.data.subjectCardData;
        })
        .catch(function (error) {
          console.log(error);
        });
      }
    },
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
}
</style>
