<template>
  <v-container>
      <v-row class="text-align center" no-gutters>
        <v-col class="font-weight-medium subtitle-1 
          red--text text--darken-1">
          100
        </v-col>
        <v-col class="font-weight-medium subtitle-1
          green--text text--darken-1">
          200
        </v-col>
        <v-col class="font-weight-medium subtitle-1 
          blue--text text--darken-1">
          300
        </v-col>
      </v-row>
      <v-row class="text-align center" no-gutters>
        <v-col class="text--disabled text-caption">
          미완료
        </v-col>
        <v-col class="text--disabled text-caption">
          완료
        </v-col>
        <v-col class="text--disabled text-caption">
          합
        </v-col>
      </v-row>
    </v-container>
</template>

<script>

export default {
  name: 'TaskSummary',
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
