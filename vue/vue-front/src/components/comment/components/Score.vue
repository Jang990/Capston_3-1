<template>
  <!-- 추천 컴포넌트 -->
  <div class="score-input">
    <button @click="this.$emit('score-changed', val + 1)">
      <!-- <Plus /> -->
      <v-icon>mdi-arrow-u-right-top-bold</v-icon>
    </button>
    <div>
      {{ val }}
    </div>
    <button @click="this.$emit('score-changed', val - 1)">
      <!-- <Minus /> -->
      <v-icon>mdi-arrow-u-right-top-bold</v-icon>
    </button>
  </div>
</template>

<script>
// import Plus from "../test/data/icons/icon-plus.svg";
// import Minus from "../test/data/icons/icon-minus.svg";

// import IconBase from "../test/data/icons/data/IconBase"

export default {
  name: "ScoreInput",
  emits: ["score-changed"],
  props: {
    value: Number,
  },
  components: { 
    // Plus, 
    // Minus 
    // IconBase
  },
  data() {
    return {
      val: this.$props.value || 0,
    };
  },
  watch: {
    value(val) {
      this.val = val;
    },
  },
};
</script>
