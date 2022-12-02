<template>
  <v-container fluid style="padding: 0;">
    <v-row no-gutters>
      <!-- <v-col sm="2" class="scrollable">
        <chats></chats>
      </v-col> -->
        <v-col>
          <div class="chat-container" v-on:scroll="onScroll" ref="chatContainer" >
            <message :messages="messages" @imageLoad="scrollToEnd"></message>
          </div>
          <emoji-picker :show="emojiPanel" @close="toggleEmojiPanel" @click="addEmojiToMessage"></emoji-picker>
          <div class="typer">
            <input type="text" placeholder="Type here..." v-on:keyup.enter="sendMessage" v-model="content">
            <v-btn icon class="blue--text emoji-panel" @click="toggleEmojiPanel">
              <v-icon>mdi-emoticon-outline</v-icon>
            </v-btn>
          </div>
        </v-col>
      </v-row>
    </v-container>
</template>

<script>
  import Stomp from 'webstomp-client'
  import SockJS from 'sockjs-client'
  import * as chatApi from '@/api/chat';
  import Message from './parts/Message.vue'
  import EmojiPicker from './parts/EmojiPicker.vue'

  // import Chats from './parts/Chats.vue'
  // import * as firebase from 'firebase'

  export default {
    props:{
      roomId: String,
    },
    data () {
      return {
        content: '',
        chatMessages: [],
        emojiPanel: false,
        currentRef: {},
        loading: false,
        totalChatHeight: 0,

        // message: "",
        recvList: []
      }
    },
    created() {
      // 이전 채팅 내역을 불러옵니다.
      this.loadPrevMessage();
      // App.vue가 생성되면 소켓 연결을 시도합니다.
      this.connect()
    },
    mounted () {
      // this.loadChat()
      // this.$store.dispatch('loadOnlineUsers')
    },
    components: {
      'message': Message,
      'emoji-picker': EmojiPicker,
      // 'chats': Chats
    },
    computed: {
      messages () {
        return this.recvList
      },
      username () {
        return this.$store.state.user.nickname
      },
      onNewMessageAdded () {
        const that = this
        let onNewMessageAdded = function (snapshot, newMessage = true) {
          let message = snapshot.val()
          message.key = snapshot.key
          /*eslint-disable */
          var urlPattern = /(\b(https?|ftp|file):\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;]*[-A-Z0-9+&@#\/%=~_|])/ig
          /*eslint-enable */
          message.content = message.content
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#039;')
          message.content = message.content.replace(urlPattern, "<a href='$1'>$1</a>")
          if (!newMessage) {
            that.chatMessages.unshift(that.processMessage(message))
            that.scrollTo()
          } else {
            that.chatMessages.push(that.processMessage(message))
            that.scrollToEnd()
          }
        }
        return onNewMessageAdded
      }
    },
    watch: {
      '$route.params.id' (newId, oldId) {
        this.currentRef.off('child_added', this.onNewMessageAdded)
        this.loadChat()
      }
    },
    methods: {
      loadChat () {
        this.totalChatHeight = this.$refs.chatContainer.scrollHeight
        this.loading = false
        if (this.id !== undefined) {
          this.chatMessages = []
          let chatID = this.id
          // this.currentRef = firebase.database().ref('messages').child(chatID).child('messages').limitToLast(20)
          // this.currentRef.on('child_added', this.onNewMessageAdded)
        }
      },
      // ---------------------
      async loadPrevMessage() {
        const res = await chatApi.loadPrevMessage(this.roomId);
        //여기에 닉네임 넣어야함
        const objs = res.data;
        console.log(objs);
        for (let i = 0; i < objs.length; i++) {
          this.recvList.push(objs[i]);
        }
      },
      send() {
        console.log("====> 메시지 정보 리스트");
        console.log(this.recvList);
        if (this.stompClient && this.stompClient.connected) {
          const msg = { 
            subjectId: this.roomId,
            nickname: this.$store.state.user.nickname,
            senderId: this.$store.state.user.studentId,
            message: this.content,
            createdTime: null
          };
          console.log("====>보낸객체:");
          console.log(msg);
          
          this.stompClient.send("/receive", JSON.stringify(msg), {});
        }
      },    
      connect() {
        // 현재 서버의 endpoint는 /ws/chat
        const serverURL = "http://localhost:8080/ws/chat"
        let socket = new SockJS(serverURL);
        this.stompClient = Stomp.over(socket);
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
      },
      // ---------------------


      onScroll () {
        let scrollValue = this.$refs.chatContainer.scrollTop
        const that = this
        if (scrollValue < 100 && !this.loading) {
          this.totalChatHeight = this.$refs.chatContainer.scrollHeight
          this.loading = true
          let chatID = this.id
          let currentTopMessage = this.chatMessages[0]
          if (currentTopMessage === undefined) {
            this.loading = false
            return
          }

          //페이징 한 것 로딩해오기
          /*
          firebase.database().ref('messages').child(chatID).child('messages').orderByKey().endAt(currentTopMessage.key).limitToLast(20).once('value').then(
            function (snapshot) {
              let tempArray = []
              snapshot.forEach(function (item) {
                tempArray.push(item)
              })
              if (tempArray[0].key === tempArray[1].key) return
              tempArray.reverse()
              tempArray.forEach(function (child) { that.onNewMessageAdded(child, false) })
              that.loading = false
            }
          )
          */
        }
      },
      processMessage (message) {
        /*eslint-disable */
        var imageRegex = /([^\s\']+).(?:jpg|jpeg|gif|png)/i
        /*eslint-enable */
        if (imageRegex.test(message.message)) {
          message.image = imageRegex.exec(message.message)[0]
        }
        var emojiRegex = /([\u{1f300}-\u{1f5ff}\u{1f900}-\u{1f9ff}\u{1f600}-\u{1f64f}\u{1f680}-\u{1f6ff}\u{2600}-\u{26ff}\u{2700}-\u{27bf}\u{1f1e6}-\u{1f1ff}\u{1f191}-\u{1f251}\u{2934}-\u{1f18e}])/gu
        if (emojiRegex.test(message.message)) {
          message.message = message.message.replace(emojiRegex, '<span class="emoji">$1</span>')
        }
        return message
      },
      sendMessage () {
        if (this.content !== '') {
          this.send()
          this.content = ''
        }
      },
      scrollToEnd () {
        this.$nextTick(() => {
          var container = this.$el.querySelector('.chat-container')
          container.scrollTop = container.scrollHeight
        })
      },
      scrollTo () {
        this.$nextTick(() => {
          let currentHeight = this.$refs.chatContainer.scrollHeight
          let difference = currentHeight - this.totalChatHeight
          var container = this.$el.querySelector('.chat-container')
          container.scrollTop = difference
        })
      },
      // 이모지 추가
      addEmojiToMessage (emoji) {
        this.content += emoji.value
      },
      // 이모지 패널 보이기
      toggleEmojiPanel () {
        this.emojiPanel = !this.emojiPanel
      }
    }
  }
</script>

<style>
  /* .scrollable {
    overflow-y: auto;
    height: 90vh;
  } */
  
  .typer{
    box-sizing: border-box;
    display: flex;
    align-items: center;
    bottom: 0;
    height: 4.9rem;
    width: 100%;
    background-color: #fff;
    box-shadow: 0 -5px 10px -5px rgba(0,0,0,.2);
  }
  .typer input[type=text]{
    position: absolute;
    left: 2.5rem;
    padding: 1rem;
    width: 80%;
    background-color: transparent;
    border: none;
    outline: none;
    font-size: 1.25rem;
  }
  .chat-container{
    box-sizing: border-box;
    height: calc(100vh - 27.5rem);
    overflow-y: auto;
    padding: 10px;
    background-color: #f2f2f2;
  }
  .message{
    text-align: left;
    margin-bottom: 3px;
  }
  .message.system{
    margin: 10px;
    text-align: center;
  }
  .message.system .content{
    background-color: rgb(143, 217, 219);
  }
  .message.own{
    text-align: right;
  }
  .message.own .content{
    background-color: lightskyblue;
  }
  .chat-container .username{
    font-size: 18px;
    font-weight: bold;
  }
  .chat-container .content{
    padding: 8px;
    background-color: lightgreen;
    border-radius: 10px;
    display:inline-block;
    box-shadow: 0 1px 3px 0 rgba(0,0,0,0.2), 0 1px 1px 0 rgba(0,0,0,0.14), 0 2px 1px -1px rgba(0,0,0,0.12);
    max-width: 50%;
    word-wrap: break-word;
    }
  @media (max-width: 480px) {
    .chat-container .content{
      max-width: 60%;
    }
  }

</style>
