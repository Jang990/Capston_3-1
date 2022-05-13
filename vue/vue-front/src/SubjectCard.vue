<template>
  <div id="subjectCard">
    {{title}}
  </div>
</template>

<script>
import axios from "axios"

export default {
  name: 'SubjectCard',
  data() {
    return {
      
    }
  },
  props:{
    title: String,
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
#subjectCard{
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
}
</style>
