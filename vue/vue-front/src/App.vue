<template>
  <div id="app">
    <h1>안녕하세요!</h1>
    <div>
      <h1>{{studentName}}님 반갑습니다.</h1>
      <h1>학번: {{studentNumber}}</h1>
    </div>
    <!-- axios 테스트를 위한 버튼-->
    <button @click="dbSearch">201845096 과목 검색</button>
    <template v-if="subjectName.length > 0">
      <subject-card v-for="name in subjectName" :key="name" v-bind:title="name"></subject-card>
    </template>
    <div v-else>조회된 과목이 없습니다!</div>
  </div>
</template>

<script>
import axios from "axios"
import SubjectCard from "./SubjectCard";

export default {
  name: 'App',
  components: {SubjectCard,},
  data() {
    return {
      studentName: '',
      studentNumber: '',
      subjectName: [],
    }
  },
  methods: {
    dbSearch() {
      axios.get('http://localhost:38080/vueDB')
        .then((response) => {
          //주의하라 (response) => {} 이렇게 화살표 함수를 사용해야 this를 사용할때 원하는 값이 나온다. 스코프를 이해해라.
          console.log(response); 
          this.studentName = response.data.name;
          this.studentNumber = response.data.studentNumber;
          this.subjectName = response.data.subjectName;
        })
        .catch(function (error) {
          // handle error
          console.log(error);
        })
        .then(function () {
          // always executed
        });
    }
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
