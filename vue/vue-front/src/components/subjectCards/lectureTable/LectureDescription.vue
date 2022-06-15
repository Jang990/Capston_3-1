<template>
  <v-data-table
      :headers="headers"
      :items="lectureArray"
      item-key="lectureId"
      :item-class="submitCheck"
      class="elevation-1"
      id="table-width-working-id"
      hide-default-footer=false	
    >
      <template v-slot:[`item.learningState`]="{ item }">
        <v-hover
          v-slot="{ hover }"
          open-delay="200"
        >
          <v-progress-linear
            v-if="item.status == '1'"
            color="light-blue"
            height="18"
            striped
            :value="StudyingPercentage(item.learningTime, item.fullTime)"
          >
            <strong v-if="!hover">{{ remainingTime(item.learningTime, item.fullTime, item.status) }}</strong>
            <strong v-else>{{ item.learningTime +'/'+ item.fullTime }}</strong>
          </v-progress-linear>
          <v-progress-linear
            v-else-if="item.learningTime == '0분'"
            color="blue-grey"
            height="18"
            :value="StudyingPercentage(item.learningTime, item.fullTime)"
          >
            <strong v-if="!hover">{{ remainingTime(item.learningTime, item.fullTime, item.status) }}</strong>
            <strong v-else>{{ item.learningTime +'/'+ item.fullTime }}</strong>
          </v-progress-linear>
          <v-progress-linear
            v-else-if="item.type != '온라인'"
            color="blue-grey"
            height="18"
            :value="StudyingPercentage(item.learningTime, item.fullTime)"
          >
            <strong>Empty</strong>
          </v-progress-linear>
          <v-progress-linear
            v-else
            color="amber"
            height="18"
            :value="StudyingPercentage(item.learningTime, item.fullTime)"
          >
            <strong v-if="!hover">{{ remainingTime(item.learningTime, item.fullTime, item.status) }}</strong>
            <strong v-else>{{ item.learningTime +'/'+ item.fullTime }}</strong>
          </v-progress-linear>
        </v-hover>
      </template>
      <template v-slot:[`item.status`]="{ item }" >
        <v-icon v-if="item.status == '1'"  color="green accent-3">mdi-check-bold</v-icon>
        <v-icon v-else color="red darken-1">mdi-video-off-outline</v-icon>
      </template>
      <template v-slot:[`item.lectureVideoId`]="{ item }">
          <v-btn
            rounded
            color="primary"
            dark
            href="https://cyber.inhatc.ac.kr/"
            target="_blank"
          >
            학습하기{{item.status}}
          </v-btn>
          <!--  -->
      </template>
    </v-data-table>
</template>

<script>
export default {
  name: 'LectureDescription',
  data() {
    return {
      LectureTitle: '강의 목록',
      expanded: [],
      singleExpand: true,
      headers: [
          { text: ' ', value: 'studyYN', width: '5%', class: "blue lighten-5" },//제출여부
          { text: ' ', value: 'idx', align: 'center', width: '5%', class: "blue lighten-5" },
          {
            text: '제목',
            align: 'center',
            sortable: false,
            value: 'title',
            width: '40%',
            class: "blue lighten-5",
            //primary
          },
          { text: '잔여시간', value: 'learningState', width: '35%', class: "blue lighten-5"},
          { text: '학습하기', value: 'lectureVideoId', width:'15%', class: "blue lighten-5" },
          
          // { text: '내용', value: 'description' },
      ],
    }
  },
  computed: {
    
  },
  props:{
    lectureArray: Object,
  },
  methods: {
    StudyingPercentage(learningTime, fullTime) {
      //이거 컴퓨티드로 바꾸고 싶은데 type에러 뜨면서 안된다고 한다.
      const numFullTime = Number(fullTime.slice(0, fullTime.indexOf('분')));
      if(numFullTime == 0) return 100;

      const numStudyingTime = Number(learningTime.slice(0, learningTime.indexOf('분')));
      if(numStudyingTime > numFullTime) return 100;
      const percent = numStudyingTime / numFullTime * 100;
      return Math.ceil(percent);
    },
    remainingTime(learningTime, fullTime, statusLecture){
      const studyTime = learningTime.split(' ');
      const fullTimeMinute = (fullTime.indexOf('분') == -1) ? 0 : Number(fullTime.replace('분', ''));
      const studyTimeMinute = (studyTime[0].indexOf('분') == -1) ? 0 : Number(studyTime[0].replace('분', ''));

      const fullTimeSecond = fullTimeMinute * 60;
      const studyTimeSecond = ((studyTime[0].indexOf('초') == -1) ? 0 : Number(studyTime[1].replace('초', ''))) + studyTimeMinute * 60;
      if(studyTimeSecond >= fullTimeSecond) {
        if(statusLecture == '1' ) return '수강 완료';
        else return '기타 사항 미흡';
      };

      let remainingTimeSecond = fullTimeSecond - studyTimeSecond;
      if(remainingTimeSecond < 60) return remainingTimeSecond+'초';
      
      const remainingTimeMinute = Math.floor(remainingTimeSecond / 60);
      remainingTimeSecond -= remainingTimeMinute * 60;
      return remainingTimeMinute+'분 ' + remainingTimeSecond +'초'
    }
  },
}
</script>

<style scoped>
  
</style>
