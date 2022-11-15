<template>
  <div id="app">
    유저이름: 
    <input
      v-model="sender"
      type="text"
    >
    내용: <input
      v-model="message"
      type="text"
      @keyup="sendMessage"
    >
    <div
      v-for="(item, idx) in recvList"
      :key="idx"
    >
      <h3>유저이름: {{ item.senderId }}</h3>
      <h3>내용: {{ item.message }}</h3>
    </div>
  </div>
</template>

<script>
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'
import * as chatApi from '@/api/chat';

export default {
  name: 'App',
  props:{
    roomId: String,
  },
  data() {
    return {
      sender: "",
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
      
      console.log("여기");
      console.log(res);
      const objs = res.data;
      for (let i = 0; i < objs.length; i++) {
        this.recvList.push(objs[i]);
      }
    },
    sendMessage (e) {
      if(e.keyCode === 13 && this.sender !== '' && this.message !== ''){
        this.send()
        this.message = ''
      }
    },    
    send() {
      console.log("Send message:" + this.message);
      if (this.stompClient && this.stompClient.connected) {
        const msg = { 
          subjectId: this.roomId,
          senderId: this.sender,
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
</style>
