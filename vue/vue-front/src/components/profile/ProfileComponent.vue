<template>
  <v-card
    class="mx-auto"
    max-width="374"
  >
    <v-card-text class="text-center">
      <h2><v-icon class="ml-1">mdi-face-man</v-icon> {{this.$store.state.studentName}}</h2>
    </v-card-text>
    <v-card-text class="text-center">
      <h2>{{this.$store.state.studentNumber}}</h2>
    </v-card-text>

    <v-divider class="mx-4"></v-divider>
    <task-summary-component></task-summary-component>

    <v-divider class="mx-4"></v-divider>
    <lecture-summary-component></lecture-summary-component>
    
    <v-divider class="mx-4"></v-divider>

    <v-card-actions>
      <v-btn
        color="deep-purple lighten-2"
        text
        @click="reserve"
      >
        세부사항
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import { mapState } from 'vuex';
import LectureSummaryComponent from './LectureSummaryComponent'
import TaskSummaryComponent from './TaskSummaryComponent';
export default {
  name: 'ProfileComponent',
  components: {TaskSummaryComponent, LectureSummaryComponent},
  data() {
    return {
      taskTitle: '과제'
    }
  },
  props:{
    
  },
  methods: {
    
  },
  computed: {
    ...mapState({
      completedTask: 'completedTask',
      showCompleted: 'showCompleted', 
      showIncompleted: 'showIncompleted',
    }),
  }
}
</script>

<style>
</style>
