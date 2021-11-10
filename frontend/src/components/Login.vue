<template>
  <div class="row">
    <div class="col m6 offset-m3 s12">
      <h4>登入</h4>
      <form id="login"
         tag="form" 
         @submit.prevent="checkForm"
         @submit="login"
      >
          <div class="row">
            <div class="input-field col s12">
              <input v-model="user.username" id="username" type="text" class="validate" required>
              <label for="username">帳號</label>
            </div>
            <div class="input-field col s12">
              <input v-model="user.password" id="password" type="password" class="validate" required>
              <label for="password">密碼</label>
            </div>
            <div class="input-field col s12">
              <p>
                <label for="rememberMe">
                    <input type="checkbox" class="filled-in" id="rememberMe" name="remember-me">
                    <span>記住我</span>
                </label>
              </p>
            </div>
            <div class="input-field col s12">
              <button class="btn waves-effect waves-light teal" type="submit" :disabled="loading">
                登入
              </button>
            </div>
            <div v-if="message" class="input-field col s12">
              <div :class="successful ? 'success' : 'error'" class="materialert">
                  <div class="material-icons">error_outline</div>
                <span>{{message}}</span>
              </div>
            </div>
          </div>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      loading: false,
      message: "",
      user: {
        username: "",
        password: ""
      }
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    },
  },
  created() {
    if (this.loggedIn) {
      this.$router.push("/profile");
    }
  },
  methods: {
    login() {
      // 驗證
      if (!(!!this.user.username && !!this.user.password)) {
        return;
      }
      this.loading = true;
      this.$store.dispatch("auth/login", this.user).then(
        () => {
          this.$router.push("profile");
          this.$router.go(); // 登入後需刷新頁面，materialize的navbar dropdown才會生效
        },
        (error) => {
          this.loading = false;
          this.message =
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString();
        }
      );
    },
  },
  setup() {
    const checkForm = e => {
      e.target.classList.add("was-validated");
    };
    return {
      checkForm
    };
  }
};
</script>