<template>
  <v-card
      class="mx-auto"
      max-width="800"
      id="subjectCard"
    >
      <!-- <v-img
        src="https://cdn.vuetifyjs.com/images/cards/sunshine.jpg"
        height="200px"
      ></v-img> -->

      <v-card-title>
        {{card.subjectName}} - {{card.owner}}
      </v-card-title>

      <!-- <v-card-subtitle>
        {{card.subjectName}} - {{card.owner}}
      </v-card-subtitle> -->

      <v-card-actions>
        <v-btn
        color="orange lighten-2"
        text
        >
        
        </v-btn>
        <v-spacer></v-spacer> 
        <!-- spacer뒤에 btn을 하면 오른쪽아래 아이콘처럼 작게 붙힌다. -->
        <v-btn
          text
          @click="show = !show"
        >
          과제 조회<v-icon> {{ show ? 'mdi-chevron-up' : 'mdi-chevron-down' }}</v-icon>
        </v-btn>
      </v-card-actions>

      <v-expand-transition>
        <div v-show="show">
          <v-divider></v-divider>

          <v-card-text>
            과제 내용에 대한 것들...<br>
            I'm a thing. But, like most politicians, he promised more than he could deliver. You won't have time for sleeping, soldier, not with all the bed making you'll be doing. Then we'll go with that data file! Hey, you add a one and two zeros to that or we walk! You're going to do his laundry? I've got to find a way to escape.
          </v-card-text>
        </div>
      </v-expand-transition>
    </v-card>
</template>

<script>
import axios from "axios"

export default {
  name: 'SubjectCard',
  data() {
    return {
      show: false,
    }
  },
  props:{
    card: Object,
  },
  methods: {
    dbSearch() {
      axios.get('http://localhost:38080/vueDB')
        .then(function (response) {
          // handle success
          console.log(response);
        })
        .catch(function (error) {
          // handle error
          console.log(error);
        })
        .then(function () {
          // always executed
        });
    }
  },
  
}
</script>

<style>
/* #subjectCard{
  border: 1px solid green;
  width: 200px;
  height: 50px;
  border-radius: 7px;
  margin-left: 20%;

  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 10px;
} */
</style>
