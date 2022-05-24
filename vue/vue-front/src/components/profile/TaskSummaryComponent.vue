<template>
  <v-container>
    <div class="mx-4  text-subtitle-1 text-left">과제</div>
    <v-row class="text-align center" no-gutters>
      <v-col class="font-weight-medium subtitle-1 
        red--text text--darken-1">
        <span>{{showIncompleted}}</span>
      </v-col>
      <v-col class="font-weight-medium subtitle-1
        green--text text--darken-1">
        <span>{{showCompleted}}</span>
      </v-col>
      <v-col class="font-weight-medium subtitle-1 
        blue--text text--darken-1">
        <span>{{showTotalTask}}</span>
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
