<template>
  <div id="app">
    내용: <input
      v-model="message"
      type="text"
      @keyup="sendMessage"
    >
    <div
      v-for="(item, idx) in recvList"
      :key="idx"
    >
      <div class="message">
        <h3>유저이름: {{ item.senderId }}</h3>
        <h3>내용: {{ item.message }}</h3>
      </div>
    </div>
  </div>
</template>

<script>
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'
import * as chatApi from '@/api/chat';
import { mapState } from 'vuex';
import {User_getStudentId, User_showNickname} from '@/store/store'

export default {
  name: 'App',
  props:{
    roomId: String,
  },
  computed: {
    ...mapState({
      studentNumber: User_getStudentId,
    }),
  },
  data() {
    return {
      message: "",
      recvList: []
    }
  },
  created() {
    // 이전 채팅 내역을 불러옵니다.
    this.loadPrevMessage();

    // App.vue가 생성되면 소켓 연결을 시도합니다.
    this.connect()
  },
  methods: {
    async loadPrevMessage() {
      const res = await chatApi.loadPrevMessage(this.roomId);
      //여기에 닉네임 넣어야함
      const objs = res.data;
      console.log(objs);
      for (let i = 0; i < objs.length; i++) {
        this.recvList.push(objs[i]);
      }
    },
    sendMessage (e) {
      if(e.keyCode === 13 && this.message !== ''){
        this.send()
        this.message = ''
      }
    },    
    send() {
      console.log("Send message:" + this.message);
      if (this.stompClient && this.stompClient.connected) {
        const msg = { 
          subjectId: this.roomId,
          nickname: this.$store.state.user.nickname,
          senderId: this.$store.state.user.studentId,
          message: this.message,
          createdTime: null
        };
        this.stompClient.send("/receive", JSON.stringify(msg), {});
      }
    },    
    connect() {
      // 현재 서버의 endpoint는 /ws/chat
      const serverURL = "http://localhost:8080/ws/chat"
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);
      console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`)
      this.stompClient.connect(
        {},
        frame => {
          // 소켓 연결 성공
          this.connected = true;
          console.log('소켓 연결 성공', frame);
          // 서버의 메시지 전송 endpoint를 구독합니다.
          // /topic/chat을 구독하려면 /app/topic/chat
          this.stompClient.subscribe("/topic/chat/room/" + this.roomId, res => {
            console.log('구독으로 받은 메시지 입니다.', res.body);

            // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
            this.recvList.push(JSON.parse(res.body))
          });
        },
        error => {
          // 소켓 연결 실패
          console.log('소켓 연결 실패', error);
          this.connected = false;
        }
      );        
    }
  }
}
</script>

<style>
#app {
  padding: 15px;
  margin-top: 15px;
  margin-bottom: 0;
}
.message{
  margin-top: 7px;
  margin-bottom: 7px;
}
</style>
