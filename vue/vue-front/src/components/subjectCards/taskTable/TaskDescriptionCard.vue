<template>
<!--
  매우 중요!
  지금 min-height를 설정해야 내용이 짧아서 컨텐츠밖으로 버튼이 튀어나가는 것을 막을 수 있다. 근데 못찾아서 접음.
-->
  <v-card
    class="mx-auto"
    id="main-card"
  >
    <v-card-text>
      <p class="body-1">과제 내용</p>
      <div class="text-justify">
        {{showDescription()}}
      </div>
    </v-card-text>
    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn
        color="primary"
        dark
        href="https://cyber.inhatc.ac.kr/"
        target="_blank"
      >
        과제 페이지로 이동
      </v-btn>
    </v-card-actions>

  </v-card>
</template>

<script>

export default {
  name: 'DescriptionCard',
  data() {
    return {
      reveal: false,
    }
  },
  props:{
    description: String,
  },
  methods: {
    showDescription() {
      //이 함수 computed로 옮기자.
      if(this.description == null || this.description == '') {
        return "내용이 없거나 학교 사이트에서 조회 실패!<br> 하단 버튼을 눌러 학교사이트로 들어가길 권장합니다.";
      }
      else {
        return this.description;
      }
    }
  },
  computed: {
    
  }, 
}
</script>

<style scoped>
.v-card--reveal {
  bottom: 0;
  opacity: 1 !important;
  position: absolute;
  width: 100%;
}
#main-card{
  margin: 0;
  border-radius: 0;
}
</style>
