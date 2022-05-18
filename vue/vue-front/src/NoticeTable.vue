<template>
  <v-container>
    <v-data-table
      :headers="headers"
      :items="noticeData"
      :items-per-page="5"
      :single-expand="singleExpand"
      :expanded.sync="expanded"
      item-key="noticeId"
      show-expand
      class="elevation-1"
    >
    <!-- @click:row="showDescription" -->
      <template v-slot:top>
        <v-toolbar flat>
          <v-toolbar-title>{{tableTitle}}</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-switch
            v-model="singleExpand"
            label="하나의 내용만 보기"
            class="mt-2"
          ></v-switch>
        </v-toolbar>
      </template>
      <template v-slot:expanded-item="{ headers, item }">
        <td :colspan="headers.length">
          {{ item.description }}
        </td>
      </template>
    </v-data-table>
  </v-container>

</template>

<script>

export default {
  name: 'NoticeTable',
  data() {
    return {
      tableTitle: '공지사항',
      expanded: [],
      singleExpand: true,
      headers: [
          {
            text: '제목',
            align: 'center',
            sortable: false,
            value: 'title',
            width: '55%',
            class: "blue lighten-5",
            //primary
          },
          { text: '작성자', value: 'author', width: '20%', class: "blue lighten-5"},
          { text: '작성일', value: 'createDate', width: '20%', class: "blue lighten-5" },
          { text: '', value: 'data-table-expand', width:'5%', class: "blue lighten-5" },
          // { text: '내용', value: 'description' },
      ],
    }
  },
  props:{
    noticeData: Object,
  },
  methods: {
    showDescription(value) {
      console.log(value);
    }
  },
  created() {},
  destoryed() {
    clearTimeout(timeouts);
  },
}
</script>

<style>
</style>
