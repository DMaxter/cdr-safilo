<template>
  <v-container fluid class="d-flex flex-column align-center justify-center fill-height">
    <v-row justify="center" align="center">
      <v-img :src="CDRLogo" contain width="350px"></v-img>
    </v-row>
    <v-row justify="center" align="center">
      <v-col cols="auto">
        <v-card
          title="Iniciar Sessão"
          elevation="12"
          color="#FAFAFA"
          height="400"
          width="450"
          style="border-radius: 15px"
        >
          <v-form @submit.prevent="login">
            <v-divider></v-divider>
            <v-card-text>
              <v-text-field
                label="Utilizador"
                type="text"
                rounded
                required
                variant="outlined"
                v-model="auth.email"
              ></v-text-field>
              <v-text-field
                label="Palavra-passe"
                type="password"
                rounded
                required
                variant="outlined"
                hide-details
                v-model="auth.password"
              ></v-text-field>
            </v-card-text>
            <v-card-actions>
              <div justify="center" align="center" style="width: 100%">
                <v-btn color="#616161" text @click="router.push('recoverCode')">
                  Esqueceu-se da palavra passe?
                </v-btn>

                <v-btn width="33%" tile class="mt-16 text-white customGradient" type="submit"
                  >Entrar
                </v-btn>

                <Message
                  v-model="failure"
                  message="O nome de utilizador ou palavra-passe está incorreto"
                />
              </div>
            </v-card-actions>
          </v-form>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts" setup>
import Backend from "@/router/backend";
import AuthDto from "@/models/AuthDto";
import CDRLogo from "@/assets/logo.png";
import { useRouter } from "vue-router";
import { ref, reactive } from "vue";

const failure = ref(false);
const auth = reactive(new AuthDto());
const router = useRouter();

async function login() {
  try {
    await Backend.login(auth);
    router.push("profile");
  } catch (error) {
    failure.value = true;
    console.error(error);
  }
}
</script>

<style lang="scss" scoped></style>
