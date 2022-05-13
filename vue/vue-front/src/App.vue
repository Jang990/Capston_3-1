<template>
  <div id="app">
    <h1>안녕하세요!</h1>
    <div>
      <h1>{{user.name}}님 반갑습니다.</h1>
      <h1>학번: {{user.id}}</h1>
    </div>
    <div>
      <!-- <table border="1" th:each="subject : ${subjectList}" th:if="${#lists.size(subjectList) > 0}"> -->
      <template v-if="subjectList.length > 0">
        <table border="1" v-for="subject in subjectList" v-bind:key="subject" >
          <tr>
            <!-- <td th:text="${subject.subjectName}" colspan="3">{{subject.subjectName}}</td> -->
            <td colspan="3">{{subject.subjectName}}</td>
          </tr>
          <template v-if="subject.lectureList.length > 0">
            <div v-bind:key="lectures" v-for="lectures in subject.lectureList">
              <tr>
                <td class="idx">학습주차</td>
                <!-- <td th:text="${lectures.title}" colspan="2"></td> -->
                <td colspan="2">{{lectures.title}}</td>
              </tr>
              <!-- <tr th:each="lecture : ${lectures.lectures}" th:if="${#lists.size(lectures.lectures) > 0}"> -->
              <template v-if="lectures.lectures.length > 0">
                <tr v-for="lecture in lectures.lectures" v-bind:key="lecture">
                  <td></td>
                  <td class="idx">학습</td>
                  <!-- <td th:text="${lecture.title}" colspan="2"></td> -->
                  <td colspan="2">{{lecture.title}}</td>
                </tr>
              </template>
            </div>
          </template>
          <!-- <tr th:unless="${#lists.size(subject.lectureList) > 0}"> -->
          <template v-else>
            <tr>
              <td colspan="3">조회된 학습이 없습니다.</td>
            </tr>
          </template>
          <tr>
            <td colspan="3">구분========================</td>
          </tr>
          
          <!-- 
           여기는 오류 안잡음
          -->
          <!-- <tr th:each="task : ${subject.taskList}" th:if="${#lists.size(subject.taskList) > 0}"> -->
          <template v-if="subject.taskList.length > 0">
            <!-- <tr th:each="task : ${subject.taskList}" th:if=""> -->
            <tr v-for="task in subject.taskList" v-bind:key="task">
              <td class="idx">과제</td>
              <!-- <td th:text="${task.title}" colspan="2"></td> -->
              <td colspan="2">{{task.title}}</td>
            </tr>
          </template>
          <!-- <tr th:unless="${#lists.size(subject.taskList) > 0}"> -->
          <template v-else>
            <tr>
              <td colspan="3">조회된 과제가 없습니다.</td>
            </tr>
          </template>
          <tr>
            <td colspan="3">구분========================</td>
          </tr>
          <!-- <tr th:each="notice: ${subject.noticeList}" th:if="${#lists.size(subject.noticeList) > 0}"> -->
          <template v-if="subject.noticeList.length > 0">
            <!-- <tr th:each="notice: ${subject.noticeList}"> -->
            <tr v-for="notice in subject.noticeList" v-bind:key="notice">
              <td class="idx">공지</td>
              <!-- <td th:text="${notice.title}" colspan="2"></td> -->
              <td colspan="2">{{notice.title}}</td>
            </tr>
          </template>
          <!-- <tr th:unless="${#lists.size(subject.noticeList) > 0}"> -->
          <template v-else>
            <tr>
              <td colspan="3">조회된 공지가 없습니다.</td>
            </tr>
          </template>
        </table>
      </template>
      <template v-else>
        <h2>조회된 과목이 없습니다.</h2>
      </template>
    </div>

    <!-- axios 테스트를 위한 버튼-->
    <button @click="dbSearchUser">201845096 사용자 검색</button>
    <button @click="dbSearch">201845096 과목 검색</button>
  </div>
</template>

<script>
import axios from "axios"

export default {
  name: 'App',
  data() {
    return {
      user: {
        name: '장현우',
        id: '201845096',
      },
      subjectList: [
        { 
          subjectName: '캡스톤디자인',
          lectureList: [
            { 
              title: '1주차',
              lectures: [ 
                { title: '1차시'},
                { title: '2차시'}
              ]
            }
          ],
          taskList: [
            { 
              title: '1주차 과제',
            }
          ],
          noticeList: [
            { 
              title: '캡스톤 디자인 수업 시간 및 운영 방법 변경',
            }
          ]
        }
      ]
    }
  },
  methods: {
    dbSearchUser() {
      axios.get('http://localhost:38080/vueDBu')
        .then(function (response) {
          // handle success
          console.log(response);
        })
        .catch(function (error) {
          // handle error
          console.log(error);
        })
        .then(function () {
          // always executed
        });
    },
    dbSearch() {
      axios.get('http://localhost:38080/vueDB')
        .then(function (response) {
          // handle success
          console.log(response);
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
