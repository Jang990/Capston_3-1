<template>
  <v-container id="SubjectCard">
      <v-card
      max-width="800"
      id="subjectCard"
      class="mx-auto"
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

        <v-card-actions :single-expand="singleExpand">
          <v-btn-toggle
            v-model="toggle_exclusive"
            color="primary"
            dense
            group
          >
            <v-btn
              text
              @click="searchLecture"
              :value="1"
            >
              강의 조회
            </v-btn>
            <v-btn
              text
              :value="2"
              @click="searchNotice"
            >
              공지 조회
            </v-btn>
            <v-btn
              :value="3"
              text
              @click="searchTask"
            >
              과제 조회
            </v-btn>
          </v-btn-toggle>
          
          
          
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
            
            <v-card-text 
              v-if="noticeData == null"
            >
              조회된 공지 데이터가 없습니다.
            </v-card-text>
            <notice-table 
              v-else
              :noticeData="noticeData"
            >
            </notice-table>
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
  </v-container>
</template>

<script>
import axios from "axios"
import NoticeTable from "./NoticeTable"
const mainAxios = axios.create({baseURL: 'http://localhost:38080'});
const delayFlag = 350;
let timeouts = [];

export default {
  name: 'SubjectCard',
  components: {NoticeTable,},
  data() {
    return {
      show: false,
      showLectureWeek: false,
      showNotice: false,
      showTask: false,
      noticeData: [],
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
        if(!response.data.length)
          this.noticeData = null;
        else
          this.noticeData = response.data;
          
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
</style>
