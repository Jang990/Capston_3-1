<template>
    <v-card class="card">
        <v-card-text class="pa-2">
            <pie-chart :data="{'완료': this.$store.state.showCompletedLecture, '미완료': this.$store.state.showIncompletedLecture}" legend="right" :donut="true"></pie-chart>
        </v-card-text>
    </v-card>
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
            data: {'완료': this.$store.state.completedLecture, '미완료': this.$store.state.incompletedLecture}
            // data: {'완료': this.$store.state.completedLecture, '미완료': this.$store.state.incompletedLecture}
        }
    },
    computed: {
        
    },
    props:{
        // lectureArray: Object,
    },
    methods: {

    },
    created() {
        timeout = setInterval(()=>{
            console.log('완료: ' + this.$store.state.completedLecture + ', 미완료 : ' + this.$store.state.incompletedLecture);
        }, 1000);
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
