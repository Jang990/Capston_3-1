<template>
  <v-card
      class="mx-auto"
      max-width="800"
      id="subjectCard"
    >
      <!-- <v-img
        src="https://cdn.vuetifyjs.com/images/cards/sunshine.jpg"
        height="200px"
      ></v-img> -->

      <v-card-title>
        {{card.subjectName}} - {{card.owner}}
      </v-card-title>

      <!-- <v-card-subtitle>
        {{card.subjectName}} - {{card.owner}}
      </v-card-subtitle> -->

      <v-card-actions>
        
        <v-btn
        color="success"
        text
        @click="searchLecture"
        >
          강의 조회
          <v-icon>
              $expand
          </v-icon>
        </v-btn>
        <v-btn
        color="warning"
        text
        @click="searchNotice"
        >
          공지 조회
          <v-icon>
              $expand
          </v-icon>
        </v-btn>
        <v-btn
        color="error"
        text
        @click="searchTask"
        >
          과제 조회
          <v-icon>
              $expand
          </v-icon>
        </v-btn>
        <!-- color="orange lighten-2" -->
        <v-spacer></v-spacer> 
        <!-- spacer뒤에 btn을 하면 오른쪽아래 아이콘처럼 작게 붙힌다. -->
        <v-btn
          text
          @click="show = !show"
        >
          요약 조회<v-icon> {{ show ? 'mdi-chevron-up' : 'mdi-chevron-down' }}</v-icon>
        </v-btn>
      </v-card-actions>

      <v-expand-transition>
        <div v-if="showLectureWeek">
          <v-divider></v-divider>
          <v-card-text>
            강의 주차 내용에 대한 것들...
          </v-card-text>
        </div>
        <div v-else-if="showNotice">
          <v-divider></v-divider>

          <v-card-text>
            공지 내용에 대한 것들...
          </v-card-text>
        </div>
        <div v-else-if="showTask">
          <v-divider></v-divider>

          <v-card-text>
            과제 내용에 대한 것들...
          </v-card-text>
        </div>
        <!-- <div v-else-if="show">
          <v-divider></v-divider>

          <v-card-text>
            I'm a thing. But, like most politicians, he promised more than he could deliver. You won't have time for sleeping, soldier, not with all the bed making you'll be doing. Then we'll go with that data file! Hey, you add a one and two zeros to that or we walk! You're going to do his laundry? I've got to find a way to escape.
          </v-card-text>
        </div> -->
      </v-expand-transition>
    </v-card>
</template>

<script>
import axios from "axios"
const mainAxios = axios.create({baseURL: 'http://localhost:38080'});
const delayFlag = 350;
let timeouts = [];

export default {
  name: 'SubjectCard',
  data() {
    return {
      show: false,
      showLectureWeek: false,
      showNotice: false,
      showTask: false,
    }
  },
  props:{
    card: Object,
    studentNumber: String,
  },
  methods: {
    searchLecture() {
      mainAxios.post('/lectureDB', null, {params: {
        studentNumber: this.studentNumber,
        subjectId: this.card.subjectId
      }}).then((response) => {
        console.log(response);

        if(this.showNotice || this.showTask) {
          timeouts[0] = setTimeout(()=>{
              this.showLectureWeek = !this.showLectureWeek; //일단 하나만 보여주고 나중에 과제랑 학습을 같이보여주거나 이렇게 만들자.
          }, delayFlag);
          this.showNotice = false;
          this.showTask = false;
        }
        else {
          this.showLectureWeek = !this.showLectureWeek;
        }
      });
    },
    searchNotice() {
      mainAxios.post('/noticeDB', null, {params: {
        subjectId: this.card.subjectId
      }}).then((response) => {
        console.log(response);

        if(this.showLectureWeek || this.showTask) {
          timeouts[1] = setTimeout(()=>{
            this.showNotice = !this.showNotice;
          }, delayFlag);
          this.showLectureWeek = false;
          this.showTask = false;
        }
        else {
          this.showNotice = !this.showNotice;
        }
      });
    },
    searchTask() {
      mainAxios.post('/taskDB', null, {params: {
        studentNumber: this.studentNumber,
        subjectId: this.card.subjectId
      }}).then((response) => {
        console.log(response);
        
        if(this.showNotice || this.showLectureWeek) {
          timeouts[2] = setTimeout(()=>{
            this.showTask = !this.showTask;
          }, delayFlag);
          this.showNotice = false;
          this.showLectureWeek = false; 
        }
        else {
          this.showTask = !this.showTask;
        }
      });
    },
  },
  destoryed() {
    clearTimeout(timeouts);
  },
}
</script>

<style>
/* #subjectCard{
  border: 1px solid green;
  width: 200px;
  height: 50px;
  border-radius: 7px;
  margin-left: 20%;

  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 10px;
} */
</style>
