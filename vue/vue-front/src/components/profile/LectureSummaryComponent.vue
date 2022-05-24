<template>
  <v-container>
    <div class="mx-4  text-subtitle-1 text-left">수업</div>
    <v-row class="text-align center" no-gutters>
      <v-col class="font-weight-medium subtitle-1 
        red--text text--darken-1">
        <span>{{showIncompletedLecture}}</span>
      </v-col>
      <v-col class="font-weight-medium subtitle-1
        green--text text--darken-1">
        <span>{{showCompletedLecture}}</span>
      </v-col>
      <v-col class="font-weight-medium subtitle-1 
        blue--text text--darken-1">
        <span>{{showTotalLecture}}</span>
      </v-col>
    </v-row>
    <v-row class="text-align center" no-gutters style="cursor: default;">
      <v-col class="text--disabled text-caption">
        미수강
      </v-col>
      <v-col class="text--disabled text-caption">
        수강
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
      showCompletedLecture: 'showCompletedLecture',
      showIncompletedLecture: 'showIncompletedLecture'
    }),
    showTotalLecture() {
      return this.$store.getters.showTotalLecture;
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
