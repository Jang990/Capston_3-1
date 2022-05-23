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
          { text: '기한일', value: 'endDate', width: '20%', class: "blue lighten-5"},
          { text: '미제출', value: 'submitYN', width: '5%', class: "blue lighten-5" },
          { text: '', value: 'data-table-expand', width:'5%', class: "blue lighten-5" },
          // { text: '내용', value: 'description' },
      ],
    }
  },
  props:{
    taskData: Object,
  },
  methods: {
    submitCheck(item) {
      return item.submitYN == 'Y' ? 'red accent-2' : 'light-green accent-2';
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
