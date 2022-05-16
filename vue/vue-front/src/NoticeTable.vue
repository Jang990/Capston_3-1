<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="noticeData"
      :items-per-page="5"
      class="elevation-1"
    ></v-data-table>
  </div>
</template>

<script>
import axios from "axios"
const mainAxios = axios.create({baseURL: 'http://localhost:38080'});
const delayFlag = 325;
let timeouts = [];

export default {
  name: 'NoticeTable',
  data() {
    return {
      headers: [
          {
            text: '제목',
            align: 'start',
            sortable: false,
            value: 'title',
          },
          { text: '작성자', value: 'author' },
          { text: '작성일', value: 'createDate' },
          // { text: '내용', value: 'description' },
      ],
    }
  },
  props:{
    noticeData: Object,
  },
  methods: {
    
  },
  created() {},
  destoryed() {
    clearTimeout(timeouts);
  },
}
</script>

<style>
</style>
