<template>
  <v-card
    class="mx-auto"
    id="main-card"
    max-width="800"
  >
    <v-card-text>
      <p>공지 내용</p>
      <div class="text--primary">
        {{description}}
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
