// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'

// vuetify
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'
Vue.use(Vuetify);

//chart
import {Chart} from 'chart.js'
import Chartkick from 'vue-chartkick'
Vue.use(Chartkick.use(Chart));

// import 'material-design-icons-iconfont/dist/material-design-icons.css' // vuetify prepend-icon 안나올 때  npm install material-design-icons-iconfont 설치후 이 코드 추가
// Vue.use(Vuetify, {
//   iconfont: 'fa',
//   icons: { iconfont: 'md', }, // vuetify prepend-icon 안나올 때 이것 추가
// })

Vue.config.productionTip = false

// vue-devtools 확장프로그램 허용 코드배포시에는 삭제하기
Vue.config.devtools = true; 

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  vuetify: new Vuetify(),
  components: { App },
  template: '<App/>'
})
