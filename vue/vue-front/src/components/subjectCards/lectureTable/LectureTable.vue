<template>
  <v-data-table
      :headers="headers"
      :items="lecturesData"
      :items-per-page="5"
      :single-expand="singleExpand"
      :expanded.sync="expanded"
      item-key="lectureWeekId"
      :item-class="submitCheck"
      show-expand
      class="elevation-1"
    >
    <!-- mobile-breakpoint="0" -->
    <!-- @click:row="showDescription" -->
      <template v-slot:top>
        <v-toolbar flat>
          <v-toolbar-title>{{LectureTitle}}</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-switch
            v-model="singleExpand"
            label="Single View"
            class="mt-2"
          ></v-switch>
        </v-toolbar>
      </template>

      <template v-slot:[`item.learningState`]="{ item }">
        <v-hover
          v-slot="{ hover }"
          open-delay="200"
        >
          <v-progress-linear
            color="light-blue"
            height="18"
            :value="item.learningState"
            v-if="item.learningState == 100"
            striped
          >
            <strong v-if="!hover">{{ item.learningState }}%</strong>
            <strong v-else>{{ item.cntCompleted+'강/'+ (item.cntCompleted+item.cntIncompleted)+'강' }}</strong>
          </v-progress-linear>
          <v-progress-linear
            v-else-if="item.learningState == 0"
            color="blue-grey"
            height="18"
            :value="item.learningState"
            
          >
            <strong v-if="!hover">{{ item.learningState }}%</strong>
            <strong v-else>{{ item.cntCompleted+'강/'+ (item.cntCompleted+item.cntIncompleted)+'강' }}</strong>
          </v-progress-linear>
          <v-progress-linear
            v-else
            color="amber"
            height="18"
            :value="item.learningState"
          >
            <strong v-if="!hover">{{ item.learningState }}%</strong>
            <strong v-else>{{ item.cntCompleted+'강/'+ (item.cntCompleted+item.cntIncompleted)+'강' }}</strong>
          </v-progress-linear>
        </v-hover>
      </template>
      <template v-slot:[`item.submitYN`]="{ item }">
        <v-icon v-if="item.cntIncompleted == 0" color="green accent-3">mdi-check-bold</v-icon>
        <v-icon v-else color="red darken-1">mdi-video-off-outline</v-icon>
      </template>

      <template v-slot:expanded-item="{ headers, item }">
        <td :colspan="headers.length" class="td-for-card">
          <lecture-description :lectureArray="item.lectures"></lecture-description>
        </td>
      </template>
    </v-data-table>
</template>

<script>
import LectureDescription from './LectureDescription';
export default {
  name: 'LectureTable',
  components: {LectureDescription},
  data() {
    return {
      LectureTitle: '강의',
      expanded: [],
      singleExpand: true,
      headers: [
          { text: ' ', value: 'submitYN', width: '5%', class: "blue lighten-5" },//제출여부
          {
            text: '제목',
            align: 'center',
            sortable: false,
            value: 'title',
            width: '45%',
            class: "blue lighten-5",
            //primary
          },
          { text: '마감일', value: 'endDate', width: '20%', class: "blue lighten-5"},
          { text: '학습율', value: 'learningState', width: '25%', class: "blue lighten-5"},
          { text: '', value: 'data-table-expand', width:'5%', class: "blue lighten-5" },
          // { text: '내용', value: 'description' },
      ],
    }
  },
  computed: {
    calcState(total, submitted) {
      return total/submitted;
    },
    
  },
  props:{
    lecturesData: Object,
  },
  methods: {
    submitCheck(item) {
      return '';
      //배경색을 지정하고 싶다면 아래 코드를 커스터마이징해서 쓰면 된다.
      // return item.submitYN == 'Y' ? 'red accent-2' : 'light-green accent-2';
    },
  },
  updated() {
    // console.log(this.lecturesData);
  },
  destoryed() {
    clearTimeout(timeouts);
  },
}
</script>

<style scoped>
.td-for-card{
  padding: 0 !important;
  margin-left: 5%;
}
v-progress-linear > strong {
  cursor: default;
  -ms-user-select: none; 
  -moz-user-select: -moz-none;
  -khtml-user-select: none;
  -webkit-user-select: none;
  user-select: none;
}
</style>
