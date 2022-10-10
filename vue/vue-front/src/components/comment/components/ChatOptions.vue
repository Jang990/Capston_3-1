<template>
  <div class="flex items-center justify-center">
    <button
      v-if="show"
      @click="this.$emit('delete')"
      class="px-2 py-1 flex items-center font-medium space-x-2 text-soft-red hover:text-pale-red"
    >
      <!-- <DeleteIcon class="fill-current" /> -->
      <v-icon>mdi-arrow-u-right-top-bold</v-icon>
      <span>Delete</span>
    </button>
    <button
      v-if="show"
      @click="this.$emit('edit')"
      class="px-2 py-1 flex items-center font-medium space-x-2 text-moderate-blue hover:text-greyish-blue"
    >
      <!-- <EditIcon class="fill-current" /> -->
      <v-icon>mdi-arrow-u-right-top-bold</v-icon>
      <span>Edit</span>
    </button>
    <button
      class="px-2 py-1 flex items-center font-medium space-x-2 text-moderate-blue hover:text-greyish-blue"
      @click="this.$emit('reply')"
      v-else
    >
      <!-- <ReplyIcon class="fill-current" /> -->
      <v-icon>mdi-arrow-u-right-top-bold</v-icon>
      <span>Reply</span>
    </button>
  </div>
</template>

<script>
// import ReplyIcon from "../test/data/icons/icon-reply.svg";
// import EditIcon from "../test/data/icons/icon-edit.svg";
// import DeleteIcon from "../test/data/icons/data/icon-delete.svg";

// import IconBase from "../test/data/icons/data/IconBase"

export default {
  props: ["show"],
  emits: ["delete", "edit", "reply"],
  components: {
    // ReplyIcon,
    // EditIcon,
    // DeleteIcon,
    // IconBase
  },
};
</script>
