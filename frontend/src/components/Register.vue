<template>
  <MDBRow 
    tag="form" 
    @submit.prevent="checkForm"
    @submit="handleRegister"
    class="g-3 needs-validation" 
    novalidate
  >
    <MDBCol md="6" class="offset-md-3">
      <h3>註冊</h3>
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
      <MDBInput
        label="電子信箱"
        v-model="user.email"
        invalidFeedback="請輸入電子信箱"
        required
      />
    </MDBCol>
    <MDBCol md="6" class="offset-md-3">
      <MDBBtn color="primary" type="submit" :disabled="loading">
        <span
          v-show="loading"
          class="spinner-border spinner-border-sm"
        ></span>
        <span>註冊</span>        
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
  name: "Register",
  data() {
    return {
      successful: false,
      loading: false,
      message: "",
      user: {
        username: "",
        password: "",
        email: ""
      }
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    },
  },
  mounted() {
    if (this.loggedIn) {
      this.$router.push("/profile");
    }
  },
  methods: {
    handleRegister() {
      // 驗證
      if (!(!!this.user.username && !!this.user.password && !!this.user.email)) {
        return;
      }
      this.message = "";
      this.successful = false;
      this.loading = true;

      this.$store.dispatch("auth/register", this.user).then(
        (data) => {
          this.message = data.message;
          this.successful = true;
          this.loading = false;
        },
        (error) => {
          this.message =
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString();
          this.successful = false;
          this.loading = false;
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