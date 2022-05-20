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
      <p>공지 내용</p>
      <div class="text--primary">
        {{showDescription()}}
      </div>
    </v-card-text>
    <v-card-actions>
      <v-btn
        text
        color="teal accent-4"
        @click="reveal = true"
      >
        학교 페이지로 이동
      </v-btn>
    </v-card-actions>

    <v-expand-transition>
      <v-card
        v-if="reveal"
        class="transition-fast-in-fast-out v-card--reveal"
        style="height: 100%;"
      >
        <v-card-text class="pb-0">
          <p class="text-h4 text--primary">
            페이지내용
          </p>
          <p>v-card-text내용 지우고 페이지내용 띄우기</p>
        </v-card-text>
        <v-card-actions class="pt-0">
          <v-btn
            text
            color="teal accent-4"
            @click="reveal = false"
          >
            Close
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-expand-transition>
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
