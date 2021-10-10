<template>
  <MDBRow 
    tag="form" 
    @submit.prevent="checkForm"
    @submit="handleLogin"
    class="g-3 needs-validation" 
    novalidate
  >
    <MDBCol md="6" class="offset-md-3">
      <h3>登入</h3>
    </MDBCol>  
    <MDBCol md="6" class="offset-md-3">
      <MDBInput
        label="帳號"
        v-model="user.username"
        invalidFeedback="請輸入帳號"
        required
      />
    </MDBCol>
    <MDBCol md="6" class="offset-md-3">
      <MDBInput
        label="密碼"
        v-model="user.password"
        invalidFeedback="請輸入密碼"
        type="password"
        required
      />
    </MDBCol>
    <MDBCol md="6" class="offset-md-3">
      <MDBCheckbox
        label="記住我"
        v-model="checkbox1"
      />
    </MDBCol>
    <MDBCol md="6" class="offset-md-3">
      <MDBBtn color="primary" type="submit" :disabled="loading">
        <span
          v-show="loading"
          class="spinner-border spinner-border-sm"
        ></span>
        <span>登入</span>        
      </MDBBtn>
    </MDBCol>
    <MDBCol md="6" class="offset-md-3 alert"
      v-if="message"
      :class="successful ? 'alert-success' : 'alert-danger'">
      {{ message }}
    </MDBCol>
  </MDBRow>
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
    handleLogin() {
      // 驗證
      if (!(!!this.user.username && !!this.user.password)) {
        return;
      }
      this.loading = true;
      this.$store.dispatch("auth/login", this.user).then(
        () => {
          this.$router.push("/profile");
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