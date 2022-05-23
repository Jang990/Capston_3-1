<template>
  <v-data-table
      :headers="headers"
      :items="taskData"
      :items-per-page="5"
      :single-expand="singleExpand"
      :expanded.sync="expanded"
      item-key="taskId"
      :item-class="submitCheck"
      show-expand
      class="elevation-1"
    >
    <!-- @click:row="showDescription" -->
      <template v-slot:top>
        <v-toolbar flat>
          <v-toolbar-title>{{tableTitle}}</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-switch
            v-model="singleExpand"
            label="Single View"
            class="mt-2"
          ></v-switch>
        </v-toolbar>
      </template>

      <template v-slot:[`item.submittedState`]="{ item }">
        <v-hover
          v-slot="{ hover }"
          open-delay="200"
        >
          <v-progress-linear
            color="light-blue"
            height="18"
            :value="Math.ceil(item.submittedState)"
            striped
          >
            <strong v-if="!hover">{{ Math.ceil(item.submittedState) }}%</strong>
            <strong v-else>{{ item.submissionNum+'명/'+ item.totalNum+ '명' }}</strong>
          </v-progress-linear>
        </v-hover>
      </template>
      <template v-slot:[`item.submitYN`]="{ item }">
        <v-icon v-if="item.submitYN == 'N'" color="green accent-3">mdi-check-bold</v-icon>
        <v-icon v-else color="red darken-1">mdi-note-off-outline</v-icon>
      </template>

      <template v-slot:expanded-item="{ headers, item }">
        <td :colspan="headers.length" class="td-for-card">
          <task-description-card :description="item.description"></task-description-card>
        </td>
      </template>
    </v-data-table>
</template>

<script>
import TaskDescriptionCard from './TaskDescriptionCard';
export default {
  name: 'TaskTable',
  components: {TaskDescriptionCard},
  data() {
    return {
      tableTitle: '과제',
      expanded: [],
      singleExpand: true,
      headers: [
          {
            text: '제목',
            align: 'center',
            sortable: false,
            value: 'title',
            width: '45%',
            class: "blue lighten-5",
            //primary
          },
          { text: '잔여일', value: 'endDate', width: '20%', class: "blue lighten-5"},
          { text: '제출/총원', value: 'submittedState', width: '25%', class: "blue lighten-5"},
          { text: ' ', value: 'submitYN', width: '5%', class: "blue lighten-5" },//제출여부
          { text: '', value: 'data-table-expand', width:'5%', class: "blue lighten-5" },
          // { text: '내용', value: 'description' },
      ],
    }
  },
  computed: {
    calcState(total, submitted) {
      return total/submitted;
    }
  },
  props:{
    taskData: Object,
  },
  methods: {
    submitCheck(item) {
      return '';
      //배경색을 지정하고 싶다면 아래 코드를 커스터마이징해서 쓰면 된다.
      // return item.submitYN == 'Y' ? 'red accent-2' : 'light-green accent-2';
    },
  },
  updated() {
    console.log(this.taskData);
  },
  destoryed() {
    clearTimeout(timeouts);
  },
}
</script>

<style>
.td-for-card{
  padding: 0 !important;
}
</style>
