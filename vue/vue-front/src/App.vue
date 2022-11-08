<template>
  <div id="app">
    <template v-if="!loginCheck">
        <!-- <test-component></test-component> -->
        <!-- <login-component></login-component> -->

        <!-- 독립 채팅방 2개 테스트 코드 -->
        <!-- 
          <test-chat roomId="vvvv"></test-chat>
          <test-chat roomId="aaaa"></test-chat> 
        -->
    </template>
    <!-- 라우터 공부후 템플릿 ifelse를 대체하기. -->
    <v-app id="inspire" v-else>
      <v-app-bar
        app
        color="white"
        flat
      >
        <v-avatar
          :color="$vuetify.breakpoint.smAndDown ? 'grey darken-1' : 'transparent'"
          size="32"
        ></v-avatar>

        <v-tabs
          centered
          class="ml-n9"
          color="grey darken-1"
        >
          <v-tab
            v-for="link in links"
            :key="link"
          >
            {{ link }}
          </v-tab>
        </v-tabs>

        <v-avatar
          class="hidden-sm-and-down"
          color="grey darken-1 shrink"
          size="32"
        ></v-avatar>
      </v-app-bar>

      <v-main class="grey lighten-3">
        <v-container>
          <v-row>
            <v-col
              cols="12"
              sm="2"
            >
              <v-sheet
                rounded="lg"
                min-height="268"
              >
                <profile-component></profile-component>
              </v-sheet>
            </v-col>

            <v-col
              cols="12"
              sm="8"
            >
              <v-sheet
                min-height="70vh"
                rounded="lg"
              >
                <!--  -->
                <template v-if="subjectCardData.length > 0">
                  <!-- <subject-card v-for="(card, i) in subjectCardData" 
                    :key="i" v-bind:card="card">
                  </subject-card> -->
                  <main-statistics-component 
                    v-if="this.$store.state.completedLecture != 0 || this.$store.state.incompletedLecture != 0"
                  >
                  </main-statistics-component>
                  <v-progress-circular
                    v-else
                    :size="70"
                    :width="7"
                    color="purple"
                    indeterminate
                  ></v-progress-circular>

                  <v-divider class="mx-4"></v-divider>
                  <v-container>
                    <v-row dense>
                      <v-col
                        v-for="(card, i) in subjectCardData"
                        :key="i"
                        cols="12"
                        class="mx-auto"
                      >
                        <subject-card v-bind:index="i"></subject-card>
                      </v-col>
                    </v-row>
                  </v-container>
                </template>
                <div v-else>조회된 과목이 없습니다!</div>
              </v-sheet>
            </v-col>

            <!-- <v-col
              cols="12"
              sm="2"
            >
              <v-sheet
                rounded="lg"
                min-height="268"
              >
                요약 그래프 위치
              </v-sheet>
            </v-col> -->
          </v-row>
        </v-container>
      </v-main>
    </v-app>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import store from '@/store/store';
import axios from "axios";
import SubjectCard from "./components/subjectCards/SubjectCard";
import LoginComponent from "./components/auth/Login";
import ProfileComponent from './components/profile/ProfileComponent';
import MainStatisticsComponent from './components/statisticsComponents/MainStatisticsComponent';

import TestComponent from './components/comment/CommentComponent';
import TestChat from './components/auth/card/TestChat.vue';

export default {
  name: 'App',
  store,
  components: {SubjectCard, LoginComponent,ProfileComponent, MainStatisticsComponent, TestComponent, TestChat},
  computed: {
    ...mapState(['loginCheck', 'subjectCardData']),
  },
  data() {
    return {
      links: [
        'Dashboard',
        'Messages',
        'Profile',
        'Updates',
      ],
    }
  },
  methods: {
    
  },
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
  margin-bottom: 60px;
}
</style>
