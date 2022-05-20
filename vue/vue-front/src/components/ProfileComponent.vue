<template>
  <v-card
    class="mx-auto"
    max-width="374"
  >
    <v-card-text class="align center" prepend-icon="mdi-lock">
      <h2>{{this.$store.state.studentName}}</h2>
    </v-card-text>
    <v-card-text class="align center" prepend-icon="material-icons">
      <h2>{{this.$store.state.studentNumber}}</h2>
    </v-card-text>

    <v-divider class="mx-4"></v-divider>

    <v-card-title>과업</v-card-title>

    <v-card-text>
      미완료 / 완료 / 합
    </v-card-text>

    <v-card-actions>
      <v-btn
        color="deep-purple lighten-2"
        text
        @click="reserve"
      >
        일단은 놔둔다.
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  name: 'ProfileComponent',
  data() {
    return {
      
    }
  },
  props:{
    
  },
  methods: {
    
  },
}
</script>

<style>
</style>
