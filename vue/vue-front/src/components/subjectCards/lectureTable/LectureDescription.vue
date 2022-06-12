<template>
  <v-data-table
      :headers="headers"
      :items="lectureArray"
      :items-per-page="5"
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
            color="light-blue"
            height="18"
            :value="StudyingPercentage(item.learningTime, item.fullTime)"
            striped
          >
            <strong v-if="!hover">{{ item.learningTime+'/'+ (item.fullTime) }}</strong>
            <strong v-else>{{ StudyingPercentage(item.learningTime, item.fullTime) }}%</strong>
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
            {{item.status}} + 학습하기
          </v-btn>
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
          { text: '번호', value: 'idx', align: 'center', width: '5%', class: "blue lighten-5" },
          {
            text: '제목',
            align: 'center',
            sortable: false,
            value: 'title',
            width: '40%',
            class: "blue lighten-5",
            //primary
          },
          { text: '진행도', value: 'learningState', width: '35%', class: "blue lighten-5"},
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
    }
  },
}
</script>

<style scoped>
  
</style>
