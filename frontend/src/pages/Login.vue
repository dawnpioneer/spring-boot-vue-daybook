<template>
  <q-layout>
    <q-page-container>
      <q-page class="flex bg-image flex-center">
        <q-card v-bind:style="$q.screen.lt.sm?{'width': '80%'}:{'width':'50%'}">
          <q-card-section>
            <div class="text-center q-pt-lg">
              <div class="col text-h5 ellipsis">
                登入
              </div>
            </div>
          </q-card-section>
          <q-card-section>
            <q-form
              class="q-gutter-md"
            >
              <q-input
                filled
                v-model="user.username"
                label="帳號"
                lazy-rules
              />

              <q-input
                type="password"
                filled
                v-model="user.password"
                label="密碼"
                lazy-rules
                v-on:keyup.enter="submit"
              />

              <div>
                <q-btn
                  unelevated
                  color="primary"
                  label="登入"
                  :disable="!user.username || !user.password"
                  @click="submit"
                />
              </div>
            </q-form>
          </q-card-section>
        </q-card>
      </q-page>
    </q-page-container>
  </q-layout>
</template>

<style>

.bg-image {
  background-image: linear-gradient(135deg, #363636 0%, #26A69A 100%);
}
</style>

<script>
export default {
  name: "login",
  data() {
    return {
      isPwd: true,
      user: {
        username: "",
        password: ""
      }
    };
  },

  created() {
    console.info("login->created");
    this.init();
  },

  methods: {
    init() {
      console.info("login->init");
    },

    submit() {
      if (!this.user.username) {
        this.$q.notify("帳號不得為空！");
        return;
      }

      if (!this.user.password) {
        this.$q.notify("密碼不得為空！");
        return;
      }

      this.$q.loading.show({
        message: "登錄中"
      });

      this.$store.dispatch("user/login", this.user)
        .then(async (data) => {
          this.$router.push("/");
          this.$q.loading.hide();
        })
        .catch(e => {
          this.$q.loading.hide();
          console.error(e);
        });
    }
  }
};
</script>