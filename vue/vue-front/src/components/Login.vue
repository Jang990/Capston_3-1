<template>
  <v-app id="inspire">
      <v-content>
        <v-container fluid fill-height>
            <v-layout align-center justify-center>
              <v-flex xs12 sm8 md4>
                  <div v-if="loginFlag">
                    <login-card></login-card>
                  </div>
                  <div v-else>
                    <sign-up-card></sign-up-card>
                  </div>
              </v-flex>
            </v-layout>
        </v-container>
      </v-content>
  </v-app>
</template>

<script>
import { login } from '../api/auth';
import LoginCard from './auth/LoginCard.vue';
import SignUpCard from './auth/SignUpCard.vue';
import { mapState } from 'vuex';

export default {
  name: 'Login',
  components: {LoginCard,SignUpCard},
  data() {
    return {
      signUp: false,
    };
  },
  props: {
      source: String,
  },
  computed: {
    ...mapState(['loginFlag']),
  },
  methods: {
  },
};
</script>

<style></style>