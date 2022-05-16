<template>
  <v-app id="inspire">
      <v-content>
        <v-container fluid fill-height>
            <v-layout align-center justify-center>
              <v-flex xs12 sm8 md4>
                  <v-card class="elevation-12">
                    <v-toolbar dark color="primary">
                        <v-toolbar-title>Login form</v-toolbar-title>
                    </v-toolbar>
                    <v-card-text>
                        <v-form>
                          <v-text-field
                              v-model="id"
                              prepend-icon="mdi-account"
                              name="login"
                              label="Login"
                              type="text"
                          ></v-text-field>
                          <v-text-field
                              v-model="password"
                              id="password"
                              prepend-icon="mdi-lock"
                              name="password"
                              label="Password"
                              type="password"
                          ></v-text-field>
                        </v-form>
                    </v-card-text>
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="primary" to="/" @click="checkLog">Login</v-btn>
                    </v-card-actions>
                  </v-card>
              </v-flex>
            </v-layout>
        </v-container>
      </v-content>
  </v-app>
</template>

<script>
import axios from "axios"

export default {
  name: 'Login',
  data() {
    return {
      id: '',
      password: '',
    };
  },
  props: {
      source: String,
  },
  methods: {
    checkLog(){
      const api = axios.create({baseURL: 'http://localhost:38080'});
      api.post('/vueLoginCheck', null, {params: {
        id: this.id,
        password: this.password
      }}).then((response) => {
        this.$emit('checkUser', response.data);
      });
    }
  },
};
</script>

<style></style>