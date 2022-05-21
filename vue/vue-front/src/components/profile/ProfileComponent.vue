<template>
  <v-card
    class="mx-auto"
    max-width="374"
  >
    <v-card-text class="align center">
      <h2><v-icon class="ml-1">mdi-face-man</v-icon> {{this.$store.state.studentName}}</h2>
    </v-card-text>
    <v-card-text class="align center">
      <h2>{{this.$store.state.studentNumber}}</h2>
    </v-card-text>

    <v-divider class="mx-4"></v-divider>

    <v-card-title>과업</v-card-title>

    
    <task-summary></task-summary>

    <v-card-actions>
      <v-btn
        color="deep-purple lighten-2"
        text
        @click="reserve"
      >
        일단은 놔둔다.
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import TaskSummary from './TaskSummary';
export default {
  name: 'ProfileComponent',
  components: {TaskSummary},
  data() {
    return {
      
    }
  },
  props:{
    
  },
  methods: {
    
  },
}
</script>

<style>
</style>
