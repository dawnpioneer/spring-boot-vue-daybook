<template>
  <div id="app">
    <MDBNavbar expand="lg" light bg="light" container>
      <MDBNavbarBrand href="/home">Daybook</MDBNavbarBrand>
      <!-- Toggle button -->
      <MDBNavbarToggler
        target="#navbarRightAlignExample"
        @click="collapse = !collapse"
      ></MDBNavbarToggler>
      <!-- Collapsible wrapper -->
      <MDBCollapse v-model="collapse" id="navbarRightAlignExample">
        <MDBNavbarNav right class="mb-2 mb-lg-0">
          <!-- Right links -->
          <MDBNavbarItem to="/home">
            <i class="fas fa-home"></i> 首頁
          </MDBNavbarItem>
          <MDBNavbarItem v-if="showAdminBoard" to="/admin">
            Admin Board
          </MDBNavbarItem>
          <MDBNavbarItem v-if="showModeratorBoard" to="/mod">
            Moderator Board
          </MDBNavbarItem>
          <MDBNavbarItem v-if="currentUser" to="/user">
            User Board
          </MDBNavbarItem>
          <MDBNavbarItem v-if="!currentUser" to="/register">
            <i class="fas fa-user-plus"></i> 註冊
          </MDBNavbarItem>
          <MDBNavbarItem v-if="!currentUser" to="/login">
             <i class="fas fa-sign-in-alt"></i> 登入
          </MDBNavbarItem>
          <!-- Navbar dropdown -->
          <MDBDropdown v-if="currentUser" class="nav-item" v-model="dropdown">
            <MDBDropdownToggle
              tag="a"
              class="nav-link"
              @click="dropdown = !dropdown"
              >{{ currentUser.username }}</MDBDropdownToggle
            >
            <MDBDropdownMenu>
              <MDBDropdownItem href="/profile">個人資料</MDBDropdownItem>
              <MDBDropdownItem href="#" @click.prevent="logOut">登出</MDBDropdownItem>
            </MDBDropdownMenu>
          </MDBDropdown>
          <!-- Right links -->
        </MDBNavbarNav>
      </MDBCollapse>
      <!-- Collapsible wrapper -->
    </MDBNavbar>

    <div class="container">
      <router-view />
    </div>

  </div>
</template>

<script>
import { ref } from 'vue';

export default {
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    showAdminBoard() {
      if (this.currentUser && this.currentUser['roles']) {
        return this.currentUser['roles'].includes('ROLE_ADMIN');
      }

      return false;
    },
    showModeratorBoard() {
      if (this.currentUser && this.currentUser['roles']) {
        return this.currentUser['roles'].includes('ROLE_MODERATOR');
      }

      return false;
    }
  },
  methods: {
    logOut() {
      this.$store.dispatch('auth/logout');
      this.$router.push('/login');
    }
  },
  setup() {
    const collapse = ref(false);
    const dropdown = ref(false);

    return {
      collapse,
      dropdown
    }
  }
};
</script>
