<template>
  <v-container>
    <v-data-table
        :headers="headers"
        :items="noticeData"
        :items-per-page="5"
        class="elevation-1"
    >
    </v-data-table>
  </v-container>
</template>

<script>

export default {
  name: 'NoticeTable',
  data() {
    return {
      headers: [
          {
            text: '제목',
            align: 'center',
            sortable: false,
            value: 'title',
            width: '60%',
            class: "blue lighten-5",
            //primary
          },
          { text: '작성자', value: 'author', width: '20%', class: "blue lighten-5"},
          { text: '작성일', value: 'createDate', width: '20%', class: "blue lighten-5" },
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
