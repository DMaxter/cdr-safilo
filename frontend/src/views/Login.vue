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
          height="370"
          width="450"
          style="border-radius: 15px"
        >
          <v-form @submit.prevent="login">
            <v-card-text>
              <v-text-field
                label="Utilizador"
                type="text"
                rounded
                variant="outlined"
                v-model="auth.email"
              ></v-text-field>
              <v-text-field
                label="Palavra-passe"
                type="password"
                rounded
                variant="outlined"
                hide-details
                v-model="auth.password"
              ></v-text-field>
            </v-card-text>
            <v-card-actions class="d-flex flex-column">
              <v-btn color="#616161" text @click="showRecover()">
                Esqueceu-se da palavra passe?
              </v-btn>

              <v-btn width="33%" rounded class="mt-8 text-white customGradient" type="submit"
                >Entrar
              </v-btn>
            </v-card-actions>
            <Message
              v-model="failure"
              message="O nome de utilizador ou a palavra-passe estão incorretos"
            />
          </v-form>
        </v-card>
      </v-col>
    </v-row>
    <RecoveryCode v-model="recover" />
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

const recover = ref(false);

async function login() {
  try {
    await Backend.login(auth);
    router.push("profile");
  } catch (error) {
    failure.value = true;
    console.error(error);
  }
}

function showRecover() {
  recover.value = true;
}
</script>

<style lang="scss" scoped></style>
