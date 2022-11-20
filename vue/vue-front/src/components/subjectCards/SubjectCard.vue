<template>
  <v-container id="SubjectCard">
      <v-card
      max-width="650"
      id="subjectCard"
      class="mx-auto"
      >
        <!-- <v-img
          src="https://cdn.vuetifyjs.com/images/cards/sunshine.jpg"
          height="200px"
        ></v-img> -->

        <v-card-title>
          <!-- {{card.subjectName}} - {{card.owner}} -->
          {{subjectCardData[index].subjectName}} - {{subjectCardData[index].owner}}
        </v-card-title>
        <v-container>
            <v-row 
              justify="start"
              no-gutters
            >
              <v-col style="max-width: 145px;">
                <div style="padding-left: 4px;">
                  <div class="mx-4  text-subtitle-1 text-left">수업</div>
                  <v-row class="text-align" no-gutters>
                    <v-col class="font-weight-medium subtitle-1 
                      red--text text--darken-1">
                      <span>{{subjectCardData[index].cntIncompletedLecture}}</span>
                    </v-col>
                    <v-col class="font-weight-medium subtitle-1
                      green--text text--darken-1">
                      <span>{{subjectCardData[index].cntCompletedLecture}}</span>
                    </v-col>
                    <v-col class="font-weight-medium subtitle-1 
                      blue--text text--darken-1">
                      <span>{{subjectCardData[index].cntIncompletedLecture + subjectCardData[index].cntCompletedLecture}}</span>
                    </v-col>
                  </v-row>
                  <v-row class="text-align center" no-gutters style="cursor: default;">
                    <v-col class="text--disabled text-caption">
                      미수강
                    </v-col>
                    <v-col class="text--disabled text-caption">
                      수강
                    </v-col>
                    <v-col class="text--disabled text-caption">
                      합
                    </v-col>
                  </v-row>
                </div>
              </v-col>
              <v-col style="max-width: 16px;">
                <v-divider
                  vertical
                ></v-divider>
              </v-col>
              <v-col style="max-width: 145px;">
                <div>
                  <div class="mx-4  text-subtitle-1 text-left">과제</div>
                  <v-row class="text-align" no-gutters>
                    <v-col class="font-weight-medium subtitle-1 
                      red--text text--darken-1">
                      <span>{{subjectCardData[index].cntIncompletedTask}}</span>
                    </v-col>
                    <v-col class="font-weight-medium subtitle-1
                      green--text text--darken-1">
                      <span>{{subjectCardData[index].cntCompletedTask}}</span>
                    </v-col>
                    <v-col class="font-weight-medium subtitle-1 
                      blue--text text--darken-1">
                      <span>{{subjectCardData[index].cntIncompletedTask + subjectCardData[index].cntCompletedTask}}</span>
                    </v-col>
                  </v-row>
                  <v-row class="text-align center" no-gutters style="cursor: default;">
                    <v-col class="text--disabled text-caption">
                      미완료
                    </v-col>
                    <v-col class="text--disabled text-caption">
                      완료
                    </v-col>
                    <v-col class="text--disabled text-caption">
                      합
                    </v-col>
                  </v-row>
                </div>
              </v-col>
            </v-row>
          </v-container>
        <v-card-actions>
          <v-btn-toggle
            v-model="toggle_exclusive"
            color="primary"
            dense
            group
          >
            <v-btn
              text
              :value="1"
              @click="clickLecture"
            >
              강의 조회
            </v-btn>
            <v-btn
              text
              :value="2"
              @click="clickNotice"
            >
              공지 조회
            </v-btn>
            <v-btn
              :value="3"
              text
              @click="clickTask"
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
            채팅방
          </v-btn>
        </v-card-actions>

        <v-expand-transition>
          <div v-if="showLectureWeek">
            <v-divider></v-divider>
            <v-progress-linear
              color="deep-purple"
              height="10"
              :indeterminate="subjectCardData[index].isCrawling[0]"
              :active="subjectCardData[index].isCrawling[0]"
            ></v-progress-linear>
            <v-card-text 
              v-if="subjectCardData[index].isCrawling[0]"
            >
              강의 데이터를 조회중입니다...
            </v-card-text>
            <v-card-text 
              v-else-if="subjectCardData[index].lectures == null"
            >
              조회된 강의가 없습니다.
            </v-card-text>
            <lecture-table 
              v-else
              :lecturesData="subjectCardData[index].lectures"
            >
            </lecture-table>
            
          </div>
          <div v-else-if="showNotice">
            <v-divider></v-divider>
            <v-progress-linear
              color="deep-purple"
              height="10"
              :indeterminate="subjectCardData[index].isCrawling[1]"
              :active="subjectCardData[index].isCrawling[1]"
            ></v-progress-linear>
            <v-card-text 
              v-if="subjectCardData[index].isCrawling[1]"
            >
              공지 데이터를 조회중입니다...
            </v-card-text>
            <v-card-text 
              v-else-if="subjectCardData[index].notice == null"
            >
              조회된 공지가 없습니다.
            </v-card-text>
            <notice-table 
              v-else
              :noticeData="subjectCardData[index].notice"
            >
            </notice-table>
          </div>
          <div v-else-if="showTask">
            <v-divider></v-divider>
            <v-progress-linear
              color="deep-purple"
              height="10"
              :indeterminate="subjectCardData[index].isCrawling[2]"
              :active="subjectCardData[index].isCrawling[2]"
            ></v-progress-linear>
            <v-card-text 
              v-if="subjectCardData[index].isCrawling[2]"
            >
              과제 데이터를 조회중입니다...
            </v-card-text>
            <v-card-text 
              v-else-if="subjectCardData[index].task == null"
            >
              조회된 과제가 없습니다.
            </v-card-text>
            <task-table 
              v-else
              :taskData="subjectCardData[index].task"
            >
            </task-table>
          </div>
        </v-expand-transition>
      </v-card>
  </v-container>
</template>

<script>
import axios from "axios"
import NoticeTable from "./noticeTable/NoticeTable"
import TaskTable from "./taskTable/TaskTable"
import LectureTable from "./lectureTable/LectureTable"
import { mapState } from 'vuex';

import * as subjectApi from '@/api/subject';

const mainAxios = axios.create({baseURL: 'http://localhost:8080'});
const delayTime = 350;
let timeouts = [];
const lectureNum = 0;
const noticeNum = 1;
const taskNum = 2;

export default {
  name: 'SubjectCard',
  components: {LectureTable, NoticeTable, TaskTable},
  computed: {
    ...mapState({
      subjectCardData: state=> state.subjectCardData,
      studentNumber: state => state.studentNumber,
      // noticeData(state) {
      //   return state.subjectCardData[this.index].notice;
      // },
    }),
  },
  data() {
    return {
      show: false,
      showLectureWeek: false,
      showNotice: false,
      showTask: false,
      isLectureSearch: true,
      isNoticeSearch: true,
      isTaskSearch: true,
      noticeData: [],
      lectureWeekData: [],
      taskData: [],
    }
  },
  props:{
    index: Number,
  },
  methods: {
    searchLecture() {
      const lecture = new Promise((resolve, reject) => {
        mainAxios.post('/lectureDB', null, {params: {
          studentNumber: this.studentNumber,
          subjectId: this.subjectCardData[this.index].subjectId
        }}).then((response) => {
          // console.log(response);
          
          if(!response.data.length)
            resolve(null);
          else
            resolve(response.data);
        });
      });

      lecture.then(value => {
        this.lectureWeekData = value;
        this.isLectureSearch = false;
      });
    },
    searchNotice() {
      const notice = new Promise((resolve, reject) => {
        mainAxios.post('/noticeDB', null, {params: {
          subjectId: this.subjectCardData[this.index].subjectId
        }}).then((response) => {
          // console.log(response);

          if(!response.data.length) {
            resolve(null);
          }
          else {
            resolve(response.data);
          }
        });
      });

      notice.then(value => {
        this.noticeData = value;
        this.isNoticeSearch = false;
      });
    },
    searchTask() {
      let test1; 
      let aaab;
      const task = new Promise((resolve, reject) => {
        mainAxios.post('/taskDB', null, {params: {
          studentNumber: this.studentNumber,
          subjectId: this.subjectCardData[this.index].subjectId
        }}).then((response) => {
          // console.log(response);
          
          if(!response.data.length)
            resolve(null);
          else
            resolve(response.data);
        });
        test1 = setTimeout(()=>{ resolve(aaab); }, 4000);
      });
      
      task.then(value => {
        this.taskData = value;
        this.isTaskSearch = false;
        clearTimeout(test1);
      });
      
    },
    clickLecture() {
      if(this.showNotice || this.showTask) {
          timeouts[lectureNum] = setTimeout(()=>{
              this.showLectureWeek = !this.showLectureWeek; //일단 하나만 보여주고 나중에 과제랑 학습을 같이보여주거나 이렇게 만들자.
          }, delayTime);
          this.showNotice = false;
          this.showTask = false;
        }
        else {
          this.showLectureWeek = !this.showLectureWeek;
        }
    },
    clickNotice() {
      if(this.showLectureWeek || this.showTask) {
        timeouts[noticeNum] = setTimeout(()=>{
          this.showNotice = !this.showNotice;
        }, delayTime);
        this.showLectureWeek = false;
        this.showTask = false;
      }
      else {
        this.showNotice = !this.showNotice;
      }
    },
    clickTask() {
      if(this.showNotice || this.showLectureWeek) {
          timeouts[taskNum] = setTimeout(()=>{
            this.showTask = !this.showTask;
          }, delayTime);
          this.showNotice = false;
          this.showLectureWeek = false; 
      }
      else {
        this.showTask = !this.showTask;
      }
    },
  },
  destoryed() {
    clearTimeout(timeouts);
  },
  mounted() {
    subjectApi.getUserSubjectInfo(
        {
          cardIdx: this.index,
          studentId: this.$store.state.user.studentId, 
          subjectId: this.$store.state.subjectCardData[this.index].subjectId
        }
      );
  }
}
</script>

<style scoped>
.subtitle-1 > span{
  cursor: pointer;
  -ms-user-select: none; 
  -moz-user-select: -moz-none;
  -khtml-user-select: none;
  -webkit-user-select: none;
  user-select: none;
}
.text-caption, .text-subtitle-1 {
  cursor: default;
  -ms-user-select: none; 
  -moz-user-select: -moz-none;
  -khtml-user-select: none;
  -webkit-user-select: none;
  user-select: none;
}
</style>
