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
          {{totalTask}}
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
let timerAll = [];
import { mapState } from 'vuex'
export default {
  name: 'TaskSummary',
  data() {
    return {
      showCompleted: 0,
      showIncompleted: 0,
    }
  },
  props:{
    
  },
  methods: {
    
  },
  computed: {
    ...mapState({
      completedTask: 'completedTask',
      incompletedTask: 'incompletedTask', 
      subjectCardData: 'subjectCardData',
      loginCheck: 'loginCheck',
      // isFinishCrawling: state => state.subjectCardData[state.subjectCardData.length].isCrawling,
    }),
    totalTask() {
      return this.$store.getters.totalTask;
    },
    checkTasks() {
      if(this.completedTask != this.showCompleted) this.showCompleted += 1;
      if(this.incompletedTask != this.showinCompleted) this.showinCompleted += 1;
      console.log('안녕');
      // if(this.showTotal != this.totalTask) this.showTotal += 1;
    },
    // showTotal() {
    //   return this.showCompleted + this.showIncompleted;
    // }
  },
  updated() {
    const checkTask = new Promise((resolve, reject) => {
      while(true){
        if(this.loginCheck) break;
      }
      resolve();
    });
      
    checkTask.then(() => {
      let timer = setInterval(()=>{
        if(this.completedTask != this.showCompleted) this.showCompleted += 1;
        if(this.incompletedTask != this.showIncompleted) this.showIncompleted += 1;
        console.log('완료과제 : ' + this.completedTask+' == '+ this.showCompleted + ', 미완료과제: ' + this.incompletedTask+' == '+ this.showIncompleted + ', ' + this.subjectCardData[this.subjectCardData.length-1].isCrawling[2]);
        if(!this.subjectCardData[this.subjectCardData.length-1].isCrawling[2] && 
          this.completedTask == this.showCompleted && 
          this.incompletedTask == this.showIncompleted) {
          clearInterval(timer);
        }
      }, 1000);
      timerAll[0] = timer;
    });
  },
  destoryed() {
    clearInterval(timerAll);
  }
}
</script>

<style>
</style>
