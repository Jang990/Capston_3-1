<template>
  <v-container>
    <v-row class="text-align center" no-gutters>
      <v-col class="font-weight-medium subtitle-1 
        red--text text--darken-1">
        {{showIncompleted}}
      </v-col>
      <v-col class="font-weight-medium subtitle-1
        green--text text--darken-1">
        {{showCompleted}}
      </v-col>
      <v-col class="font-weight-medium subtitle-1 
        blue--text text--darken-1">
        {{showTotalTask}}
      </v-col>
    </v-row>
    <v-row class="text-align center" no-gutters>
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
  </v-container>
</template>

<script>
import { mapState } from 'vuex'
export default {
  name: 'TaskSummary',
  data() {
    return {
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
    showTotalTask() {
      return this.$store.getters.showTotalTask;
    },
  },
  updated() {
    
  },
  destoryed() {
  }
}
</script>

<style>
</style>
