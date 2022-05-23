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
            요약 조회<v-icon> {{ show ? 'mdi-chevron-up' : 'mdi-chevron-down' }}</v-icon>
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
            <v-card-text v-else>
              강의 주차 내용에 대한 것들...
            </v-card-text>
            <!-- 
            <task-table 
              v-else
              :lecturesData="subjectCardData[index].lectures"
            >
            </task-table>
            -->
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
import { mapState } from 'vuex';
const mainAxios = axios.create({baseURL: 'http://localhost:38080'});
const delayTime = 350;
let timeouts = [];
const lectureNum = 0;
const noticeNum = 1;
const taskNum = 2;

export default {
  name: 'SubjectCard',
  components: {NoticeTable, TaskTable},
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
  created() {
    // this.searchNotice();
    // this.searchLecture();
    // this.searchTask();
  }
}
</script>

<style>
</style>
