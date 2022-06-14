<template>
    <v-container>
        <pie-chart :data="insertData()" legend="right" :donut="true"></pie-chart>
        <!-- <pie-chart :data="{'완료': this.$store.state.showCompletedLecture, '미완료': this.$store.state.showIncompletedLecture}" legend="right" :donut="true"></pie-chart> -->
    </v-container>
</template>

<script>
import { mapState } from 'vuex';
let timeout = null;
export default {
    name: 'MainStatisticsComponent',
    ...mapState({
        completedTask: state=> state.completedLecture,
        incompletedTask: state => state.incompletedLecture,
    }),
    data() {
        return {
            // data: {'USA': 90, 'China': 70, 'Russia': 40, 'Germany': 30, 'United Kingdom': 35, 'Turkey': 22}
            data: {'완료': this.completedLecture, '미완료': this.incompletedLecture}
            // data: {'완료': this.$store.state.completedLecture, '미완료': this.$store.state.incompletedLecture}
        }
    },
    computed: {
        
    },
    props:{
        // lectureArray: Object,
    },
    methods: {
        insertData() {
            let obj = {};
            for(let i = 0; i < this.$store.state.subjectCardData.length; i++) {
                // console.log("' "+this.$store.state.subjectCardData[i].subjectName + " '의 완료한 일: " + this.$store.state.subjectCardData[i].cntCompletedTotal);
                let idxStartTitle = this.$store.state.subjectCardData[i].subjectName.indexOf(']')+1
                if(idxStartTitle == -1) idxStartTitle = 0;
                const idxEndTitle = this.$store.state.subjectCardData[i].subjectName.length;
                obj[this.$store.state.subjectCardData[i].subjectName.slice(idxStartTitle, idxEndTitle)] = this.$store.state.subjectCardData[i].cntCompletedTotal;
            }
            return obj;
        }
    },
    created() {
        timeout = setInterval(()=>{
            // console.log('완료: ' + this.$store.state.completedLecture + ', 미완료 : ' + this.$store.state.incompletedLecture);
            // for(let i = 0; i < this.$store.state.subjectCardData.length; i++) {
            //     console.log("' "+this.$store.state.subjectCardData[i].subjectName + " '의 완료한 일: " + this.$store.state.subjectCardData[i].cntCompletedTotal);
            // }
        }, 3000);
    },
    destoryed() {
        clearInterval(timeout);
    },
}
</script>

<style scoped>
    .card {
        border-radius: 3px;
        background-clip: border-box;
        border: 1px solid rgba(0, 0, 0, 0.125);
        box-shadow: 1px 1px 1px 1px rgba(0, 0, 0, 0.21);
        background-color: transparent;
    }
</style>
