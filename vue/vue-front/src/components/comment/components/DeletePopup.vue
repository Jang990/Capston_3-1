<template>
  <!-- delete 버튼을 눌렀을 때 화면 가운데에 생성되는 팝업 -->
  <div
    class="w-screen h-screen bg-black/40 fixed inset-0 grid place-items-center"
    v-if="show"
  >
    <div
      class="w-[400px] p-8 bg-white space-y-4 flex flex-shrink-0 flex-col rounded-md"
    >
      <h1 class="font-medium text-2xl text-dark-blue">댓글 삭제</h1>
      <p class="text-greyish-blue font-medium">
        정말 댓글을 삭제하시겠습니까? 삭제하시면 댓글이 제거되고 취소할 수 없습니다.
      </p>
      <div class="flex space-x-4 items-center">
        
        <!-- 삭제 안함 로직 -->
        <button
          @click="this.$emit('cancel')"
          class="px-4 w-1/2 py-3 uppercase text-white font-medium rounded-md bg-greyish-blue"
        >
          취소
        </button>

        <!-- 삭제 로직 -->
        <button
          @click="this.$emit('proceed')"
          class="px-4 w-1/2 py-3 uppercase text-white font-medium rounded-md bg-soft-red"
        >
          댓글 삭제
        </button>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "DeletePopup",
  emits: ["proceed", "cancel"],
  props: ["show"],
};
</script>
