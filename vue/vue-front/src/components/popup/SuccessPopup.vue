<template>
    <v-snackbar v-model="snackbar" :timeout="timeout" :multi-line="multiLine">
        {{ text }}
        <template v-slot:action="{ attrs }">
            <v-btn color="blue" text v-bind="attrs" @click="snackbar = false">
                닫기
            </v-btn>
        </template>
    </v-snackbar>
</template>

<script>
import { mapState } from 'vuex';

export default {
    name: 'SuccessPopup',
    computed: {
        ...mapState({
            text: state => state.popup.text,
            snackbar: state => state.popup.uccess
            // closed: state => state.popup.closed
        }),
    },
    data() {
        return {
            snackbar: false,
            timeout: 2000,
        };
    },
    computed: {
    },
    methods: {
        openPopup() {
            this.snackbar = true;
        }
    },
};
</script>

<style>
</style>