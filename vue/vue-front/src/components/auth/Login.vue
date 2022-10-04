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
        <!-- 회원가입 성공 팝업 -->
        <success-popup></success-popup>
      </v-content>
  </v-app>
</template>

<script>
import { login } from '../../api/auth';
import LoginCard from './card/LoginCard.vue';
import SignUpCard from './card/SignUpCard.vue';
import { mapState } from 'vuex';
import SuccessPopup from '../popup/SuccessPopup';

export default {
  name: 'Login',
  components: {LoginCard,SignUpCard, SuccessPopup},
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